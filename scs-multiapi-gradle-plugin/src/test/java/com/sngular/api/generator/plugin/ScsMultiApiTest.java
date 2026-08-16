/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.sngular.api.generator.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sngular.api.generator.plugin.asyncapi.parameter.OperationParameterObject;
import com.sngular.api.generator.plugin.common.model.TypeConstants;
import com.sngular.api.generator.plugin.model.OpenApiSpecFile;
import com.sngular.api.generator.plugin.model.OperationParameter;
import com.sngular.api.generator.plugin.openapi.parameter.SpecFile;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

class ScsMultiApiTest {

  @Test
  void greeterPluginAddsOpenApiTaskToProject() {
    Project project = ProjectBuilder.builder().build();
    project.getPluginManager().apply("com.sngular.scs-multiapi-gradle-plugin");

    assertTrue(project.getTasks().getByName("openApiTask") instanceof OpenApiTask);
  }

  @Test
  void greeterPluginAddsAsyncApiTaskTaskToProject() {
    Project project = ProjectBuilder.builder().build();
    project.getPluginManager().apply("com.sngular.scs-multiapi-gradle-plugin");

    assertTrue(project.getTasks().getByName("asyncApiTask") instanceof AsyncApiTask);
  }

  @Test
  void openApiTaskMapsUseTimeType() {
    final OpenApiSpecFile specFile = new OpenApiSpecFile();
    specFile.setFilePath("api.yml");
    specFile.setUseTimeType(TypeConstants.TimeType.ZONED);

    final SpecFile result = (SpecFile) OpenApiTask.toFileSpec(specFile);

    assertEquals(TypeConstants.TimeType.ZONED, result.getUseTimeType());
  }

  @Test
  void openApiTaskDefaultsUseTimeTypeToLocal() {
    final OpenApiSpecFile specFile = new OpenApiSpecFile();
    specFile.setFilePath("api.yml");

    final SpecFile result = (SpecFile) OpenApiTask.toFileSpec(specFile);

    assertEquals(TypeConstants.TimeType.LOCAL, result.getUseTimeType());
  }

  @Test
  void asyncApiTaskMapsUseTimeType() {
    final OperationParameter parameterObject = new OperationParameter();
    parameterObject.setUseTimeType(TypeConstants.TimeType.ZONED);

    final OperationParameterObject result = AsyncApiTask.toOperationParameterObject(parameterObject);

    assertEquals(TypeConstants.TimeType.ZONED, result.getUseTimeType());
  }

  @Test
  void asyncApiTaskDefaultsUseTimeTypeToLocal() {
    final OperationParameter parameterObject = new OperationParameter();

    final OperationParameterObject result = AsyncApiTask.toOperationParameterObject(parameterObject);

    assertEquals(TypeConstants.TimeType.LOCAL, result.getUseTimeType());
  }

  @Test
  void asyncApiTaskMapsCommonSpecFileFields() {
    final OperationParameter parameterObject = new OperationParameter();
    parameterObject.setApiPackage("com.example.api");
    parameterObject.setModelPackage("com.example.model");
    parameterObject.setModelNamePrefix("Prefix");
    parameterObject.setModelNameSuffix("DTO");
    parameterObject.setClassNamePostfix("Consumer");
    parameterObject.setDateFormat("dd/MM/yyyy");
    parameterObject.setDateTimeFormat("dd/MM/yyyy HH:mm");
    parameterObject.setUseLombokModelAnnotation(true);

    final OperationParameterObject result = AsyncApiTask.toOperationParameterObject(parameterObject);

    assertEquals("com.example.api", result.getApiPackage());
    assertEquals("com.example.model", result.getModelPackage());
    assertEquals("Prefix", result.getModelNamePrefix());
    assertEquals("DTO", result.getModelNameSuffix());
    assertEquals("Consumer", result.getClassNamePostfix());
    assertEquals("dd/MM/yyyy", result.getDateFormat());
    assertEquals("dd/MM/yyyy HH:mm", result.getDateTimeFormat());
    assertTrue(result.isUseLombokModelAnnotation());
  }

}
