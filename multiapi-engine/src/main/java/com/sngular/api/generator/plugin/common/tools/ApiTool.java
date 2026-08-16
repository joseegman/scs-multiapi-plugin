package com.sngular.api.generator.plugin.common.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sngular.api.generator.plugin.asyncapi.util.FactoryTypeEnum;
import com.sngular.api.generator.plugin.common.files.FileLocation;
import com.sngular.api.generator.plugin.common.model.TypeConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public final class ApiTool {

  public static final String FORMAT = "format";

  public static final String ALL_OF = "allOf";

  public static final String ANY_OF = "anyOf";

  public static final String ONE_OF = "oneOf";

  public static final String COMPONENTS = "components";

  public static final String SCHEMAS = "schemas";

  public static final String DEFS = "$defs";

  public static final String REQUIRED = "required";

  public static final String PARAMETERS = "parameters";

  public static final String RESPONSES = "responses";

  public static final String REQUEST_BODIES = "requestBodies";

  private static final String PACKAGE_SEPARATOR_STR = ".";

  private ApiTool() {
  }

  public static Iterator<Entry<String, JsonNode>> getProperties(final JsonNode schema) {
    return getNode(schema, "properties").fields();
  }

  public static JsonNode getNode(final JsonNode schema, final String nodeName) {
    return schema.get(nodeName);
  }

  public static String getRefValue(final JsonNode schema) {
    return getNode(schema, "$ref").textValue();
  }

  public static JsonNode getAdditionalProperties(final JsonNode schema) {
    return getNode(schema, "additionalProperties");
  }

  public static String getFormat(final JsonNode schema) {
    return getNodeAsString(schema, FORMAT);
  }

  public static String getNodeAsString(final JsonNode schema, final String nodeName) {
    return hasNode(schema, nodeName) ? getNode(schema, nodeName).textValue() : null;
  }

  public static boolean hasNode(final JsonNode schema, final String nodeName) {
    return Objects.nonNull(schema) && schema.has(nodeName);
  }

  public static JsonNode getAllOf(final JsonNode schema) {
    return getNode(schema, ALL_OF);
  }

  public static JsonNode getAnyOf(final JsonNode schema) {
    return getNode(schema, ANY_OF);
  }

  public static JsonNode getOneOf(final JsonNode schema) {
    return getNode(schema, ONE_OF);
  }

  public static boolean getNodeAsBoolean(final JsonNode schema, final String nodeName) {
    return hasNode(schema, nodeName) && getNode(schema, nodeName).booleanValue();
  }

  public static Object getNodeAsObject(final JsonNode schema, final String nodeName) {
    return hasNode(schema, nodeName) ? getNodeAsType(getNode(schema, nodeName)) : null;
  }

  private static Object getNodeAsType(final JsonNode node) {
    final Object result;
    if (node.isBigDecimal()) {
      result = BigDecimal.valueOf(node.asDouble());
    } else if (node.isBigInteger()) {
      result = BigInteger.valueOf(node.asLong());
    } else if (node.isBoolean()) {
      result = node.asBoolean();
    } else if (node.isFloat() || node.isDouble()) {
      result = node.asDouble();
    } else if (node.isInt() || node.isNumber()) {
      result = node.asInt();
    } else {
      result = node.asText();
    }
    return result;
  }

  public static String getNodeAsString(final JsonNode schema) {
    return Objects.nonNull(schema) ? schema.textValue() : null;
  }

  public static Iterator<Entry<String, JsonNode>> getFieldIterator(final JsonNode schema) {
    return Objects.nonNull(schema) ? schema.fields() : IteratorUtils.emptyIterator();
  }

  public static Iterator<Entry<String, JsonNode>> getFieldIterator(final JsonNode schema, final String nodeName) {
    return Objects.isNull(schema) || !hasNode(schema, nodeName) ?
               IteratorUtils.emptyIterator() : getNode(schema, nodeName).fields();
  }

  public static String getName(final JsonNode node) {
    return hasNode(node, "name") ? getNodeAsString(node, "name") : node.textValue();
  }

  public static List<String> getEnumValues(final JsonNode schema) {
    return new ArrayList<>(CollectionUtils.collect(
        IteratorUtils.toList(schema.get("enum").elements()),
        getTextValue()));
  }

  private static Transformer<JsonNode, String> getTextValue() {
    return JsonNode::asText;
  }

  public static JsonNode getItems(final JsonNode schema) {
    return getNode(schema, "items");
  }

  public static Map<String, JsonNode> getComponentSchemas(final JsonNode openApi) {
    final var schemasMap = getComponentSchemasByType(openApi, SCHEMAS);
    // JSON Schema 2020-12 / OpenAPI 3.1: reusable schemas may live under a
    // top-level `$defs` object instead of `components/schemas`.
    schemasMap.putAll(getDefsSchemas(openApi));
    return schemasMap;
  }

  private static Map<String, JsonNode> getDefsSchemas(final JsonNode openApi) {
    final var schemasMap = new HashMap<String, JsonNode>();
    if (hasNode(openApi, DEFS)) {
      final var defs = getNode(openApi, DEFS);
      defs.fieldNames().forEachRemaining(name -> schemasMap.put(
          DEFS.toUpperCase() + "/" + StringCaseUtils.titleToSnakeCase(name), getNode(defs, name)));
    }
    return schemasMap;
  }

  private static Map<String, JsonNode> getComponentSchemasByType(final JsonNode openApi, final String schemaType) {
    final var schemasMap = new HashMap<String, JsonNode>();

    if (hasNode(openApi, COMPONENTS)) {
      final var components = getNode(openApi, COMPONENTS);
      if (hasNode(components, schemaType)) {
        final var schemas = getNode(components, schemaType);
        final var schemasIt = schemas.fieldNames();
        schemasIt.forEachRemaining(name -> schemasMap.put(schemaType.toUpperCase() + "/" + StringCaseUtils.titleToSnakeCase(name),
                                                          getNode(schemas, name)));
      }
    }

    return schemasMap;
  }

  public static Map<String, JsonNode> getParameterSchemas(final JsonNode openApi) {
    return getComponentSchemasByType(openApi, PARAMETERS);
  }

  public static Map<String, JsonNode> getResponseSchemas(final JsonNode openApi) {
    return getComponentSchemasByType(openApi, RESPONSES);
  }

  public static Map<String, JsonNode> getRequestBodySchemas(final JsonNode openApi) {
    return getComponentSchemasByType(openApi, REQUEST_BODIES);
  }

  public static Map<String, JsonNode> getComponentSecuritySchemes(final JsonNode openApi) {
    return getComponentSchemasByType(openApi, "securitySchemes");
  }

  public static String getNumberType(final JsonNode schema) {
    final String type;
    if (hasType(schema)) {
      switch (getType(schema)) {
        case TypeConstants.DOUBLE:
          type = TypeConstants.DOUBLE;
          break;
        case TypeConstants.FLOAT:
          type = TypeConstants.FLOAT;
          break;
        case TypeConstants.NUMBER:
          type = TypeConstants.NUMBER;
          break;
        case TypeConstants.INT_64:
          type = TypeConstants.INT_64;
          break;
        case TypeConstants.INT_32:
          type = TypeConstants.INT_32;
          break;
        default:
          type = TypeConstants.INTEGER;
          break;
      }
    } else {
      type = TypeConstants.INTEGER;
    }
    return type;
  }

  public static boolean hasType(final JsonNode schema) {
    return hasNode(schema, "type");
  }

  public static String getType(final JsonNode schema) {
    if (!hasType(schema)) {
      return "";
    }
    final JsonNode typeNode = getNode(schema, "type");
    if (typeNode.isArray()) {
      return getArrayType(typeNode);
    }
    // OpenAPI 3.1 / JSON Schema 2020-12: a scalar `type: "null"` carries no concrete
    // Java type; degrade to object so we never emit an invalid `null` type.
    if (TypeConstants.NULL.equalsIgnoreCase(typeNode.textValue())) {
      return TypeConstants.OBJECT;
    }
    return StringUtils.defaultIfEmpty(typeNode.textValue(), "");
  }

  private static String getArrayType(final JsonNode typeNode) {
    // OpenAPI 3.1 / JSON Schema 2020-12: "type" may be an array (e.g. ["string", "null"]).
    // "null" only marks the type as nullable; keep the concrete (non-"null") types.
    final List<String> concreteTypes = new ArrayList<>();
    for (final JsonNode element : typeNode) {
      if (!TypeConstants.NULL.equalsIgnoreCase(element.asText())) {
        concreteTypes.add(element.asText());
      }
    }
    if (concreteTypes.isEmpty()) {
      // Only "null" (or an empty array): no concrete type to generate, fall back to object.
      return TypeConstants.OBJECT;
    }
    if (concreteTypes.size() > 1) {
      log.warn("Schema declares a multi-type union {}; only '{}' is generated, the remaining types are ignored.",
               concreteTypes, concreteTypes.get(0));
    }
    return concreteTypes.get(0);
  }

  public static boolean hasItems(final JsonNode schema) {
    return hasNode(schema, "items");
  }

  public static boolean hasRequired(final JsonNode schema) {
    return hasNode(schema, REQUIRED);
  }

  public static boolean hasName(JsonNode message) {
    return hasNode(message, "name");
  }

  public static boolean hasRef(final JsonNode schema) {
    // OpenAPI 3.1 / JSON Schema 2020-12 allow sibling keywords next to `$ref`
    // (e.g. `description`), and their ordering is not significant. A node is a
    // reference whenever it declares a top-level `$ref`, regardless of position.
    return hasNode(schema, "$ref");
  }

  public static boolean hasProperties(final JsonNode schema) {
    return hasNode(schema, "properties");
  }

  public static boolean hasContent(final JsonNode schema) {
    return hasNode(schema, "content");
  }

  public static boolean hasAdditionalProperties(final JsonNode schema) {
    return hasNode(schema, "additionalProperties");
  }

  public static boolean isObject(final JsonNode schema) {
    return hasType(schema) && TypeConstants.OBJECT.equalsIgnoreCase(getType(schema));
  }

  public static boolean isArray(final JsonNode schema) {
    return hasType(schema) && TypeConstants.ARRAY.equalsIgnoreCase(getType(schema));
  }

  public static boolean isComposed(final JsonNode schema) {
    return ApiTool.hasField(schema, ANY_OF, ALL_OF, ONE_OF);
  }

  public static boolean hasField(final JsonNode schema, final String... fieldNameArray) {
    final var nodeNamesList = Arrays.asList(fieldNameArray);
    return StringUtils.isNotEmpty(IteratorUtils.find(schema.fieldNames(), nodeNamesList::contains));
  }

  public static boolean isString(final JsonNode schema) {
    return hasType(schema) && TypeConstants.STRING.equalsIgnoreCase(getType(schema));
  }

  public static boolean isBoolean(final JsonNode schema) {
    return hasType(schema) && TypeConstants.BOOLEAN.equalsIgnoreCase(getType(schema));
  }

  public static boolean isNumber(final JsonNode schema) {
    return hasType(schema)
           && (TypeConstants.INTEGER.equalsIgnoreCase(getType(schema))
               || TypeConstants.NUMBER.equalsIgnoreCase(getType(schema))
               || TypeConstants.INT_64.equalsIgnoreCase(getType(schema))
               || TypeConstants.INT_32.equalsIgnoreCase(getType(schema)));
  }

  public static boolean isEnum(final JsonNode schema) {
    return schema.has("enum");
  }

  public static boolean isAllOf(final JsonNode schema) {
    return hasNode(schema, ALL_OF);
  }

  public static boolean isAnyOf(final JsonNode schema) {
    return hasNode(schema, ANY_OF);
  }

  public static boolean isOneOf(final JsonNode schema) {
    return hasNode(schema, ONE_OF);
  }

  public static boolean isDateTime(final JsonNode schema) {
    final boolean isDateTime;
    if (hasType(schema) && TypeConstants.STRING.equalsIgnoreCase(getType(schema))) {
      if (hasNode(schema, FORMAT)) {
        isDateTime = "date".equalsIgnoreCase(getNode(schema, FORMAT).textValue())
                     || "date-time".equalsIgnoreCase(getNode(schema, FORMAT).textValue());
      } else {
        isDateTime = false;
      }
    } else {
      isDateTime = false;
    }
    return isDateTime;
  }

  public static boolean isBinary(final JsonNode schema) {
    final boolean isMultipartFile;
    if (hasType(schema) && TypeConstants.STRING.equalsIgnoreCase(getType(schema))) {
      if (hasNode(schema, FORMAT) && "binary".equalsIgnoreCase(getNode(schema, FORMAT).textValue())) {
        isMultipartFile = true;
      } else {
        // OpenAPI 3.1 / JSON Schema 2020-12 replace `format: binary` with the
        // `contentEncoding` / `contentMediaType` keywords for binary payloads.
        isMultipartFile = hasNode(schema, "contentEncoding") || hasNode(schema, "contentMediaType");
      }
    } else {
      isMultipartFile = false;
    }
    return isMultipartFile;
  }

  public static boolean isNullable(final JsonNode schema) {
    if (Objects.isNull(schema)) {
      return false;
    }
    // OpenAPI 3.0 `nullable: true`.
    if (getNodeAsBoolean(schema, "nullable")) {
      return true;
    }
    // OpenAPI 3.1 idiom: `type: ["<type>", "null"]`.
    if (hasType(schema)) {
      final JsonNode typeNode = getNode(schema, "type");
      if (typeNode.isArray()) {
        for (final JsonNode element : typeNode) {
          if (TypeConstants.NULL.equalsIgnoreCase(element.asText())) {
            return true;
          }
        }
      } else {
        return TypeConstants.NULL.equalsIgnoreCase(typeNode.textValue());
      }
    }
    return false;
  }

  public static boolean hasPatternProperties(final JsonNode schema) {
    return hasNode(schema, "patternProperties");
  }

  public static JsonNode getPatternProperties(final JsonNode schema) {
    return getNode(schema, "patternProperties");
  }

  public static boolean isInlineObject(final JsonNode schema) {
    return Objects.nonNull(schema) && schema.isObject() && !hasType(schema) && !hasNode(schema, "properties")
           && !hasNode(schema, "$ref") && !isComposed(schema) && !hasNode(schema, "items") && !hasNode(schema, "enum")
           && !hasNode(schema, "additionalProperties") && !hasNode(schema, "patternProperties")
           && hasNonSchemaFields(schema);
  }

  private static boolean hasNonSchemaFields(final JsonNode schema) {
    final Iterator<Entry<String, JsonNode>> fields = schema.fields();
    return fields.hasNext() && !isSchemaKeyword(fields.next().getKey());
  }

  private static boolean isSchemaKeyword(final String fieldName) {
    return "type".equals(fieldName) || "title".equals(fieldName) || "description".equals(fieldName)
           || "required".equals(fieldName) || "nullable".equals(fieldName) || "deprecated".equals(fieldName)
           || "example".equals(fieldName) || "default".equals(fieldName) || "format".equals(fieldName)
           || "const".equals(fieldName) || "readOnly".equals(fieldName) || "writeOnly".equals(fieldName)
           || "discriminator".equals(fieldName) || "xml".equals(fieldName) || "externalDocs".equals(fieldName);
  }

  public static boolean hasPrefixItems(final JsonNode schema) {
    return hasNode(schema, "prefixItems");
  }

  public static JsonNode getPrefixItems(final JsonNode schema) {
    return getNode(schema, "prefixItems");
  }

  public static List<JsonNode> findContentSchemas(final JsonNode schema) {
    return hasNode(schema, "content") ? schema.findValues("schema") : Collections.emptyList();
  }

  public static boolean checkIfRequired(final JsonNode schema, final String fieldName) {
    boolean isRequired = false;
    if (hasNode(schema, REQUIRED)) {
      final var fieldIt = getNode(schema, REQUIRED).elements();
      while (fieldIt.hasNext() && !isRequired) {
        isRequired = fieldName.equalsIgnoreCase(fieldIt.next().textValue());
      }
    }
    return isRequired;
  }

  public static Iterator<Entry<String, JsonNode>> getComponent(final JsonNode node, final String componentType) {
    Iterator<Entry<String, JsonNode>> result = Collections.emptyIterator();
    if (hasComponents(node) && hasNode(getNode(node, "components"), componentType)) {
      result = getNode(getNode(node, "components"), componentType).fields();
    }
    return result;
  }

  public static boolean hasComponents(final JsonNode node) {
    return hasNode(node, COMPONENTS);
  }

  public static JsonNode nodeFromFile(final FileLocation ymlParent, final String filePath, final FactoryTypeEnum factoryTypeEnum) throws IOException {
    final InputStream file;
    // Remote references (http/https/ftp/file URLs) are fetched directly from their URL.
    if (PathUtil.isRemoteUri(filePath)) {
      file = PathUtil.openUrlStream(URI.create(filePath).toURL());
    } else if (PathUtil.isAbsolutePath(filePath)) {
      // For absolute paths, open directly
      file = new FileInputStream(filePath);
    } else if (filePath.startsWith(PACKAGE_SEPARATOR_STR) || filePath.matches("^\\w.*$")) {
      // For relative paths starting with . or alphanumeric (package-like paths)
      file = ymlParent.getFileAtLocation(filePath);
    } else {
      if (filePath.contains(".jar!")) {
        var resource = filePath.substring(filePath.indexOf(".jar!") + 1);
        file = ApiTool.class.getClassLoader().getResourceAsStream(resource);
      } else {
        // Other paths - try as file path
        file = new FileInputStream(filePath);
      }
    }

    final ObjectMapper om;

    if (FactoryTypeEnum.YML.equals(factoryTypeEnum)) {
      om = new ObjectMapper(new YAMLFactory());
    } else {
      om = new ObjectMapper();
    }
    return om.readTree(file);
  }

  public static String getDescription(final JsonNode schema) {
    return getNodeAsString(schema, "description");
  }

  public static boolean isDeprecated(final JsonNode schema) {
    return getNodeAsBoolean(schema, "deprecated");
  }

  public static String getExample(final JsonNode schema) {
    // OpenAPI 3.0 uses a single `example`; OpenAPI 3.1 / JSON Schema 2020-12 use an
    // `examples` array. Prefer `example`, otherwise take the first `examples` entry.
    if (hasNode(schema, "example")) {
      return asExampleText(getNode(schema, "example"));
    }
    if (hasNode(schema, "examples")) {
      final JsonNode examples = getNode(schema, "examples");
      if (examples.isArray() && examples.elements().hasNext()) {
        return asExampleText(examples.elements().next());
      }
    }
    return null;
  }

  private static String asExampleText(final JsonNode example) {
    if (Objects.isNull(example) || example.isNull()) {
      return null;
    }
    return example.isValueNode() ? example.asText() : example.toString();
  }

  public static boolean hasConst(final JsonNode fieldBody) {
    return hasNode(fieldBody, "const");
  }

  public static Object getConst(final JsonNode fieldBody) {
    return getValue(getNode(fieldBody, "const"));
  }

  private static Object getValue(final JsonNode aConst) {
    Object value = null;
    if (Objects.nonNull(aConst)) {
      if (aConst.isTextual()) {
        value = aConst.textValue();
      } else if (aConst.isNumber()) {
        value = aConst.numberValue();
      } else if (aConst.isBoolean()) {
        value = aConst.booleanValue();
      } else if (aConst.isFloat()) {
        value = aConst.floatValue();
      } else if (aConst.isDouble()) {
        value = aConst.doubleValue();
      } else if (aConst.isInt()) {
        value = aConst.intValue();
      } else if (aConst.isLong()) {
        value = aConst.longValue();
      } else if (aConst.isBigDecimal()) {
        value = aConst.decimalValue();
      }
    }
    return value;
  }
}

