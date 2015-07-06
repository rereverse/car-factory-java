package cz.zaoral.devchallange.carfactory.application.model;

import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

import static cz.zaoral.devchallange.carfactory.application.model.CarColour.NOT_PAINTED;
import static cz.zaoral.devchallange.carfactory.util.Ensuring.ensuring;
import static cz.zaoral.devchallange.carfactory.util.Ensuring.ensuringNotNull;
import static java.util.Collections.unmodifiableList;

@ToString(callSuper = true)
public class Car extends Product {
    private static final Integer NUMBER_OF_WHEELS_PER_CAR = 4;
    private final Engine engine;
    private final Coachwork coachwork;
    private final List<Wheel> wheels;
    private final CarColour colour;

    protected Car(Long serialNumber, Engine engine, Coachwork coachwork, List<Wheel> wheels) {
        this(serialNumber, engine, coachwork, wheels, NOT_PAINTED);
    }

    private Car(Long serialNumber, Engine engine, Coachwork coachwork, List<Wheel> wheels, CarColour colour) {
        super(ensuringNotNull(serialNumber));
        this.engine = ensuringNotNull(engine);
        this.coachwork = ensuringNotNull(coachwork);
        this.wheels = unmodifiableList(ensuring(wheels, w -> w != null && w.size() == NUMBER_OF_WHEELS_PER_CAR));
        this.colour = ensuringNotNull(colour);
    }

    public Car paint(CarColour colour) {
        return new Car(getSerialNumber(), engine, coachwork, wheels, colour);
    }

    @Getter
    public static class Builder {
        private final Long serialNumber;
        private Engine engine;
        private Coachwork coachwork;
        private List<Wheel> wheels = new LinkedList<>();

        public Builder(Long serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Car build() {
            return new Car(serialNumber, engine, coachwork, wheels, NOT_PAINTED);
        }

        public Builder addEngine(Engine engine) {
            this.engine = engine;
            return this;
        }

        public Builder addCoachwork(Coachwork coachwork) {
            this.coachwork = coachwork;
            return this;
        }

        public Builder addWheel(Wheel wheel) {
            this.wheels.add(wheel);
            return this;
        }
    }
}
