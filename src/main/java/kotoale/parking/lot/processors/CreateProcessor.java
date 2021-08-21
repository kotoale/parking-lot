package kotoale.parking.lot.processors;

import kotoale.parking.lot.service.ParkingLot;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class CreateProcessor extends AbstractProcessor<CreateProcessor.Command> {
    private static final String COMMAND_NAME = "create";
    private static final String ARGS_REGEXP = "(?<createSize>[1-9]\\d*)";

    private final ParkingLot parkingLot;

    public CreateProcessor(ParkingLot parkingLot) {
        super(COMMAND_NAME, ARGS_REGEXP);
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
