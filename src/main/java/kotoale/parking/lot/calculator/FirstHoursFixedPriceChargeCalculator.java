package kotoale.parking.lot.calculator;

public class FirstHoursFixedPriceChargeCalculator implements ChargeCalculator {

    private final int fixedPrice;
    private final int firstHours;
    private final int hourlyRate;

    public FirstHoursFixedPriceChargeCalculator(int fixedPrice, int firstHours, int hourlyRate) {
        if (fixedPrice <= 0) {
            throw new IllegalArgumentException("fixedPrice is non-positive: " + fixedPrice);
        }
        if (firstHours <= 0) {
            throw new IllegalArgumentException("fixedPrice is non-positive: " + firstHours);
        }
        if (hourlyRate <= 0) {
            throw new IllegalArgumentException("fixedPrice is non-positive: " + hourlyRate);
        }
        this.fixedPrice = fixedPrice;
        this.firstHours = firstHours;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public int calculate(int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException(String.format("hours is not positive: %d", hours));
        }
        return fixedPrice + Math.max(0, hours - firstHours) * hourlyRate;
    }
}
