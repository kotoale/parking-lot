package kotoale.parking.lot.calculator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FirstHoursFixedPriceChargeCalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "10, 2, 10, 1, 10",
            "10, 2, 10, 2, 10",
            "10, 2, 10, 3, 20",
            "10, 2, 10, 4, 30",
    })
    void calculate_should_return_expected_result(int fixedPrice, int firstHours, int hourlyRate, int hours, int expectedCharge) {
        var calculator = new FirstHoursFixedPriceChargeCalculator(fixedPrice, firstHours, hourlyRate);
        assertThat(calculator.calculate(hours)).isEqualTo(expectedCharge);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -10})
    void calculate_should_throw_IllegalArgumentException_when_hours_is_not_positive(int hours) {
        var calculator = new FirstHoursFixedPriceChargeCalculator(10, 1, 10);
        var exception = assertThrows(IllegalArgumentException.class, () -> calculator.calculate(hours));
        assertThat(exception.getMessage()).isEqualTo(String.format("hours is not positive: %d", hours));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 2, 10, fixedPrice is not positive: 0",
            "-1, 2, 10, fixedPrice is not positive: -1",
            "-3, 2, 10, fixedPrice is not positive: -3",
            "10, 0, 10, firstHours is not positive: 0",
            "10, -1, 10, firstHours is not positive: -1",
            "10, -3, 10, firstHours is not positive: -3",
            "10, 2, 0, hourlyRate is not positive: 0",
            "10, 2, -1, hourlyRate is not positive: -1",
            "10, 2, -3, hourlyRate is not positive: -3",
    })
    void constructor_should_throw_IllegalArgumentException(int fixedPrice, int firstHours, int hourlyRate, String message) {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new FirstHoursFixedPriceChargeCalculator(fixedPrice, firstHours, hourlyRate));
        assertThat(exception.getMessage()).isEqualTo(message);
    }

}