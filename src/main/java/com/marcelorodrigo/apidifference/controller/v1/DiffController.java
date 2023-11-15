package com.marcelorodrigo.apidifference.controller.v1;

import com.marcelorodrigo.apidifference.exception.DiffException;
import com.marcelorodrigo.apidifference.exception.DiffNotFoundException;
import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;
import com.marcelorodrigo.apidifference.model.ResultType;
import com.marcelorodrigo.apidifference.service.DiffService;
import com.marcelorodrigo.apidifference.vo.Base64VO;
import com.marcelorodrigo.apidifference.vo.DiffResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/diff")
@Tag(name = "Difference Controller")
public class DiffController {

    private final DiffService diffService;

    public DiffController(DiffService diffService) {
        this.diffService = diffService;
    }

    @Operation(summary = "Post LEFT side", responses = {
            @ApiResponse(responseCode = "201", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid base64 data provided")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "{id}/left", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> leftPost(
            @Parameter(description = "ID", required = true) @PathVariable String id,
            @Parameter(description = "JSON Base64", required = true) @RequestBody Base64VO base64) {
        try {
            final var diff = diffService.addLeft(id, base64.getData());
            return ResponseEntity.created(getCreatedURI(diff.getId())).build();
        } catch (InvalidBase64Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Post RIGHT side", responses = {
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Invalid base64 data provided")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "{id}/right", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> rightPost(
            @Parameter(description = "ID", required = true) @PathVariable String id,
            @Parameter(description = "JSON Base64", required = true) @RequestBody Base64VO base64) {
        try {
            final var diff = diffService.addRight(id, base64.getData());
            return ResponseEntity.created(getCreatedURI(diff.getId())).build();
        } catch (InvalidBase64Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get difference", responses = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(schema = @Schema(implementation = DiffResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data to compare, check details",
                    content = @Content(schema = @Schema(implementation = DiffResult.class))),
            @ApiResponse(responseCode = "404", description = "ID not found")
    })
    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DiffResult> getDiff(
            @Parameter(description = "ID", required = true) @PathVariable String id) {
        try {
            return ResponseEntity.ok(diffService.getDifference(id));
        } catch (DiffNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DiffException e) {
            final var result = new DiffResult()
                    .setResultType(ResultType.INVALID_DATA)
                    .setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * Generate a created URI for new record
     *
     * @param id Identifier
     * @return Created URI
     */
    private URI getCreatedURI(String id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/diff/{id}").buildAndExpand(id).toUri();
    }

}
