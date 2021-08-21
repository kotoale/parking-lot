package kotoale.parking.lot.service;

import kotoale.parking.lot.model.Receipt;
import kotoale.parking.lot.calculator.ChargeCalculator;
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

@Service
public class ParkingLot {

    private boolean initialized;

    private final Map<Integer, String> slotNoToPlate = new TreeMap<>();
    private final Map<String, Integer> plateToSlotNo = new HashMap<>();
    private final NavigableSet<Integer> vacantSlots = new TreeSet<>();

    private final ChargeCalculator chargeCalculator;

    public ParkingLot(ChargeCalculator chargeCalculator) {
        this.chargeCalculator = Objects.requireNonNull(chargeCalculator, "chargeCalculator is null");
    }

    public void init(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(String.format("size is not positive: %d", size));
        }
        if (initialized) {
            throw new IllegalStateException("Parking lot already initialized");
        }

        vacantSlots.addAll(IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()));
        initialized = true;
    }

    public int park(String plate) {
        checkInitialize();
        Objects.requireNonNull(plate, "plate is null");
        if (plateToSlotNo.containsKey(plate)) {
            throw new IllegalArgumentException(String.format("Vehicle with the plate already parked, plate: %s", plate));
        }

        Integer slotNo = vacantSlots.pollFirst();
        if (slotNo == null) {
            throw new IllegalStateException("Sorry, parking lot is full");
        }

        slotNoToPlate.put(slotNo, plate);
        plateToSlotNo.put(plate, slotNo);
        return slotNo;
    }

    public Receipt leave(String plate, int hours) {
        checkInitialize();
        Objects.requireNonNull(plate, "plate is null");
        if (hours <= 0) {
            throw new IllegalArgumentException(String.format("hours is not positive: %d", hours));
        }
        Integer slotNo = plateToSlotNo.remove(plate);
        if (slotNo == null) {
            throw new IllegalArgumentException(String.format("Registration Number %s not found", plate));
        }

        slotNoToPlate.remove(slotNo);
        vacantSlots.add(slotNo);
        int charge = chargeCalculator.calculate(hours);
        return new Receipt(slotNo, charge);
    }

    public Map<Integer, String> status() {
        checkInitialize();
        return Collections.unmodifiableMap(slotNoToPlate);
    }

    private void checkInitialize() {
        if (!initialized) {
            throw new IllegalStateException("Parking lot not created");
        }
    }

}
