package com.sngular.multifileplugin.externalfragmentpathref.model;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonDeserialize(builder = InlineResponse200ExampleOperationDTO.InlineResponse200ExampleOperationDTOBuilder.class)
public class InlineResponse200ExampleOperationDTO {

  @JsonProperty(value ="exampleProperty")
  private String exampleProperty;

  private InlineResponse200ExampleOperationDTO(InlineResponse200ExampleOperationDTOBuilder builder) {
    this.exampleProperty = builder.exampleProperty;

  }

  public static InlineResponse200ExampleOperationDTO.InlineResponse200ExampleOperationDTOBuilder builder() {
    return new InlineResponse200ExampleOperationDTO.InlineResponse200ExampleOperationDTOBuilder();
  }

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class InlineResponse200ExampleOperationDTOBuilder {

    private String exampleProperty;

    public InlineResponse200ExampleOperationDTO.InlineResponse200ExampleOperationDTOBuilder exampleProperty(String exampleProperty) {
      this.exampleProperty = exampleProperty;
      return this;
    }

    public InlineResponse200ExampleOperationDTO build() {
      InlineResponse200ExampleOperationDTO inlineResponse200ExampleOperationDTO = new InlineResponse200ExampleOperationDTO(this);
      return inlineResponse200ExampleOperationDTO;
    }
  }

  @Schema(name = "exampleProperty", required = false, example = "exampleValue")
  public String getExampleProperty() {
    return exampleProperty;
  }
  public void setExampleProperty(String exampleProperty) {
    this.exampleProperty = exampleProperty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse200ExampleOperationDTO inlineResponse200ExampleOperationDTO = (InlineResponse200ExampleOperationDTO) o;
    return Objects.equals(this.exampleProperty, inlineResponse200ExampleOperationDTO.exampleProperty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(exampleProperty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("InlineResponse200ExampleOperationDTO{");
    sb.append(" exampleProperty:").append(exampleProperty);
    sb.append("}");
    return sb.toString();
  }


}
