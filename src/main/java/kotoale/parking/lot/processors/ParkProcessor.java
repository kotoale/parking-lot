package kotoale.parking.lot.processors;

import kotoale.parking.lot.ParkingLot;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class ParkProcessor implements Processor<ParkProcessor.Command> {
    private static final String COMMAND = "park";
    private static final String COMMAND_ARG_REGEXP = "(?<parkPlate>[a-zA-Z\\-\\d]+)";

    private final ParkingLot parkingLot;

    public ParkProcessor(ParkingLot parkingLot) {
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
        return new Command(matcher.group("parkPlate"));
    }

    @Override
    public String processCommand(Command command) {
        int slotNo = parkingLot.park(command.plate);
        return "Allocated slot number: " + slotNo;
    }

    static class Command {

        private final String plate;

        public Command(String plate) {
            this.plate = plate;
        }
    }
}
