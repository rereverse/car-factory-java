package cz.zaoral.devchallange.carfactory.application.model;

public class Coachwork extends CarPart {
    public Coachwork(Long serialNumber, Boolean faulty) {
        super(serialNumber, faulty);
    }

    @Override
    public String toString() {
        return "Coachwork{sn=" + getSerialNumber() + ", defective=" + defective() + '}';
    }
}
