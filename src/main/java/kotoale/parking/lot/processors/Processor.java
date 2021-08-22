package kotoale.parking.lot.processors;

import java.util.regex.Matcher;

public interface Processor<C> {
    String getCommandName();

    String getArgsRegexp();

    C createCommand(Matcher matcher);

    String processCommand(C command);
}
