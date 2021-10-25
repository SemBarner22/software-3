package ru.akirakozov.sd.refactoring.domain;

public class Product {

    private String name;

    private long price;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
