package cz.zaoral.devchallange.carfactory.util;

import lombok.ToString;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;

@ToString
public class ExponentialMovingAverage implements Function<Double, Optional<Double>> {
    public static ExponentialMovingAverage newEma(Integer period) {
        return new ExponentialMovingAverage(period);
    }
    private final SimpleMovingAverage sma;
    private final Double multiplier;
    private Optional<Double> ema = empty();
    private boolean onlyEma = false;

    public ExponentialMovingAverage(Integer period) {
        sma = new SimpleMovingAverage(period);
        multiplier = 2.0d / (period + 1);
    }

    @Override
    public Optional<Double> apply(Double aDouble) {
        ema = ema.map(x -> (aDouble - x) * multiplier + x);
        return ema.isPresent() ? ema : computeSmaFirst(aDouble);
    }

    private Optional<Double> computeSmaFirst(Double input) {
        ema = sma.apply(input);
        return ema;
    }
}
