package kotoale.parking.lot.processors;

import kotoale.parking.lot.ParkingLot;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class CreateProcessor implements Processor<CreateProcessor.Command> {
    private static final String COMMAND = "create";
    private static final String COMMAND_ARG_REGEXP = "(?<createSize>[1-9]\\d*)";

    private final ParkingLot parkingLot;

    public CreateProcessor(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public String getCommandName() {
        return COMMAND;
    }

    @Override
    public String getArgsRegexp() {
        return COMMAND_ARG_REGEXP;
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
