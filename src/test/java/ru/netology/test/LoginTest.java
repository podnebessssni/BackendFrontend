package ru.netology.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SqlData;

import java.sql.SQLException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setBasePath("/api")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    void shouldLoginAndTransferMoney() throws SQLException {
        int amount = 100 + (int) (Math.random() * 5000);
        given()
                .spec(requestSpec)
                .body(DataHelper.getAuthInfo())
                .when()
                .post("/auth")
                .then()
                .statusCode(200);

        String token =
                given()
                        .spec(requestSpec)
                        .body(DataHelper.getInfoWithVerification())
                        .when()
                        .post("/auth/verification")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("token");

        assertThat(token, equalTo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbiI6InZhc3lhIn0.JmhHh8NXwfqktXSFbzkPohUb90gnc3yZ9tiXa0uUpRY"));

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

        assertEquals(status.get(0), SqlData.getSecondCardBalance());
        assertEquals(status.get(1), SqlData.getFirstCardBalance());

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(DataHelper.transferMoney(amount))
                .when()
                .post("/transfer")
                .then()
                .statusCode(200);

        assertEquals(SqlData.getSecondCardBalance(), DataHelper.cardBalanceAfterGetMoney((Integer) status.get(0), amount));
        assertEquals(SqlData.getFirstCardBalance(), DataHelper.cardBalanceAfterSendMoney((Integer) status.get(1), amount));
    }
}
