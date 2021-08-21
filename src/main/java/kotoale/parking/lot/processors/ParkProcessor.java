package kotoale.parking.lot.processors;

import kotoale.parking.lot.service.ParkingLot;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class ParkProcessor extends AbstractProcessor<ParkProcessor.Command> {
    private static final String COMMAND_NAME = "park";
    private static final String ARGS_REGEXP = "(?<parkPlate>[a-zA-Z\\-\\d]+)";

    private final ParkingLot parkingLot;

    public ParkProcessor(ParkingLot parkingLot) {
        super(COMMAND_NAME, ARGS_REGEXP);
        this.parkingLot = parkingLot;
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
