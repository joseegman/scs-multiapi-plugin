package com.sngular.multifileplugin.testexternalschemafileref.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public class SummaryDTO {

  @JsonProperty(value ="total")
  private Integer total;

  @JsonProperty(value ="active")
  private Boolean active;


  @Builder
  @Jacksonized
  private SummaryDTO(Integer total, Boolean active) {
    this.total = total;
    this.active = active;

  }

}