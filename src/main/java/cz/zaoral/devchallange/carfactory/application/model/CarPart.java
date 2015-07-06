package cz.zaoral.devchallange.carfactory.application.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public abstract class CarPart extends Product {
    private final Boolean faulty;

    protected CarPart(Long serialNumber, Boolean faulty) {
        super(serialNumber);
        this.faulty = faulty;
    }
}
