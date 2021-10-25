package ru.akirakozov.sd.refactoring.utils;

import ru.akirakozov.sd.refactoring.domain.Product;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HTMLUtils {
    public static void setTextTypeWithOKResponse(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public static void createHtmlBody(HttpServletResponse response, List<String> toWrite) throws IOException {
        response.getWriter().println("<html><body>");
        toWrite.forEach(s -> {
            try {
                response.getWriter().println(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        response.getWriter().println("</body></html>");
    }

    public static String parseIntCount(int price) {
        return "Number of products: " + System.lineSeparator() + price;
    }

    public static String parseIntSum(int price) {
        return "Summary price: " + System.lineSeparator() + price;
    }

    public static String parseProduct(Product product) {
        return parseProductAbstract(product, "");
    }

    public static String parseProductMax(Product product) {
        return parseProductAbstract(product, "<h1>Product with max price: </h1>");
    }

    public static String parseProductMin(Product product) {
        return parseProductAbstract(product, "<h1>Product with min price: </h1>");
    }

    private static String parseProductAbstract(Product product, String header) {
        if (product == null) {
            return header;
        }
        return header + System.lineSeparator() + product.getName() + "\t" + product.getPrice() + "</br>";
    }

}
