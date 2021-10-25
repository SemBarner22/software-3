package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.repository.MarketRepository;

import javax.servlet.http.HttpServlet;

public class AbstractServlet extends HttpServlet {

    final MarketRepository marketRepository;

    public AbstractServlet(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }
}
