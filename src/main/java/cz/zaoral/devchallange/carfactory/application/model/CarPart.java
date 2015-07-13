package cz.zaoral.devchallange.carfactory.application.model;

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
