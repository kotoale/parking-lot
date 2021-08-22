package kotoale.parking.lot.service;

import kotoale.parking.lot.exception.ParkingLotException;
import kotoale.parking.lot.processors.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")
@Service
public class CommandHandler {

    private static final Logger log = LoggerFactory.getLogger(CommandHandler.class);

    private final Map<String, Processor> commandNameToProcessor;
    private final Pattern commandPattern;

    public CommandHandler(List<Processor> processors) {
        commandNameToProcessor = processors.stream().collect(Collectors.toMap(Processor::getCommandName, Function.identity()));
        commandPattern = Pattern.compile(processors.stream().map(this::getCommandRegexp)
                .collect(Collectors.joining(")|(", "^(?:\\s*)((", "))(?:\\s*)$")));
    }

    public List<Processor> getProcessors() {
        return commandNameToProcessor.values().stream()
                .sorted(Comparator.comparing(Processor::getCommandName))
                .collect(Collectors.toUnmodifiableList());
    }

    @SuppressWarnings("unchecked")
    public void handle(Stream<String> commands, PrintWriter writer) {
        commands.forEach(commandLine -> {
            Matcher matcher = commandPattern.matcher(commandLine);
            if (!matcher.matches()) {
                writer.println("Incorrect command: " + commandLine);
                log.error("Incorrect command: '{}'", commandLine);
                return;
            }

            try {
                String commandName = Arrays.stream(matcher.group().split("\\s+"))
                        .filter(Predicate.not(String::isBlank))
                        .findFirst()
                        .orElseThrow();
                Processor processor = commandNameToProcessor.get(commandName);
                var command = processor.createCommand(matcher);
                String commandOutput = processor.processCommand(command);
                writer.println(commandOutput);
                log.info("Successfully processed command: '{}'", commandLine);
            } catch (ParkingLotException exception) {
                writer.println(exception.getMessage());
                log.error("Processed command with error; command: '{}', error: {}", commandLine, exception.getMessage());
            } catch (Exception exception) {
                String message = String.format("Failed to process command: '%s', error: %s", commandLine, exception.getMessage());
                writer.println(message);
                log.error(message, exception);
            }
        });
    }

    private String getCommandRegexp(Processor processor) {
        String commandName = Objects.requireNonNull(processor.getCommandName(), "processor.getCommandName()");
        String argsRegexp = Objects.requireNonNull(processor.getArgsRegexp(), "processor.getArgsRegexp()");
        if (commandName.matches(".*\\s+.*")) {
            throw new IllegalArgumentException(String.format("Command name should not contain whitespaces: '%s'," +
                    " Processor: %s", commandName, processor.getClass().getName()));
        }
        return commandName + createProcessorArgsRegexp(argsRegexp);
    }

    private String createProcessorArgsRegexp(String argsRegexp) {
        return argsRegexp.isBlank() ? "" : ("(?:\\s+)" + argsRegexp);
    }

}
