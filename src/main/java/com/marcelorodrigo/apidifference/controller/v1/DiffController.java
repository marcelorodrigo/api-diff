package com.marcelorodrigo.apidifference.controller.v1;

import com.marcelorodrigo.apidifference.exception.DiffException;
import com.marcelorodrigo.apidifference.exception.DiffNotFoundException;
import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;
import com.marcelorodrigo.apidifference.model.Diff;
import com.marcelorodrigo.apidifference.model.DiffResult;
import com.marcelorodrigo.apidifference.model.ResultType;
import com.marcelorodrigo.apidifference.service.DiffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/diff")
@Api(value = "Difference Controller", description = "Difference Controller")
public class DiffController {

    private DiffService diffService;

    public DiffController(DiffService diffService) {
        this.diffService = diffService;
    }

    @ApiOperation("Post LEFT side")
    @ApiResponses({
            @ApiResponse(code = 201, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid base64 data")
    })
    @PostMapping(value = "{id}/left")
    public ResponseEntity<Void> leftPost(@PathVariable String id, @RequestBody String data) {
        try {
            Diff diff = diffService.addLeft(id, data);
            return ResponseEntity.created(getCreatedURI(diff.getId())).build();
        } catch (InvalidBase64Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation("Post RIGHT side")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Ok"),
            @ApiResponse(code = 400, message = "Invalid base64 data")
    })
    @PostMapping(value = "{id}/right")
    public ResponseEntity<Void> rightPost(@PathVariable String id, @RequestBody String data) {
        try {
            Diff diff = diffService.addRight(id, data);
            return ResponseEntity.created(getCreatedURI(diff.getId())).build();
        } catch (InvalidBase64Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation("Get difference")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok", response = DiffResult.class),
            @ApiResponse(code = 400, message = "Invalid data to compare, check details"),
            @ApiResponse(code = 404, message = "ID not found")
    })
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DiffResult> getDiff(@PathVariable String id) {
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
