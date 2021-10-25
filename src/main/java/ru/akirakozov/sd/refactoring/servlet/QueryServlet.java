package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.repository.MarketRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractServlet {

    public QueryServlet(MarketRepository marketRepository) {
        super(marketRepository);
    }

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
