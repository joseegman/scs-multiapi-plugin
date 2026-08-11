package com.sngular.multifileplugin.testexternalschemafileref.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public class DashboardDTO {

  @JsonProperty(value ="id")
  private String id;

  @JsonProperty(value ="title")
  private String title;


  @Builder
  @Jacksonized
  private DashboardDTO(String id, String title) {
    this.id = id;
    this.title = title;

  }

}