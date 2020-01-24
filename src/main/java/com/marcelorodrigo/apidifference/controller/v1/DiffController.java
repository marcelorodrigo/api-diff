package com.marcelorodrigo.apidifference.controller.v1;

import com.marcelorodrigo.apidifference.exception.DiffException;
import com.marcelorodrigo.apidifference.exception.DiffNotFoundException;
import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;
import com.marcelorodrigo.apidifference.model.Diff;
import com.marcelorodrigo.apidifference.model.ResultType;
import com.marcelorodrigo.apidifference.service.DiffService;
import com.marcelorodrigo.apidifference.vo.Base64VO;
import com.marcelorodrigo.apidifference.vo.DiffResult;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/diff")
@Api(value = "Difference Controller")
public class DiffController {

    private DiffService diffService;

    public DiffController(DiffService diffService) {
        this.diffService = diffService;
    }

    @ApiOperation(code = 201, value = "Post LEFT side")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid base64 data provided")
    })
    @PostMapping(value = "{id}/left", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> leftPost(
            @ApiParam(value = "ID", required = true) @PathVariable String id,
            @ApiParam(value = "JSON Base64", required = true) @RequestBody Base64VO base64) {
        try {
            Diff diff = diffService.addLeft(id, base64.getData());
            return ResponseEntity.created(getCreatedURI(diff.getId())).build();
        } catch (InvalidBase64Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation("Post RIGHT side")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Ok"),
            @ApiResponse(code = 400, message = "Invalid base64 data provided")
    })
    @PostMapping(value = "{id}/right", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> rightPost(
            @ApiParam(value = "ID", required = true) @PathVariable String id,
            @ApiParam(value = "JSON Base64", required = true) @RequestBody Base64VO base64) {
        try {
            Diff diff = diffService.addRight(id, base64.getData());
            return ResponseEntity.created(getCreatedURI(diff.getId())).build();
        } catch (InvalidBase64Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation("Get difference")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok", response = DiffResult.class),
            @ApiResponse(code = 400, message = "Invalid data to compare, check details", response = DiffResult.class),
            @ApiResponse(code = 404, message = "ID not found")
    })
    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DiffResult> getDiff(
            @ApiParam(value = "ID", required = true) @PathVariable String id) {
        try {
            return ResponseEntity.ok(diffService.getDifference(id));
        } catch (DiffNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DiffException e) {
            DiffResult result = new DiffResult()
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
