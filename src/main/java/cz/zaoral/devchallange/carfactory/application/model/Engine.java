package cz.zaoral.devchallange.carfactory.application.model;

import lombok.ToString;

@ToString(callSuper = true)
public class Engine extends CarPart {
    public Engine(Long serialNumber, Boolean faulty) {
        super(serialNumber, faulty);
    }
}