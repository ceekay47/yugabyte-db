// Copyright (c) Yugabyte, Inc.

package com.yugabyte.yw.controllers;

import static com.yugabyte.yw.common.AssertHelper.assertBadRequest;
import static com.yugabyte.yw.common.AssertHelper.assertOk;
import static com.yugabyte.yw.common.AssertHelper.assertPlatformException;
import static com.yugabyte.yw.common.AssertHelper.assertUnauthorized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.test.Helpers.contentAsString;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.yugabyte.yw.common.*;
import com.yugabyte.yw.controllers.handlers.NodeAgentHandler;
import com.yugabyte.yw.forms.NodeAgentForm;
import com.yugabyte.yw.forms.NodeInstanceFormData;
import com.yugabyte.yw.forms.NodeInstanceFormData.NodeInstanceData;
import com.yugabyte.yw.models.AvailabilityZone;
import com.yugabyte.yw.models.Customer;
import com.yugabyte.yw.models.NodeAgent;
import com.yugabyte.yw.models.NodeAgent.State;
import com.yugabyte.yw.models.Provider;
import com.yugabyte.yw.models.Region;
import com.yugabyte.yw.models.Users;
import com.yugabyte.yw.models.helpers.NodeConfiguration;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.MockitoJUnitRunner;
import play.Application;
import play.libs.Json;
import play.mvc.Result;

@RunWith(MockitoJUnitRunner.class)
public class NodeAgentControllerTest extends FakeDBApplication {
  @Mock private Config mockAppConfig;
  @Mock private ConfigHelper mockConfigHelper;
  @Mock private PlatformScheduler mockPlatformScheduler;

  private NodeAgentHandler nodeAgentHandler;
  private Customer customer;
  private Provider provider;
  private Region region;
  private AvailabilityZone zone;
  private Users user;

  @Override
  protected Application provideApplication() {
    // Setup the config for node configurations
    String nodeAgentConfig = "yb.node_agent.preflight_checks.";
    Map<String, Object> dummyConfigMap = Maps.newHashMap();
    dummyConfigMap.put(nodeAgentConfig + "internet_connection", "true");
    dummyConfigMap.put(nodeAgentConfig + "python_version", "2.7");
    dummyConfigMap.put(nodeAgentConfig + "ram_size", "2");
    dummyConfigMap.put(nodeAgentConfig + "prometheus_no_node_exporter", "true");
    dummyConfigMap.put(nodeAgentConfig + "ports", "true");
    dummyConfigMap.put(nodeAgentConfig + "ntp_service_status", "true");
    dummyConfigMap.put(nodeAgentConfig + "tmp_dir_space", "100");
    dummyConfigMap.put(nodeAgentConfig + "cpu_cores", "1");
    dummyConfigMap.put(nodeAgentConfig + "user", "fakeUser");
    dummyConfigMap.put(nodeAgentConfig + "user_group", "fakeGroup");
    dummyConfigMap.put(nodeAgentConfig + "prometheus_space", "100");
    dummyConfigMap.put(nodeAgentConfig + "pam_limits_writable", "true");
    dummyConfigMap.put(nodeAgentConfig + "mount_points", "true");
    dummyConfigMap.put(nodeAgentConfig + "home_dir_space", "100");
    return provideApplication(dummyConfigMap);
  }

  @Before
  public void setup() {
    customer = ModelFactory.testCustomer();
    provider = ModelFactory.onpremProvider(customer);
    region = Region.create(provider, "region-1", "Region 1", "yb-image-1");
    zone = AvailabilityZone.createOrThrow(region, "az-1", "AZ 1", "subnet-1");
    user = ModelFactory.testUser(customer);
    nodeAgentHandler = new NodeAgentHandler(mockAppConfig, mockConfigHelper, mockPlatformScheduler);
  }

  private Result registerNodeAgent(NodeAgentForm formData) {
    return FakeApiHelper.doRequestWithAuthTokenAndBody(
        "POST",
        "/api/customers/" + customer.uuid + "/node_agents",
        user.createAuthToken(),
        Json.toJson(formData));
  }

  private Result getNodeAgent(UUID nodeAgentUuid, String jwt) {
    return FakeApiHelper.doRequestWithJWT(
        "GET", "/api/customers/" + customer.uuid + "/node_agents/" + nodeAgentUuid, jwt);
  }

  private Result pingNodeAgent(UUID nodeAgentUuid) {
    return FakeApiHelper.doGetRequestNoAuth(
        "/api/customers/" + customer.uuid + "/node_agents/" + nodeAgentUuid + "/state");
  }

  private Result updateNodeState(UUID nodeAgentUuid, NodeAgentForm formData, String jwt) {
    String uri = "/api/customers/" + customer.uuid + "/node_agents/" + nodeAgentUuid + "/state";
    return FakeApiHelper.doRequestWithJWTAndBody("PUT", uri, jwt, Json.toJson(formData));
  }

  private Result updateNode(UUID nodeAgentUuid, String jwt) {
    String uri = "/api/customers/" + customer.uuid + "/node_agents/" + nodeAgentUuid;
    return FakeApiHelper.doRequestWithJWTAndBody("PUT", uri, jwt, Json.newObject());
  }

  private Result createNode(UUID zoneUuid, NodeInstanceData details, String jwt) {
    String uri = "/api/customers/" + customer.uuid + "/zones/" + zoneUuid + "/nodes";
    NodeInstanceFormData formData = new NodeInstanceFormData();
    formData.nodes = Lists.newArrayList(details);
    return FakeApiHelper.doRequestWithJWTAndBody("POST", uri, jwt, Json.toJson(formData));
  }

  private Result unregisterNodeAgent(UUID nodeAgentUuid, String jwt) {
    return FakeApiHelper.doRequestWithJWT(
        "DELETE", "/api/customers/" + customer.uuid + "/node_agents/" + nodeAgentUuid, jwt);
  }

  @Test
  public void testNodeAgentRegistrationWorkflow() {
    NodeAgentForm formData = new NodeAgentForm();
    formData.name = "test";
    formData.ip = "10.20.30.40";
    formData.version = "2.12.0";
    // Register the node agent.
    Result result = registerNodeAgent(formData);
    assertOk(result);
    NodeAgent nodeAgent = Json.fromJson(Json.parse(contentAsString(result)), NodeAgent.class);
    assertNotNull(nodeAgent.uuid);
    UUID nodeAgentUuid = nodeAgent.uuid;

    // Ping for node state.
    result = pingNodeAgent(nodeAgentUuid);
    assertOk(result);
    State state = Json.fromJson(Json.parse(contentAsString(result)), State.class);
    assertEquals(State.REGISTERING, state);
    String jwt = nodeAgentHandler.getClientToken(nodeAgentUuid, user.uuid);
    result = assertPlatformException(() -> registerNodeAgent(formData));
    assertBadRequest(result, "Node agent is already registered");
    result = getNodeAgent(nodeAgentUuid, jwt);
    assertOk(result);
    // Report live to the server.
    formData.state = State.LIVE;
    result = updateNodeState(nodeAgentUuid, formData, jwt);
    assertOk(result);
    // Ping for node state.
    result = pingNodeAgent(nodeAgentUuid);
    assertOk(result);
    state = Json.fromJson(Json.parse(contentAsString(result)), State.class);
    assertEquals(State.LIVE, state);
    NodeInstanceData testNode = new NodeInstanceData();
    testNode.ip = "10.20.30.40";
    testNode.region = region.code;
    testNode.zone = zone.code;
    testNode.instanceType = "fake_instance_type";
    testNode.sshUser = "ssh-user";
    // Missing node configurations in the payload.
    result = assertPlatformException(() -> createNode(zone.uuid, testNode, jwt));
    assertEquals(result.status(), BAD_REQUEST);
    // Accepted value for NTP_SERVICE_STATUS is "running".
    testNode.nodeConfigurations = getTestNodeConfigurationsSet();
    result = createNode(zone.uuid, testNode, jwt);
    assertOk(result);

    NodeConfiguration pamNode =
        testNode
            .nodeConfigurations
            .stream()
            .filter(n -> n.type == NodeConfiguration.Type.PAM_LIMITS_WRITABLE)
            .findFirst()
            .get();
    NodeConfiguration errCheck =
        new NodeConfiguration(NodeConfiguration.Type.PAM_LIMITS_WRITABLE, "false");
    testNode.nodeConfigurations.remove(pamNode);
    testNode.nodeConfigurations.add(errCheck);
    // Set an unaccepted value.
    result = assertPlatformException(() -> createNode(zone.uuid, testNode, jwt));
    // Missing preflight checks should return an error
    assertEquals(result.status(), BAD_REQUEST);
    result = unregisterNodeAgent(nodeAgentUuid, jwt);
    assertOk(result);
    result = assertPlatformException(() -> getNodeAgent(nodeAgentUuid, jwt));
    assertUnauthorized(result, "Invalid token");
  }

  @Test
  public void testNodeAgentUpgradeWorkflow() {
    NodeAgentForm formData = new NodeAgentForm();
    formData.name = "test";
    formData.ip = "10.20.30.40";
    formData.version = "2.12.0";
    Result result = registerNodeAgent(formData);
    assertOk(result);
    NodeAgent nodeAgent = Json.fromJson(Json.parse(contentAsString(result)), NodeAgent.class);
    assertNotNull(nodeAgent.uuid);
    UUID nodeAgentUuid = nodeAgent.uuid;
    String certPath = nodeAgent.config.get(NodeAgent.CERT_DIR_PATH_PROPERTY);
    // Ping for node state.
    result = pingNodeAgent(nodeAgentUuid);
    assertOk(result);
    State state = Json.fromJson(Json.parse(contentAsString(result)), State.class);
    assertEquals(State.REGISTERING, state);
    AtomicReference<String> jwtRef =
        new AtomicReference<>(nodeAgentHandler.getClientToken(nodeAgentUuid, user.uuid));
    result = assertPlatformException(() -> registerNodeAgent(formData));
    assertBadRequest(result, "Node agent is already registered");
    result = getNodeAgent(nodeAgentUuid, jwtRef.get());
    assertOk(result);
    // Report live to the server.
    formData.state = State.LIVE;
    result = updateNodeState(nodeAgentUuid, formData, jwtRef.get());
    assertOk(result);
    // Ping for node state.
    result = pingNodeAgent(nodeAgentUuid);
    assertOk(result);
    state = Json.fromJson(Json.parse(contentAsString(result)), State.class);
    assertEquals(State.LIVE, state);
    // Initiate upgrade in the server.
    nodeAgent = NodeAgent.getOrBadRequest(customer.uuid, nodeAgentUuid);
    nodeAgent.state = State.UPGRADE;
    nodeAgent.save();
    assertOk(result);
    // Ping for node state.
    result = pingNodeAgent(nodeAgentUuid);
    assertOk(result);
    state = Json.fromJson(Json.parse(contentAsString(result)), State.class);
    assertEquals(State.UPGRADE, state);
    // Report upgrading to the server.
    formData.state = State.UPGRADING;
    result = updateNodeState(nodeAgentUuid, formData, jwtRef.get());
    assertOk(result);
    // Ping for node state.
    result = pingNodeAgent(nodeAgentUuid);
    assertOk(result);
    state = Json.fromJson(Json.parse(contentAsString(result)), State.class);
    assertEquals(State.UPGRADING, state);
    // Reach out to the server to refresh certs.
    result = updateNode(nodeAgentUuid, jwtRef.get());
    assertOk(result);
    nodeAgent = Json.fromJson(Json.parse(contentAsString(result)), NodeAgent.class);
    assertEquals(certPath, nodeAgent.config.get(NodeAgent.CERT_DIR_PATH_PROPERTY));
    // Complete upgrading.
    formData.state = State.UPGRADED;
    result = updateNodeState(nodeAgentUuid, formData, jwtRef.get());
    assertOk(result);
    nodeAgent = Json.fromJson(Json.parse(contentAsString(result)), NodeAgent.class);
    assertNotEquals(certPath, nodeAgent.config.get(NodeAgent.CERT_DIR_PATH_PROPERTY));
    certPath = nodeAgent.config.get(NodeAgent.CERT_DIR_PATH_PROPERTY);
    // Ping for node state.
    result = pingNodeAgent(nodeAgentUuid);
    assertOk(result);
    state = Json.fromJson(Json.parse(contentAsString(result)), State.class);
    assertEquals(State.UPGRADED, state);
    // Restart the node agent and report live to the server.
    formData.state = State.LIVE;
    // Old key is invalid.
    assertThrows(
        "Invalid token",
        PlatformServiceException.class,
        () -> updateNodeState(nodeAgentUuid, formData, jwtRef.get()));
    jwtRef.set(nodeAgentHandler.getClientToken(nodeAgentUuid, user.uuid));
    result = updateNodeState(nodeAgentUuid, formData, jwtRef.get());
    assertOk(result);
    nodeAgent = Json.fromJson(Json.parse(contentAsString(result)), NodeAgent.class);
    assertEquals(certPath, nodeAgent.config.get(NodeAgent.CERT_DIR_PATH_PROPERTY));
    // Ping for node state.
    result = pingNodeAgent(nodeAgentUuid);
    assertOk(result);
    state = Json.fromJson(Json.parse(contentAsString(result)), State.class);
    assertEquals(State.LIVE, state);
    NodeInstanceData testNode = new NodeInstanceData();
    testNode.ip = "10.20.30.40";
    testNode.region = region.code;
    testNode.zone = zone.code;
    testNode.instanceType = "fake_instance_type";
    testNode.sshUser = "ssh-user";
    // Get a new JWT after the update.
    String updatedJwt = nodeAgentHandler.getClientToken(nodeAgentUuid, user.uuid);
    NodeConfiguration nodeConfig = new NodeConfiguration();
    testNode.nodeConfigurations = Sets.newSet(nodeConfig);
    nodeConfig.setType(NodeConfiguration.Type.NTP_SERVICE_STATUS);
    nodeConfig.setValue("true");
    // Missing preflight checks should return an error
    result = assertPlatformException(() -> createNode(zone.uuid, testNode, updatedJwt));
    assertEquals(result.status(), BAD_REQUEST);
    // Accepted value for NTP_SERVICE_STATUS is "running".
    testNode.nodeConfigurations = getTestNodeConfigurationsSet();
    result = createNode(zone.uuid, testNode, updatedJwt);
    assertOk(result);
    result = unregisterNodeAgent(nodeAgentUuid, updatedJwt);
    assertOk(result);
    result = assertPlatformException(() -> getNodeAgent(nodeAgentUuid, updatedJwt));
    assertUnauthorized(result, "Invalid token");
  }

  public Set<NodeConfiguration> getTestNodeConfigurationsSet() {
    Set<NodeConfiguration> nodeConfigurations = Sets.newSet();
    nodeConfigurations.add(
        new NodeConfiguration(NodeConfiguration.Type.PROMETHEUS_NO_NODE_EXPORTER, "true"));
    nodeConfigurations.add(new NodeConfiguration(NodeConfiguration.Type.PYTHON_VERSION, "3.0.0"));
    nodeConfigurations.add(new NodeConfiguration(NodeConfiguration.Type.USER, "fakeUser"));
    nodeConfigurations.add(new NodeConfiguration(NodeConfiguration.Type.USER_GROUP, "fakeGroup"));
    nodeConfigurations.add(
        new NodeConfiguration(NodeConfiguration.Type.INTERNET_CONNECTION, "true"));
    nodeConfigurations.add(new NodeConfiguration(NodeConfiguration.Type.TMP_DIR_SPACE, "10000"));
    nodeConfigurations.add(new NodeConfiguration(NodeConfiguration.Type.PROMETHEUS_SPACE, "10000"));
    nodeConfigurations.add(
        new NodeConfiguration(NodeConfiguration.Type.PAM_LIMITS_WRITABLE, "true"));
    nodeConfigurations.add(new NodeConfiguration(NodeConfiguration.Type.HOME_DIR_SPACE, "10000"));
    nodeConfigurations.add(
        new NodeConfiguration(NodeConfiguration.Type.PORTS, "{\"54422\":\"true\"}"));
    nodeConfigurations.add(
        new NodeConfiguration(
            NodeConfiguration.Type.MOUNT_POINTS, "{\"/home/yugabyte\":\"true\"}"));
    nodeConfigurations.add(
        new NodeConfiguration(NodeConfiguration.Type.NTP_SERVICE_STATUS, "true"));
    nodeConfigurations.add(new NodeConfiguration(NodeConfiguration.Type.CPU_CORES, "1"));
    nodeConfigurations.add(new NodeConfiguration(NodeConfiguration.Type.RAM_SIZE, "6"));
    return nodeConfigurations;
  }
}
