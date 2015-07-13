package cz.zaoral.devchallange.carfactory.application.model;

public class Engine extends CarPart {
    public Engine(Long serialNumber, Boolean faulty) {
        super(serialNumber, faulty);
    }

    @Override
    public String toString() {
        return "Engine{sn=" + getSerialNumber() + ", defective=" + defective() + '}';
    }
}
