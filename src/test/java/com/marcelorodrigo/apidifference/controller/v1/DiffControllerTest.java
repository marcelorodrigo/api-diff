package com.marcelorodrigo.apidifference.controller.v1;

import com.marcelorodrigo.apidifference.model.ResultType;
import com.marcelorodrigo.apidifference.vo.Base64VO;
import com.marcelorodrigo.apidifference.vo.DiffResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiffControllerTest {
    static final String BASE_URL = "/v1/diff";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void leftPost() {
        var response = restTemplate
                .postForEntity(BASE_URL + "/100/left", new Base64VO("Yg=="), Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    void leftPostInvalidBase64() {
        var response = restTemplate
                .postForEntity(BASE_URL + "/11/left", new Base64VO("some-invalid-data"), Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void rightPost() {
        var response = restTemplate
                .postForEntity(BASE_URL + "/101/right", new Base64VO("Yw=="), Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    void rightPostInvalidBase64() {
        var response = restTemplate
                .postForEntity(BASE_URL + "/11/right", new Base64VO("some-invalid-data-right"), Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void getDiffNotFound() {
        var diffResponse = restTemplate
                .getForEntity(BASE_URL + "/non-existing-id", DiffResult.class);

        assertThat(diffResponse.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void getDiffInvalidData() {
        // Creating only right data
        restTemplate.postForEntity(BASE_URL + "/103/right", new Base64VO("ZA=="), Void.class);

        ResponseEntity<DiffResult> diffResponse = restTemplate
                .getForEntity(BASE_URL + "/103", DiffResult.class);

        assertThat(diffResponse.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(diffResponse.getBody(), notNullValue());
        assertThat(diffResponse.getBody().getResultType(), equalTo(ResultType.INVALID_DATA));
    }

    @Test
    void getDiff() {
        var diffExpected = "{offset=1, length=1}, {offset=3, length=2}";

        // Creating left data
        restTemplate.postForEntity(BASE_URL + "/104/left", new Base64VO("TUFyQ0Vsbw=="), Void.class);

        // Creating right data
        restTemplate.postForEntity(BASE_URL + "/104/right", new Base64VO("TWFyY2Vsbw=="), Void.class);

        // Getting the difference
        final var diffResponse = restTemplate
                .getForEntity(BASE_URL + "/104", DiffResult.class);

        // Assert
        assertThat(diffResponse.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(diffResponse.getBody(), notNullValue());
        assertThat(diffResponse.getBody().getResultType(), equalTo(ResultType.SAME_LENGTH));
        assertThat(diffResponse.getBody().getMessage(), equalTo(diffExpected));
    }

}