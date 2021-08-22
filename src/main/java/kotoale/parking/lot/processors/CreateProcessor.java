package kotoale.parking.lot.processors;

import kotoale.parking.lot.service.ParkingLot;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

/**
 * Creates parking lot with given size.
 */
@Component
public class CreateProcessor extends AbstractProcessor<CreateProcessor.Command> {

    private final ParkingLot parkingLot;

    public CreateProcessor(ParkingLot parkingLot) {
        super("create", "(?<createSize>[1-9]\\d*)");
        this.parkingLot = parkingLot;
    }

    @Override
    public Command createCommand(Matcher matcher) {
        String size = matcher.group("createSize");
        return new Command(Integer.parseInt(size));
    }

    @Override
    public String processCommand(Command command) {
        int size = command.size;
        parkingLot.init(size);
        return String.format("Created parking lot with %d slots", size);
    }

    static class Command {
        private final int size;

        public Command(int size) {
            this.size = size;
        }
    }
}
