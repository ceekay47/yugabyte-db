// Copyright (c) YugaByte, Inc.

package com.yugabyte.yw.models.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.yugabyte.yw.common.utils.Pair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.map.HashedMap;

/** Pair of node configuration type and its value. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "A node configuration.")
public class NodeConfiguration {
  @NotNull
  @ApiModelProperty(required = true)
  public Type type;

  @NotNull
  @ApiModelProperty(value = "unlimited", required = true)
  @EqualsAndHashCode.Exclude
  public String value;

  /**
   * Checks if the value is accepted according to the predicate in the type.
   *
   * @return true if it is accepted or configured, else false.
   */
  @JsonIgnore
  public boolean isConfigured(Config appConfig) {
    return type.isConfigured(value, appConfig);
  }

  /** Type of the configuration. The predicate can be minimum value comparison like ulimit. */
  public enum Type {
    // TODO add more types.
    // Add predicates for the preflight checks
    NTP_SERVICE_STATUS(ValidationPredicate.EQUALS_STRING),
    PROMETHEUS_SPACE(ValidationPredicate.INT_VALUE_ABOVE),
    MOUNT_POINTS(ValidationPredicate.UNMARSHAL_AND_EQUALS),
    USER(ValidationPredicate.EQUALS_STRING),
    USER_GROUP(ValidationPredicate.EQUALS_STRING),
    HOME_DIR_SPACE(ValidationPredicate.INT_VALUE_ABOVE),
    RAM_SIZE(ValidationPredicate.INT_VALUE_ABOVE),
    INTERNET_CONNECTION(ValidationPredicate.EQUALS_STRING),
    CPU_CORES(ValidationPredicate.INT_VALUE_ABOVE),
    PROMETHEUS_NO_NODE_EXPORTER(ValidationPredicate.EQUALS_STRING),
    TMP_DIR_SPACE(ValidationPredicate.INT_VALUE_ABOVE),
    PAM_LIMITS_WRITABLE(ValidationPredicate.EQUALS_STRING),
    PORTS(ValidationPredicate.UNMARSHAL_AND_EQUALS),
    PYTHON_VERSION(ValidationPredicate.VERSION_ABOVE);

    // Predicate to test if a value is acceptable.
    private final ValidationPredicate predicate;

    private Type(ValidationPredicate predicate) {
      this.predicate = predicate;
    }

    private Type() {
      this.predicate = null;
    }

    public boolean isConfigured(String value, Config appConfig) {
      if (predicate == null) {
        return true;
      }
      String configKey = this.name().toLowerCase();
      String configPath = String.format("yb.node_agent.preflight_checks.%s", configKey);

      if (!appConfig.hasPath(configPath)) {
        return true;
      }
      return predicate.test(value, appConfig.getString(configPath));
    }
  }

  public enum ValidationPredicate {
    // Test if a given version x.y.z is greater than x.y.z .
    VERSION_ABOVE(
        v -> {
          String versionRegex = "[0-9]+(\\.[0-9]+)*";
          String curVer = v.getFirst();

          if (!curVer.matches(versionRegex)) return false;

          String[] curVerParts = curVer.split("\\.");
          String[] reqParts = v.getSecond().split("\\.");

          int maxLength = Math.max(curVerParts.length, reqParts.length);

          for (int i = 0; i < maxLength; i++) {
            int curVerInt = i < curVerParts.length ? Integer.parseInt(curVerParts[i]) : 0;
            int reqVerInt = i < reqParts.length ? Integer.parseInt(reqParts[i]) : 0;
            if (curVerInt > reqVerInt) return true;
            if (curVerInt < reqVerInt) return false;
          }
          return true;
        }),
    // Test if an int value greater than the given value.
    INT_VALUE_ABOVE(
        v -> {
          try {
            int curVal = Integer.parseInt(v.getFirst());
            int expVal = Integer.parseInt(v.getSecond());
            return curVal >= expVal;
          } catch (NumberFormatException ex) {
            return false;
          }
        }),
    // Test if a string is equal to another string.
    EQUALS_STRING(v -> v.getFirst().equalsIgnoreCase(v.getSecond())),
    // Unmarshals the given input into a map and checks if all the values satisfy the expected
    // string.
    UNMARSHAL_AND_EQUALS(
        v -> {
          String expVal = v.getSecond();
          try {
            Map<String, String> result =
                new ObjectMapper().readValue(v.getFirst(), HashedMap.class);
            for (String val : result.values()) {
              if (!val.equalsIgnoreCase(expVal)) return false;
            }
          } catch (IOException e) {
            return false;
          }
          return true;
        });

    private final Predicate<Pair<String, String>> predicate;

    ValidationPredicate(Predicate<Pair<String, String>> predicate) {
      this.predicate = predicate;
    }

    public boolean test(String s, String v) {
      return predicate.test(new Pair<>(s, v));
    }
  }

  /** Group of types. This can be extended to add minimum requirements. */
  public enum TypeGroup {
    ALL(EnumSet.allOf(Type.class));

    private final Set<Type> requiredConfigTypes;

    private TypeGroup(Set<Type> requiredConfigTypes) {
      this.requiredConfigTypes = Collections.unmodifiableSet(requiredConfigTypes);
    }

    public Set<Type> getRequiredConfigTypes() {
      return requiredConfigTypes;
    }
  }
}
