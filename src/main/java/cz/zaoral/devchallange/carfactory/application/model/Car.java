package cz.zaoral.devchallange.carfactory.application.model;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

import static cz.zaoral.devchallange.carfactory.application.model.CarColour.NOT_PAINTED;
import static cz.zaoral.devchallange.carfactory.util.Utils.ensuring;
import static cz.zaoral.devchallange.carfactory.util.Utils.ensuringNotNull;
import static java.util.Collections.unmodifiableList;

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

    @Override
    public String toString() {
        return "Car{" + "sn=" + getSerialNumber() + ", engine=" + engine + ", coachwork=" + coachwork + ", wheels=" +
                wheels + ", colour=" + colour + '}';
    }

    @Getter
    public static class CarBeingBuilt {
        private final Long serialNumber;
        private Engine engine;
        private Coachwork coachwork;
        private List<Wheel> wheels = new LinkedList<>();

        public CarBeingBuilt(Long serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Car build() {
            return new Car(serialNumber, engine, coachwork, wheels, NOT_PAINTED);
        }

        public CarBeingBuilt addEngine(Engine engine) {
            this.engine = engine;
            return this;
        }

        public CarBeingBuilt addCoachwork(Coachwork coachwork) {
            this.coachwork = coachwork;
            return this;
        }

        public CarBeingBuilt addWheel(Wheel wheel) {
            this.wheels.add(wheel);
            return this;
        }
    }
}
