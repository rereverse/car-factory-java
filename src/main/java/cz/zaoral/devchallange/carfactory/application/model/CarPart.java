package cz.zaoral.devchallange.carfactory.application.model;

import lombok.ToString;

@ToString(callSuper = true)
public abstract class CarPart extends Product {
    private final Boolean faulty;

    protected CarPart(Long serialNumber, Boolean faulty) {
        super(serialNumber);
        this.faulty = faulty;
    }

    public Boolean defective() {
        return faulty;
    }
}
