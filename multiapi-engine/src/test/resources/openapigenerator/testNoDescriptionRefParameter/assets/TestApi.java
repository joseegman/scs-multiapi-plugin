package com.sngular.multifileplugin.nodescriptionrefparameter;

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


public interface TestApi {

  /**
   * GET /test/{testId}: test
   * @param testId true
   * @return  CustomerPositionStateResponse; (status code 200)
   */

  @Operation(
    operationId = "Retrieve",
    summary = "test",
    tags = {"Test"},
    responses = {
      @ApiResponse(responseCode = "200", description = "CustomerPositionStateResponse", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/test/{testId}",
    produces = {"application/json"}
  )

  default ResponseEntity<String> Retrieve(@Parameter(name = "testId", required = true, schema = @Schema(description = "")) @PathVariable("testId") String testId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}