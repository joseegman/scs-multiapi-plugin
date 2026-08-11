package com.sngular.multifileplugin.testnestedrefinallof;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import com.sngular.multifileplugin.testnestedrefinallof.model.InlineResponse200ListProductsDTO;

public interface ProductsApi {

  /**
   * GET /products: List all products
   * @return  A list of products; (status code 200)
   */

  @Operation(
    operationId = "listProducts",
    summary = "List all products",
    tags = {"products"},
    responses = {
      @ApiResponse(responseCode = "200", description = "A list of products", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InlineResponse200ListProductsDTO.class)))
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/products",
    produces = {"application/json"}
  )

  default ResponseEntity<InlineResponse200ListProductsDTO> listProducts() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}