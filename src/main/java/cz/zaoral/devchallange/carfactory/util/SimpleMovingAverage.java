package cz.zaoral.devchallange.carfactory.util;

import lombok.ToString;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;

@ToString
public class SimpleMovingAverage implements Function<Double, Optional<Double>> {
    private final Queue<Double> valuesOverPeriod;
    private final Integer period;
    private Double sumOverPeriod = 0.0;

    public SimpleMovingAverage(Integer period) {
        this.period = period;
        this.valuesOverPeriod = new LinkedList<>();
    }

    public Optional<Double> apply(Double aDouble) {
        if (isReady()) {
            sumOverPeriod -= valuesOverPeriod.poll();
        }
        sumOverPeriod += aDouble;
        valuesOverPeriod.add(aDouble);

        return isReady() ? Optional.of(sumOverPeriod / period) : Optional.<Double>empty();
    }

    private boolean isReady() {
        return valuesOverPeriod.size() == period;
    }
}
