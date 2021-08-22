package kotoale.parking.lot.calculator;

/**
 * Calculator with fixed charge for first hours.
 */
public class FirstHoursFixedPriceChargeCalculator implements ChargeCalculator {

    private final int fixedPrice;
    private final int firstHours;
    private final int hourlyRate;

    /**
     * Creates new instance of the class {@link FirstHoursFixedPriceChargeCalculator}
     *
     * @param fixedPrice fixed charge for first {@code firstHours} hours
     * @param firstHours number of hours for fixed charge
     * @param hourlyRate hourly rate for each hour after first {@code firstHours} hours
     */
    public FirstHoursFixedPriceChargeCalculator(int fixedPrice, int firstHours, int hourlyRate) {
        if (fixedPrice <= 0) {
            throw new IllegalArgumentException("fixedPrice is not positive: " + fixedPrice);
        }
        if (firstHours <= 0) {
            throw new IllegalArgumentException("firstHours is not positive: " + firstHours);
        }
        if (hourlyRate <= 0) {
            throw new IllegalArgumentException("hourlyRate is not positive: " + hourlyRate);
        }
        this.fixedPrice = fixedPrice;
        this.firstHours = firstHours;
        this.hourlyRate = hourlyRate;
    }

    /**
     * @param hours number of hours
     * @return calculated charge for given {@code hours}
     */
    @Override
    public int calculate(int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("hours is not positive: " + hours);
        }
        return fixedPrice + Math.max(0, hours - firstHours) * hourlyRate;
    }
}
