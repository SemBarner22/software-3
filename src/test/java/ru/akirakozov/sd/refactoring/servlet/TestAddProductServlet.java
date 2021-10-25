package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestAddProductServlet {

    private static final String DB = "jdbc:sqlite:test.db";
    private static final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private static final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private static StringWriter stringWriter;
    private static PrintWriter printWriter;
    private static final String[] commands = {"max", "min", "sum", "count", "unknown"};

    @BeforeAll
    public static void beforeAll() {
        Mockito.when(request.getParameter("name")).thenReturn("a");
        Mockito.when(request.getParameter("price")).thenReturn("3");
        try {
            Mockito.when(response.getWriter()).thenReturn(printWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void startUpForTests() throws SQLException, IOException {
        String sqlDrop = "DROP TABLE IF EXISTS PRODUCT";
        String sqlCreate = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
        try (Connection c = DriverManager.getConnection(DB)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sqlDrop);
            stmt.executeUpdate(sqlCreate);
            stmt.close();
        }
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        Mockito.when(response.getStatus()).thenReturn(200);
    }

    private String formatAnswerEqualPrice(String option, String name, int price, int amount) {
        StringBuilder ans = new StringBuilder();
        ans.append("<html><body>").append(System.lineSeparator());
        if ("max".equals(option)) {
            ans.append("<h1>Product with max price: </h1>").append(System.lineSeparator());
            if (amount != 0) {
                ans.append("a" + "\t" + "3" + "</br>").append(System.lineSeparator());
            }
        } else if ("min".equals(option)) {
            ans.append("<h1>Product with min price: </h1>").append(System.lineSeparator());
            if (amount != 0) {
                ans.append("a" + "\t" + "3" + "</br>").append(System.lineSeparator());
            }
        } else if ("sum".equals(option)) {
            ans.append("Summary price: ").append(System.lineSeparator());
            ans.append(price * amount).append(System.lineSeparator());
        } else if ("count".equals(option)) {
            ans.append("Number of products: ").append(System.lineSeparator());
            ans.append(amount).append(System.lineSeparator());
        } else {
            return "Unknown command: " + option + System.lineSeparator();
        }
        ans.append("</body></html>").append(System.lineSeparator());
        return ans.toString();
    }

    @Test
    public void emptyProductsGet() throws IOException {
        String expected =
            "<html><body>" + System.lineSeparator() +
            "</body></html>" + System.lineSeparator();
        GetProductsServlet productsServlet = new GetProductsServlet();
        productsServlet.doGet(request, response);
        Assertions.assertEquals(expected, stringWriter.toString());
    }

    @Test
    void emptyProductsQuery() throws IOException {
        QueryServlet queryServlet = new QueryServlet();
        for (String command: commands) {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(printWriter);
            mockCommand(command, queryServlet, formatAnswerEqualPrice(command, "", 0, 0));
        }
    }

    @Test
    void getOneProduct() throws IOException {
        addProduct(1);
        String expected = "OK" + System.lineSeparator() +
            "<html><body>" + System.lineSeparator() +
            "a" + "\t" + "3" + "</br>" + System.lineSeparator() +
            "</body></html>" + System.lineSeparator();
        GetProductsServlet productsServlet = new GetProductsServlet();
        productsServlet.doGet(request, response);
        Assertions.assertEquals(expected, stringWriter.toString());
    }

    @Test
    void oneProductQuery() throws IOException {
        addProduct(1);
        QueryServlet queryServlet = new QueryServlet();
        for (String command: commands) {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(printWriter);
            mockCommand(command, queryServlet, formatAnswerEqualPrice(command, "a", 3, 1));
        }
    }

    @Test
    void getManyProductQuery() throws IOException {
        StringBuilder ans = new StringBuilder();
        addProduct(100);
        for (int i = 0; i < 100; i++) {
            ans.append("OK").append(System.lineSeparator());
        }
        ans.append("<html><body>").append(System.lineSeparator());
        for (int i = 0; i < 100; i++) {
            ans.append("a" + "\t" + "3" + "</br>" + System.lineSeparator());
        }
        ans.append("</body></html>" + System.lineSeparator());
        GetProductsServlet productsServlet = new GetProductsServlet();
        productsServlet.doGet(request, response);
        Assertions.assertEquals(ans.toString(), stringWriter.toString());
    }

    @Test
    void getManyProductQueryServlet() throws IOException {
        addProduct(100);
        QueryServlet queryServlet = new QueryServlet();
        for (String command: commands) {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(printWriter);
            mockCommand(command, queryServlet, formatAnswerEqualPrice(command, "a", 3, 100));
        }
    }

    private void addProduct(int amount) throws IOException {
        for (int i = 0; i < amount; i++) {
            AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        }
    }

    private void mockCommand(String command, QueryServlet queryServlet, String answer) throws IOException {
        Mockito.when(request.getParameter("command")).thenReturn(command);
        queryServlet.doGet(request, response);
        Assertions.assertEquals(answer, stringWriter.toString());
    }

}
