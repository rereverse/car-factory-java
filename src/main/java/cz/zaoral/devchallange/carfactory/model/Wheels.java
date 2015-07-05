package cz.zaoral.devchallange.carfactory.model;

import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

import static cz.zaoral.devchallange.carfactory.util.Ensuring.ensuring;
import static java.util.Collections.unmodifiableList;

@ToString
public class Wheels {
    private static final Integer NUMBER_OF_WHEELS_PER_CAR = 4;
    @Getter
    private final List<Wheel> wheels;

    private Wheels(List<Wheel> wheels) {
        this.wheels = unmodifiableList(ensuring(wheels, w -> w.size() == NUMBER_OF_WHEELS_PER_CAR));
    }

    public static class Builder {
        private final List<Wheel> wheels = new LinkedList<>();

        public Builder add(Wheel wheel) {
            wheels.add(wheel);
            return this;
        }

        public Wheels build() {
            return new Wheels(wheels);
        }
    }
}
