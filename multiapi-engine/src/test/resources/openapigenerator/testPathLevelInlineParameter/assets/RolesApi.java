package com.sngular.multifileplugin.pathlevelinlineparameter;

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


public interface RolesApi {

  /**
   * GET /roles/{roleId}: Get a role by id
   * @param roleId The id of the role to retrieve true
   * @return  Expected response to a valid request; (status code 200)
   */

  @Operation(
    operationId = "getRole",
    summary = "Get a role by id",
    tags = {"roles"},
    responses = {
      @ApiResponse(responseCode = "200", description = "Expected response to a valid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "default", description = "unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/roles/{roleId}",
    produces = {"application/json"}
  )

  default ResponseEntity<String> getRole(@Parameter(name = "roleId", description = "The id of the role to retrieve", required = true, schema = @Schema(description = "")) @PathVariable("roleId") String roleId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}