package com.sngular.multifileplugin.externalfragmentpathref.model;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonDeserialize(builder = InlineObjectExampleOperationDTO.InlineObjectExampleOperationDTOBuilder.class)
public class InlineObjectExampleOperationDTO {

  @JsonProperty(value ="exampleProperty")
  private String exampleProperty;

  private InlineObjectExampleOperationDTO(InlineObjectExampleOperationDTOBuilder builder) {
    this.exampleProperty = builder.exampleProperty;

  }

  public static InlineObjectExampleOperationDTO.InlineObjectExampleOperationDTOBuilder builder() {
    return new InlineObjectExampleOperationDTO.InlineObjectExampleOperationDTOBuilder();
  }

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class InlineObjectExampleOperationDTOBuilder {

    private String exampleProperty;

    public InlineObjectExampleOperationDTO.InlineObjectExampleOperationDTOBuilder exampleProperty(String exampleProperty) {
      this.exampleProperty = exampleProperty;
      return this;
    }

    public InlineObjectExampleOperationDTO build() {
      InlineObjectExampleOperationDTO inlineObjectExampleOperationDTO = new InlineObjectExampleOperationDTO(this);
      return inlineObjectExampleOperationDTO;
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
    InlineObjectExampleOperationDTO inlineObjectExampleOperationDTO = (InlineObjectExampleOperationDTO) o;
    return Objects.equals(this.exampleProperty, inlineObjectExampleOperationDTO.exampleProperty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(exampleProperty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("InlineObjectExampleOperationDTO{");
    sb.append(" exampleProperty:").append(exampleProperty);
    sb.append("}");
    return sb.toString();
  }


}
