package ru.akirakozov.sd.refactoring.utils;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.MarketRepository;
import ru.akirakozov.sd.refactoring.repository.MarketRepositoryImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static ru.akirakozov.sd.refactoring.utils.HTMLUtils.createHtmlBody;

public class ProductUtils {

    public static MarketRepository marketRepository = new MarketRepositoryImpl();

    public static BiFunction<ResultSet, Function<Product, String>, String> functionProduct = (rs, htmlParser) -> {
        if (rs == null) {
            return htmlParser.apply(null);
        }
        try {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            Product product = new Product(name, price);
            return htmlParser.apply(product);
        } catch (SQLException throwables) {
            return htmlParser.apply(null);
        }
    };

    public static BiFunction<ResultSet, Function<Integer, String>, String> functionInt = (rs, htmlParser) -> {
        if (rs == null) {
            return htmlParser.apply(null);
        }
        try {
            int price = rs.getInt(1);
            return htmlParser.apply(price);
        } catch (SQLException throwables) {
            return htmlParser.apply(null);
        }
    };


    public static void dbAbstractUpdateQuery(
        String query
    ) {
        dbAbstractQuery(statement -> {
            try {
                statement.executeUpdate(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public static <T> void dbAbstractResponseQuery(
        HttpServletResponse response,
        String query,
        BiFunction<ResultSet, Function<T, String>, String> parseResultSet,
        Function<T, String> htmlParser
    ) {
        dbAbstractQuery(statement -> {
            try {
                ResultSet rs = statement.executeQuery(query);
                List<String> toWrite = new ArrayList<>();
                boolean rsHasNext = false;
                while (rs.next()) {
                    rsHasNext = true;
                    toWrite.add(parseResultSet.apply(rs, htmlParser));
                }
                if (!rsHasNext) {
                    toWrite.add(parseResultSet.apply(null, htmlParser));
                }
                createHtmlBody(response, toWrite);
                rs.close();
                HTMLUtils.setTextTypeWithOKResponse(response);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void dbAbstractQuery(Consumer<Statement> statementConsumer) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            statementConsumer.accept(stmt);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}


