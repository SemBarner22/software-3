package ru.akirakozov.sd.refactoring.repository;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.utils.HTMLUtils;
import ru.akirakozov.sd.refactoring.utils.ProductUtils;

import javax.servlet.http.HttpServletResponse;

public class MarketRepositoryImpl implements MarketRepository {

    @Override
    public void createTables() {
        ProductUtils.dbAbstractUpdateQuery(
            "CREATE TABLE IF NOT EXISTS PRODUCT" +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " PRICE          INT     NOT NULL)"
        );
    }

    @Override
    public void addProduct(Product product, HttpServletResponse response) {
        ProductUtils.dbAbstractUpdateQuery(
            "INSERT INTO PRODUCT " +
            "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")"
        );
    }

    @Override
    public void getProducts(HttpServletResponse response) {
        ProductUtils.dbAbstractResponseQuery(
            response,
            "SELECT * FROM PRODUCT",
            ProductUtils.functionProduct,
            HTMLUtils::parseProduct
        );
    }

    @Override
    public void minPriceProduct(HttpServletResponse response) {
        ProductUtils.dbAbstractResponseQuery(
            response,
            "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
            ProductUtils.functionProduct,
            HTMLUtils::parseProductMin
        );
    }

    @Override
    public void maxPriceProduct(HttpServletResponse response) {
        ProductUtils.dbAbstractResponseQuery(
            response,
            "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
            ProductUtils.functionProduct,
            HTMLUtils::parseProductMax
        );
    }

    @Override
    public void totalPriceOfProducts(HttpServletResponse response) {
        ProductUtils.dbAbstractResponseQuery(
            response,
            "SELECT SUM(price) FROM PRODUCT",
            ProductUtils.functionInt,
            HTMLUtils::parseIntSum
        );
    }

    @Override
    public void numberOfProducts(HttpServletResponse response) {
        ProductUtils.dbAbstractResponseQuery(
            response,
            "SELECT COUNT(*) FROM PRODUCT",
            ProductUtils.functionInt,
            HTMLUtils::parseIntCount
        );
    }
}
