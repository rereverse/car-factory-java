package cz.zaoral.devchallange.carfactory.model;

import lombok.ToString;

@ToString(callSuper = true)
public class Coachwork extends CarPart {
    public Coachwork(Long serialNumber, Boolean faulty) {
        super(serialNumber, faulty);
    }
}
