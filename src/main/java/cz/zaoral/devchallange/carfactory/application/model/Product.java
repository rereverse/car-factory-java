package cz.zaoral.devchallange.carfactory.application.model;

import lombok.Getter;

@Getter
public abstract class Product {
    private final Long serialNumber;

    protected Product(Long serialNumber) {
        this.serialNumber = serialNumber;
    }
}
