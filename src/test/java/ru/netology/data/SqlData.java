package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.awt.*;
import java.sql.Array;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;

public class SqlData {

    public static String getVerificationCode(){
        val codeSQL = "SELECT code FROM auth_codes WHERE created = (select MAX(created) FROM auth_codes);";
        val runner = new QueryRunner();
        String code = null;

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            code = runner.query(conn, codeSQL, new ScalarHandler<>());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return code;
    }

    public static String getFirstCardNumber(){
        val selectCard = "SELECT min(number) FROM cards;";
        val runner = new QueryRunner();
        String number = null;
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            number = runner.query(conn, selectCard, new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return number;
    }

    public static String getSecondCardNumber(){
        val selectCard = "SELECT max(number) FROM cards;";
        val runner = new QueryRunner();
        String number = null;
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            number = runner.query(conn, selectCard, new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return number;
    }


    public static int getSecondCardBalance(){
        val selectCard = "SELECT balance_in_kopecks FROM cards WHERE number = '5559 0000 0000 0002';";
        val runner = new QueryRunner();
        int balance = 0;
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            balance = runner.query(conn, selectCard, new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return balance / 100;
    }

    public static int getFirstCardBalance(){
        val selectCard = "SELECT balance_in_kopecks FROM cards WHERE number = '5559 0000 0000 0001';";
        val runner = new QueryRunner();
        int balance = 0;
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            balance = runner.query(conn, selectCard, new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return balance / 100;
    }
}

