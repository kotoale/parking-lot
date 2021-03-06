package kotoale.parking.lot.processors;

import kotoale.parking.lot.service.ParkingLot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Returns parking lot status with allocated slots info.
 */
@Component
public class StatusProcessor extends AbstractProcessor<Void> {

    private final ParkingLot parkingLot;

    public StatusProcessor(ParkingLot parkingLot) {
        super("status", "");
        this.parkingLot = parkingLot;
    }

    @Override
    public Void createCommand(Matcher matcher) {
        return null;
    }

    @Override
    public String processCommand(Void command) {
        Map<Integer, String> status = parkingLot.status();
        return status.isEmpty()
                ? "All slots are vacant"
                : status.entrySet().stream().map(entry -> String.format("%-8d %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n", "Slot No. Registration No.\n", ""));
    }
}
