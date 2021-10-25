package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.repository.MarketRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractServlet {

    public GetProductsServlet(MarketRepository marketRepository) {
        super(marketRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        marketRepository.getProducts(response);
    }
}
