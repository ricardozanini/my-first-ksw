package org.acme;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    void testGreetRest() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"workflowdata\" : {\"name\" : \"John\", \"language\":\"English\"}}").when()
                .post("/greetings")
                .then()
                .statusCode(201)
                .body("workflowdata.greeting", is("Hello from YAML Workflow, John"));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"workflowdata\" : {\"name\" : \"Javierito\", \"language\":\"Spanish\"}}").when()
                .post("/greetings")
                .then()
                .statusCode(201)
                .body("workflowdata.greeting", is("Saludos desde YAML Workflow, Javierito"));
    }

}