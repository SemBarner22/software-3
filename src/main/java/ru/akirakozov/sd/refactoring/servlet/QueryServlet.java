package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.akirakozov.sd.refactoring.utils.ProductUtils.marketRepository;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (command.intern()) {
            case "max":
                marketRepository.maxPriceProduct(response);
                break;
            case "min":
                marketRepository.minPriceProduct(response);
                break;
            case "sum":
                marketRepository.totalPriceOfProducts(response);
                break;
            case "count":
                marketRepository.numberOfProducts(response);
                break;
            default:
                response.getWriter().println("Unknown command: " + command);
        }
    }

}
