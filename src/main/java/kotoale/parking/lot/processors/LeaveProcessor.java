package kotoale.parking.lot.processors;

import kotoale.parking.lot.ParkingLot;
import kotoale.parking.lot.Receipt;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class LeaveProcessor implements Processor<LeaveProcessor.Command> {
    private static final String COMMAND = "leave";
    private static final String COMMAND_ARG_REGEXP = "(?<leavePlate>[a-zA-Z\\-\\d]+)(?:\\s+)(?<leaveHours>[1-9]\\d*)";

    private final ParkingLot parkingLot;

    public LeaveProcessor(ParkingLot parkingLot) {
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
