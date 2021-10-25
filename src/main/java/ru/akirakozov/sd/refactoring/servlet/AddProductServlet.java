package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.MarketRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractServlet {

    public AddProductServlet(MarketRepository marketRepository) {
        super(marketRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Product product = new Product(
            request.getParameter("name"),
            Long.parseLong(request.getParameter("price"))
        );

        marketRepository.addProduct(product, response);
        response.getWriter().println("OK");
    }
}
