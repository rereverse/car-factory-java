package cz.zaoral.devchallange.carfactory.model;

import lombok.ToString;

@ToString(callSuper = true)
public class Wheel extends CarPart {
    public Wheel(Long serialNumber, Boolean faulty) {
        super(serialNumber, faulty);
    }
}
