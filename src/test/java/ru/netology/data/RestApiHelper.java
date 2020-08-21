package ru.netology.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

@Data
public class RestApiHelper {
    private String token;
    private ArrayList status;
    private int amount = 100 + (int) (Math.random() * 5000);

    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setBasePath("/api")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    public void login() {
        given()
                .spec(requestSpec)
                .body(DataHelper.getAuthInfo())
                .when()
                .post("/auth")
                .then()
                .statusCode(200);
    }

    public String verification() {

        token =
                given()
                        .spec(requestSpec)
                        .body(DataHelper.getInfoWithVerification())
                        .when()
                        .post("/auth/verification")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("token");
        return token;
    }

    public ArrayList showCardsInfo() {
        Response balanceBeforeTransfer =
                given()
                        .spec(requestSpec)
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/cards")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        ArrayList status = balanceBeforeTransfer.path("balance");
        return status;
    }

    public void transfer() {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(DataHelper.transferMoney(amount))
                .when()
                .post("/transfer")
                .then()
                .statusCode(200);
    }

}
