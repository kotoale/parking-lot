package kotoale.parking.lot.processors;

import kotoale.parking.lot.config.ProcessorProperties;
import kotoale.parking.lot.service.ParkingLot;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class ParkProcessor extends AbstractProcessor<ParkProcessor.Command> {

    private final ParkingLot parkingLot;

    public ParkProcessor(ParkingLot parkingLot, ProcessorProperties props) {
        super("park", "(?<parkPlate>" + props.getPlateRegexp() + ")");
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
