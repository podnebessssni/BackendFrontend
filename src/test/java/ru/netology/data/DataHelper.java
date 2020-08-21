package ru.netology.data;

import lombok.Value;


public class DataHelper {
    private DataHelper() {
    }


    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationInfo {
        private String login;
        private String code;
    }

    public static VerificationInfo getInfoWithVerification(){
        return new VerificationInfo("vasya", SqlData.getVerificationCode());
    }

    @Value
    public static class Card {
        private String id;
        private String number;
        private String balance;
    }

    @Value
    public static class Transfer {
        private String from;
        private String to;
        private int amount ;
    }

    public static Transfer transferMoney(int amount){
        return new Transfer(SqlData.getFirstCardNumber(), SqlData.getSecondCardNumber(), amount);
    }

    public static int cardBalanceAfterGetMoney(int balance, int amount ) {
        int total = balance + amount;
        return total;
    }

    public static int cardBalanceAfterSendMoney(int balance, int amount) {
        int total = balance - amount;
        return total;
    }
}
