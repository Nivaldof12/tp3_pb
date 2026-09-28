package br.com.infnet.system;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@Tag("integration")
class PlatformSmokeIT {

    @BeforeAll
    static void configureBaseUri() {
        String base = System.getenv().getOrDefault("SMOKE_BASE_URL", "http://localhost:8080");
        RestAssured.baseURI = base;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void gatewayDeveEstarSaudavel() {
        given()
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"));
    }

    @Test
    void fluxoUsuarioFinanceiroNotificacao() {
        String email = "smoke+" + System.currentTimeMillis() + "@infnet.test";

        Number usuarioId = given()
                .contentType(ContentType.JSON)
                .body("""
                        {"nome":"Smoke Test","email":"%s","senha":"123456"}
                        """.formatted(email))
                .when()
                .post("/usuarios")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "usuario": {"id": %d},
                          "tipo": "DESPESA",
                          "categoria": "Mercado",
                          "descricao": "Smoke IT",
                          "valor": 1500.00,
                          "data": "2026-09-28"
                        }
                        """.formatted(usuarioId.longValue()))
                .when()
                .post("/financeiros")
                .then()
                .statusCode(200)
                .body("id", notNullValue());

        Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> given()
                .when()
                .get("/notificacoes/usuario/{id}", usuarioId)
                .then()
                .statusCode(200)
                .body("[0].tipo", equalTo("ALERTA")));
    }
}
