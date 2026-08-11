package com.sngular.multifileplugin.testnestedrefinallof.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public class InlineResponse200ListProductsDTO {

  @JsonProperty(value ="name")
  @NonNull
  private String name;

  @JsonProperty(value ="id")
  @NonNull
  private String id;

  @JsonProperty(value ="sku")
  @NonNull
  private String sku;

  @JsonProperty(value ="price")
  @NonNull
  private Double price;


  @Builder
  @Jacksonized
  private InlineResponse200ListProductsDTO(@NonNull String name, @NonNull String id, @NonNull String sku, @NonNull Double price) {
    this.name = name;
    this.id = id;
    this.sku = sku;
    this.price = price;

  }

}