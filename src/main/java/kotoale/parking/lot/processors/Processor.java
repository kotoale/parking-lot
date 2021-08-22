package kotoale.parking.lot.processors;

import kotoale.parking.lot.exception.ParkingLotException;

import java.util.regex.Matcher;

/**
 * This interface represents a command processor for parking lot operations.
 *
 * @param <C> command class
 */
public interface Processor<C> {
    /**
     * @return nonnull unique command name for the processor
     */
    String getCommandName();

    /**
     * @return nonnull regexp for supported command arguments
     */
    String getArgsRegexp();

    /**
     * @param matcher {@link Matcher} object used for the command creation
     * @return Command object that can be processed using {{@link #processCommand(C)}}
     */
    C createCommand(Matcher matcher);

    /**
     * @param command the command to be processed
     * @return parking lot output string after processing the {@code command}
     * @throws ParkingLotException      when a business exception occurred
     * @throws IllegalArgumentException when argument is illegal
     */
    String processCommand(C command);
}
