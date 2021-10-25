package ru.akirakozov.sd.refactoring.repository;

import ru.akirakozov.sd.refactoring.domain.Product;

import javax.servlet.http.HttpServletResponse;

public interface MarketRepository {

    void createTables();

    void addProduct(Product product, HttpServletResponse response);

    void getProducts(HttpServletResponse response);

    void minPriceProduct(HttpServletResponse response);

    void maxPriceProduct(HttpServletResponse response);

    void totalPriceOfProducts(HttpServletResponse response);

    void numberOfProducts(HttpServletResponse response);

}
