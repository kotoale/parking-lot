package kotoale.parking.lot.processors;

import java.util.Objects;

public abstract class AbstractProcessor<C> implements Processor<C> {

    private final String commandName;
    private final String argsRegexp;

    public AbstractProcessor(String commandName, String argsRegexp) {
        this.commandName = Objects.requireNonNull(commandName, "commandName");
        this.argsRegexp = Objects.requireNonNull(argsRegexp, "argsRegexp");
    }

    @Override
    final public String getCommandName() {
        return commandName;
    }

    @Override
    final public String getCommandRegexp() {
        return String.format("(%s%s)", commandName, createProcessorArgsRegexp());
    }

    private String createProcessorArgsRegexp() {
        return argsRegexp.isEmpty() ? "" : String.format("(?:\\s+)%s", argsRegexp);
    }
}
