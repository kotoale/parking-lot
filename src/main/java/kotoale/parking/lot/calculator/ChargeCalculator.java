package kotoale.parking.lot.calculator;

/**
 * Interface for parking lot charge calculation.
 */
public interface ChargeCalculator {
    /**
     * @param hours number of hours
     * @return calculated charge for given {@code hours}
     */
    int calculate(int hours);
}
