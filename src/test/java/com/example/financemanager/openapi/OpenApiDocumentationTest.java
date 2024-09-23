package com.example.financemanager.openapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OpenApiDocumentationTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void testOpenApiSpec() {
        var url = "/v3/api-docs.yaml";
        var response = testRestTemplate.getForObject(url, String.class);
        try (var writer = new FileWriter("src/main/resources/openapi/finance-manager-specification.yaml")) {
            writer.write(response);
            writer.flush();
        } catch (IOException e) {
            assert false;
        }
        assertNotNull(response);
    }

}
