package com.yugabyte.yw.models.helpers;

import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class NodeConfigurationTest {

  Config appConfig;

  @Before
  public void setup() {
    HashMap<String, String> dummyConfigMap = Maps.newHashMap();
    String nodeAgentConfig = "yb.node_agent.";
    dummyConfigMap.put(nodeAgentConfig + "internet_connection", "true");
    dummyConfigMap.put(nodeAgentConfig + "ram_size", "2");
    dummyConfigMap.put(nodeAgentConfig + "python_version", "2.7");
    dummyConfigMap.put(nodeAgentConfig + "prometheus_no_node_exporter", "true");
    dummyConfigMap.put(nodeAgentConfig + "ports", "true");
    dummyConfigMap.put(nodeAgentConfig + "pam_limits_writable", "true");
    dummyConfigMap.put(nodeAgentConfig + "tmp_dir_space", "100");
    dummyConfigMap.put(nodeAgentConfig + "cpu_cores", "1");
    dummyConfigMap.put(nodeAgentConfig + "user", "dummyUser");
    appConfig = ConfigFactory.parseMap(dummyConfigMap);
  }

  @Test
  public void testValidatePredicate() {
    NodeConfiguration.ValidationPredicate intCompareCheck =
        NodeConfiguration.ValidationPredicate.INT_VALUE_ABOVE;
    Assert.assertTrue(intCompareCheck.test("100", "10"));
    Assert.assertFalse(intCompareCheck.test("100", "1000"));

    NodeConfiguration.ValidationPredicate versionValidation =
        NodeConfiguration.ValidationPredicate.VERSION_ABOVE;
    Assert.assertTrue(versionValidation.test("2.7", "2.6.4"));
    Assert.assertFalse(versionValidation.test("2.7", "3"));

    NodeConfiguration.ValidationPredicate equalsCheck =
        NodeConfiguration.ValidationPredicate.EQUALS_STRING;
    Assert.assertTrue(equalsCheck.test("fakeString", "fakeString"));
    Assert.assertFalse(equalsCheck.test("fakeString", "realString"));

    NodeConfiguration.ValidationPredicate jsonCheck =
        NodeConfiguration.ValidationPredicate.UNMARSHAL_AND_EQUALS;
    Assert.assertTrue(jsonCheck.test("{\"ports\":\"true\"}", "true"));
    Assert.assertFalse(jsonCheck.test("{\"ports\":\"false\"}", "true"));
  }

  @Test
  public void testNodeConfiguration() {
    // Test Internet Connection Configuration
    NodeConfiguration internetConnection =
        new NodeConfiguration(NodeConfiguration.Type.INTERNET_CONNECTION, "true");
    Assert.assertTrue(internetConnection.isConfigured(appConfig));
    internetConnection.setValue("false");
    Assert.assertFalse(internetConnection.isConfigured(appConfig));

    // Test Ram Size
    NodeConfiguration ramSize = new NodeConfiguration(NodeConfiguration.Type.RAM_SIZE, "6");
    Assert.assertTrue(ramSize.isConfigured(appConfig));
    ramSize.setValue("1");
    Assert.assertFalse(ramSize.isConfigured(appConfig));

    // Test Python Version
    NodeConfiguration pyVersion =
        new NodeConfiguration(NodeConfiguration.Type.PYTHON_VERSION, "2.9.1");
    Assert.assertTrue(pyVersion.isConfigured(appConfig));
    pyVersion.setValue("2.3.1");
    Assert.assertFalse(pyVersion.isConfigured(appConfig));
  }
}
