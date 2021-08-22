package kotoale.parking.lot.service;

import kotoale.parking.lot.calculator.ChargeCalculator;
import kotoale.parking.lot.exception.ParkingLotException;
import kotoale.parking.lot.model.Receipt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Parking lot.
 */
@Service
public class ParkingLot {

    private boolean initialized;

    private final Map<Integer, String> slotNoToPlate = new TreeMap<>();
    private final Map<String, Integer> plateToSlotNo = new HashMap<>();
    private final NavigableSet<Integer> vacantSlots = new TreeSet<>();

    private final ChargeCalculator chargeCalculator;

    /**
     * Creates new {@link ParkingLot} object with {@code chargeCalculator}
     *
     * @param chargeCalculator charge calculator to be used by the parking lot
     */
    public ParkingLot(ChargeCalculator chargeCalculator) {
        this.chargeCalculator = Objects.requireNonNull(chargeCalculator, "chargeCalculator is null");
    }

    /**
     * Initialize parking lot with given {@code size}
     *
     * @param size parking lot size
     * @throws IllegalArgumentException when {@code size} is not positive
     * @throws ParkingLotException      when parking lot already initialized
     */
    public void init(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(String.format("size is not positive: %d", size));
        }
        if (initialized) {
            throw new ParkingLotException("Parking lot already initialized");
        }

        vacantSlots.addAll(IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()));
        initialized = true;
    }

    /**
     * Parks a car with {@code plate}
     *
     * @param plate registration number of the car to be parked
     * @return a parking slot occupied by the parked car
     * @throws ParkingLotException  when parking lot is not initialized
     * @throws ParkingLotException  when a vehicle with the {@code plate} already parked
     * @throws ParkingLotException  when parking lot is full
     * @throws NullPointerException when {@code plate} is null
     */
    public int park(String plate) {
        checkInitialize();
        Objects.requireNonNull(plate, "plate is null");
        if (plateToSlotNo.containsKey(plate)) {
            throw new ParkingLotException(String.format("Vehicle with the plate already parked, plate: %s", plate));
        }

        Integer slotNo = vacantSlots.pollFirst();
        if (slotNo == null) {
            throw new ParkingLotException("Sorry, parking lot is full");
        }

        slotNoToPlate.put(slotNo, plate);
        plateToSlotNo.put(plate, slotNo);
        return slotNo;
    }

    /**
     * Removes car with {@code plate} from the parking lot and calculate total charge
     *
     * @param plate registration number of the leaving car
     * @param hours parking time (rounded to hours) for the car with {@code plate}
     * @return {@link Receipt} for the leaving car
     * @throws IllegalArgumentException when {@code hours} is not positive
     * @throws ParkingLotException      when parking lot is not initialized
     * @throws ParkingLotException      when car with the {@code plate} not parked
     * @throws NullPointerException     when {@code plate} is null
     */
    public Receipt leave(String plate, int hours) {
        checkInitialize();
        Objects.requireNonNull(plate, "plate is null");
        if (hours <= 0) {
            throw new IllegalArgumentException(String.format("hours is not positive: %d", hours));
        }
        Integer slotNo = plateToSlotNo.remove(plate);
        if (slotNo == null) {
            throw new ParkingLotException(String.format("Registration Number %s not found", plate));
        }

        slotNoToPlate.remove(slotNo);
        vacantSlots.add(slotNo);
        int charge = chargeCalculator.calculate(hours);
        return new Receipt(slotNo, charge);
    }

    /**
     * @return unmodifiable map: occupied slot No -> car registration number
     * @throws ParkingLotException when parking lot is not initialized
     */
    public Map<Integer, String> status() {
        checkInitialize();
        return Collections.unmodifiableMap(slotNoToPlate);
    }

    private void checkInitialize() {
        if (!initialized) {
            throw new ParkingLotException("Parking lot not created");
        }
    }

}
