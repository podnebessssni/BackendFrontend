package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.RestApiHelper;
import ru.netology.data.SqlData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
    private RestApiHelper helper = new RestApiHelper();

    @Test
    void shouldLoginAndTransferMoney() {
        helper.login();
        val token = helper.verification();
        assertThat(token, equalTo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbiI6InZhc3lhIn0.JmhHh8NXwfqktXSFbzkPohUb90gnc3yZ9tiXa0uUpRY"));
        val status = helper.showCardsInfo();
        assertEquals(status.get(0), SqlData.getSecondCardBalance());
        assertEquals(status.get(1), SqlData.getFirstCardBalance());
        helper.transfer();
        assertEquals(SqlData.getSecondCardBalance(), DataHelper.cardBalanceAfterGetMoney((Integer) status.get(0), helper.getAmount()));
        assertEquals(SqlData.getFirstCardBalance(), DataHelper.cardBalanceAfterSendMoney((Integer) status.get(1), helper.getAmount()));
    }
}
