package cz.zaoral.devchallange.carfactory.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Product {
    private final Long serialNumber;

    protected Product(Long serialNumber) {
        this.serialNumber = serialNumber;
    }
}
