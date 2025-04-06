package edu.neu.csye6225.webapp;
import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HealthCheckApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @BeforeAll
    static void loadDotenv
            () {
        Dotenv dotenv = Dotenv.configure().load();
        // Setting environment variables
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()) );
    }

    // Success Scenarios
    @Test
    @DisplayName("GET /healthz - Success")
    public void testHealthCheckSuccess() {
        given()
                .when()
                .get("/healthz")
                .then()
                .assertThat()
                .statusCode(200)
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("X-Content-Type-Options", "nosniff")
                .body(isEmptyString());
    }

    // Failure Scenarios
    @Test
    @DisplayName("GET /healthz with Query Parameters - Should Fail")
    public void testHealthCheckWithQueryParams() {
        given()
                .queryParam("test", "value")
                .when()
                .get("/healthz")
                .then()
                .assertThat()
                .statusCode(400)
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("X-Content-Type-Options", "nosniff");
    }

    @Test
    @DisplayName("GET /healthz with Request Body - Should Fail")
    public void testHealthCheckWithRequestBody() {
        given()
                .body("{\"test\": \"value\"}")
                .when()
                .get("/healthz")
                .then()
                .assertThat()
                .statusCode(400)
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("X-Content-Type-Options", "nosniff");
    }

    @Test
    @DisplayName("Test All Unsupported HTTP Methods")
    public void testUnsupportedMethods() {
        Method[] methods = {Method.POST, Method.PUT, Method.DELETE, Method.PATCH, Method.HEAD, Method.OPTIONS};

        for (Method method : methods) {
            given()
                    .when()
                    .request(method, "/healthz")
                    .then()
                    .assertThat()
                    .statusCode(405)
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("X-Content-Type-Options", "nosniff");
        }
    }


    @Test
    @DisplayName("GET /healthz with Invalid Path - Should Fail")
    public void testHealthCheckWithInvalidPath() {
        given()
                .when()
                .get("/invalid-path")
                .then()
                .assertThat()
                .statusCode(404)
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("X-Content-Type-Options", "nosniff");
    }
}