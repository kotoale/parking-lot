package kotoale.parking.lot.service;

import kotoale.parking.lot.processors.Processor;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")
@Service
public class CommandHandler {

    private final PrintWriter writer;
    private final Map<String, Processor> commandNameToProcessor;
    private final Pattern commandPattern;

    public CommandHandler(PrintWriter writer, List<Processor> processors) {
        this.writer = writer;
        commandNameToProcessor = processors.stream().collect(Collectors.toMap(Processor::getCommandName, Function.identity()));
        commandPattern = Pattern.compile(processors.stream().map(Processor::getCommandRegexp)
                .collect(Collectors.joining("|", "^(?:\\s*)(", ")(?:\\s*)$")));
    }

    @SuppressWarnings("unchecked")
    public void handle(Stream<String> commands) {

        commands.forEach(commandLine -> {
            Matcher matcher = commandPattern.matcher(commandLine);
            if (!matcher.matches()) {
                writer.println("Incorrect command: " + commandLine);
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
            } catch (IllegalArgumentException | IllegalStateException exception) {
                writer.println(exception.getMessage());
            } catch (Exception exception) {
                writer.println(String.format("Failed to process command: '%s', error: %s",
                        commandLine, exception.getMessage()));
            }
        });
    }

}
