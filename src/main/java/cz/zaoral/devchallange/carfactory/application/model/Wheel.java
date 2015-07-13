package cz.zaoral.devchallange.carfactory.application.model;

public class Wheel extends CarPart {
    public Wheel(Long serialNumber, Boolean faulty) {
        super(serialNumber, faulty);
    }

    @Override
    public String toString() {
        return "Wheel{sn=" + getSerialNumber() + ", defective=" + defective() + '}';
    }
}
