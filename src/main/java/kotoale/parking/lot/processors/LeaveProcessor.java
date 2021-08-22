package kotoale.parking.lot.processors;

import kotoale.parking.lot.config.ProcessorProperties;
import kotoale.parking.lot.service.ParkingLot;
import kotoale.parking.lot.model.Receipt;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class LeaveProcessor extends AbstractProcessor<LeaveProcessor.Command> {

    private final ParkingLot parkingLot;

    public LeaveProcessor(ParkingLot parkingLot, ProcessorProperties props) {
        super("leave", "(?<leavePlate>" + props.getPlateRegexp() + ")(?:\\s+)(?<leaveHours>[1-9]\\d*)");
        this.parkingLot = parkingLot;
    }

    @Override
    public Command createCommand(Matcher matcher) {
        String plate = matcher.group("leavePlate");
        int hours = Integer.parseInt(matcher.group("leaveHours"));
        return new Command(plate, hours);
    }

    @Override
    public String processCommand(Command command) {
        Receipt receipt = parkingLot.leave(command.plate, command.hours);
        return String.format("Registration Number %s from Slot %d has left with Charge %d",
                command.plate, receipt.getSlotNo(), receipt.getCharge());
    }

    static class Command {
        private final String plate;
        private final int hours;

        public Command(String plate, int hours) {
            this.plate = plate;
            this.hours = hours;
        }
    }
}
