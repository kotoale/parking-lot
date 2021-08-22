package kotoale.parking.lot.service;

import kotoale.parking.lot.calculator.ChargeCalculator;
import kotoale.parking.lot.exception.ParkingLotException;
import kotoale.parking.lot.model.Receipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingLotTest {

    private static final int CHARGE = 120;

    private ParkingLot parkingLot;

    @Mock
    private ChargeCalculator calculator;

    @BeforeEach
    void setUp() {
        parkingLot = new ParkingLot(calculator);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -10})
    void init_should_throw_IllegalArgumentException_when_size_is_not_positive(int size) {
        var exception = assertThrows(IllegalArgumentException.class, () -> parkingLot.init(size));
        assertThat(exception.getMessage()).isEqualTo(String.format("size is not positive: %d", size));
        verifyNoInteractions(calculator);
    }

    @Test
    void init_should_throw_ParkingLotException_when_parking_lot_is_already_initialized() {
        parkingLot.init(1);
        var exception = assertThrows(ParkingLotException.class, () -> parkingLot.init(2));
        assertThat(exception.getMessage()).isEqualTo("Parking lot already initialized");
        verifyNoInteractions(calculator);
    }

    @Test
    void park_should_throw_NullPointerException_when_plate_is_null() {
        parkingLot.init(1);
        var exception = assertThrows(NullPointerException.class, () -> parkingLot.park(null));
        assertThat(exception.getMessage()).isEqualTo("plate is null");
        verifyNoInteractions(calculator);
    }

    @Test
    void park_should_throw_ParkingLotException_when_parking_lot_is_not_initialized() {
        var exception = assertThrows(ParkingLotException.class, () -> parkingLot.park("plate"));
        assertThat(exception.getMessage()).isEqualTo("Parking lot not created");
        verifyNoInteractions(calculator);
    }

    @Test
    void park_should_return_expected_result() {
        parkingLot.init(2);
        assertThat(parkingLot.park("plate1")).isEqualTo(1);
        assertThat(parkingLot.park("plate2")).isEqualTo(2);
        verifyNoInteractions(calculator);
    }

    @Test
    void park_should_return_nearest_slot_no() {
        parkingLot.init(4);
        assertThat(parkingLot.park("plate1")).isEqualTo(1);
        assertThat(parkingLot.park("plate2")).isEqualTo(2);
        assertThat(parkingLot.park("plate3")).isEqualTo(3);

        when(calculator.calculate(2)).thenReturn(CHARGE);
        parkingLot.leave("plate2", 2);

        assertThat(parkingLot.park("plate4")).isEqualTo(2);
        assertThat(parkingLot.park("plate5")).isEqualTo(4);
        verify(calculator).calculate(2);
    }

    @Test
    void park_again_after_leave() {
        parkingLot.init(2);
        assertThat(parkingLot.park("plate1")).isEqualTo(1);

        when(calculator.calculate(2)).thenReturn(CHARGE);
        parkingLot.leave("plate1", 2);

        assertThat(parkingLot.park("plate1")).isEqualTo(1);
        verify(calculator).calculate(2);
    }

    @Test
    void park_should_throw_ParkingLotException_when_car_is_already_parked() {
        parkingLot.init(2);
        parkingLot.park("plate");
        var exception = assertThrows(ParkingLotException.class, () -> parkingLot.park("plate"));
        assertThat(exception.getMessage()).isEqualTo(String.format("Vehicle with the plate already parked, plate: %s", "plate"));
        verifyNoInteractions(calculator);
    }

    @Test
    void park_should_throw_ParkingLotException_when_parking_lot_is_full() {
        parkingLot.init(1);
        parkingLot.park("plate1");
        var exception = assertThrows(ParkingLotException.class, () -> parkingLot.park("plate2"));
        assertThat(exception.getMessage()).isEqualTo("Sorry, parking lot is full");
        verifyNoInteractions(calculator);
    }

    @Test
    void leave_should_throw_ParkingLotException_when_parking_lot_is_not_initialized() {
        var exception = assertThrows(ParkingLotException.class, () -> parkingLot.leave("plate", 1));
        assertThat(exception.getMessage()).isEqualTo("Parking lot not created");
        verifyNoInteractions(calculator);
    }

    @Test
    void leave_should_throw_NullPointerException_when_plate_is_null() {
        parkingLot.init(1);
        var exception = assertThrows(NullPointerException.class, () -> parkingLot.leave(null, 1));
        assertThat(exception.getMessage()).isEqualTo("plate is null");
        verifyNoInteractions(calculator);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -10})
    void leave_should_throw_IllegalArgumentException_when_hours_is_not_positive(int hours) {
        parkingLot.init(1);
        var exception = assertThrows(IllegalArgumentException.class, () -> parkingLot.leave("plate", hours));
        assertThat(exception.getMessage()).isEqualTo(String.format("hours is not positive: %d", hours));
        verifyNoInteractions(calculator);
    }

    @Test
    void leave_should_throw_ParkingLotException_car_is_not_found() {
        parkingLot.init(1);
        var exception = assertThrows(ParkingLotException.class, () -> parkingLot.leave("plate", 1));
        assertThat(exception.getMessage()).isEqualTo(String.format("Registration Number %s not found", "plate"));
        verifyNoInteractions(calculator);
    }

    @Test
    void leave_should_return_expected_result() {
        parkingLot.init(3);
        parkingLot.park("plate1");
        parkingLot.park("plate2");
        parkingLot.park("plate3");

        when(calculator.calculate(3)).thenReturn(CHARGE);

        assertThat(parkingLot.leave("plate2", 3)).isEqualTo(new Receipt(2, CHARGE));
        verify(calculator).calculate(3);
    }

    @Test
    void status_should_throw_ParkingLotException_when_parking_lot_is_not_initialized() {
        var exception = assertThrows(ParkingLotException.class, () -> parkingLot.status());
        assertThat(exception.getMessage()).isEqualTo("Parking lot not created");
        verifyNoInteractions(calculator);
    }

    @Test
    void status_should_return_empty_result() {
        parkingLot.init(3);
        assertThat(parkingLot.status()).isEmpty();
        verifyNoInteractions(calculator);
    }

    @Test
    void status_should_return_expected_result() {
        parkingLot.init(4);
        parkingLot.park("plate1");
        parkingLot.park("plate2");
        parkingLot.park("plate3");
        parkingLot.park("plate4");

        when(calculator.calculate(2)).thenReturn(CHARGE);
        parkingLot.leave("plate3", 2);

        assertThat(parkingLot.status()).isEqualTo(
                Map.of(
                        1, "plate1",
                        2, "plate2",
                        4, "plate4"));
        verify(calculator).calculate(2);
    }
}