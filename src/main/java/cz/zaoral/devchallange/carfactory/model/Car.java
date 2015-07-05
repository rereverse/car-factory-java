package cz.zaoral.devchallange.carfactory.model;

import lombok.Getter;
import lombok.ToString;

import static cz.zaoral.devchallange.carfactory.model.CarColour.NOT_PAINTED;
import static cz.zaoral.devchallange.carfactory.util.Ensuring.ensuringNotNull;

@ToString(callSuper = true)
public class Car extends Product {
    private final Engine engine;
    private final Coachwork coachwork;
    private final Wheels wheels;
    private final CarColour colour;

    protected Car(Long serialNumber, Engine engine, Coachwork coachwork, Wheels wheels) {
        this(serialNumber, engine, coachwork, wheels, NOT_PAINTED);
    }

    private Car(Long serialNumber, Engine engine, Coachwork coachwork, Wheels wheels, CarColour colour) {
        super(ensuringNotNull(serialNumber));
        this.engine = ensuringNotNull(engine);
        this.coachwork = ensuringNotNull(coachwork);
        this.wheels = ensuringNotNull(wheels);
        this.colour = colour;
    }

    public Car paint(CarColour colour) {
        return new Car(getSerialNumber(), engine, coachwork, wheels, colour);
    }

    @Getter
    public static class Builder {
        private final Long serialNumber;
        private Engine engine;
        private Coachwork coachwork;
        private Wheels wheels;
        private CarColour colour = NOT_PAINTED;

        public Builder(Long serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Car build() {
            return new Car(serialNumber, engine, coachwork, wheels, colour);
        }

        public Builder setEngine(Engine engine) {
            this.engine = engine;
            return this;
        }

        public Builder setCoachwork(Coachwork coachwork) {
            this.coachwork = coachwork;
            return this;
        }

        public Builder setWheels(Wheels wheels) {
            this.wheels = wheels;
            return this;
        }

        public Builder setColour(CarColour colour) {
            this.colour = colour;
            return this;
        }
    }
}
