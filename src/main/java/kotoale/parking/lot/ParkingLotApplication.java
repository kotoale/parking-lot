package kotoale.parking.lot;

import kotoale.parking.lot.processors.Processor;
import kotoale.parking.lot.service.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class ParkingLotApplication {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ParkingLotApplication.class, args);
        CommandHandler handler = context.getBean(CommandHandler.class);

        if (args.length == 0 || "-h".equals(args[0]) || "--help".equals(args[0])) {
            printUsage(handler.getProcessors());
            return;
        }

        String file = args[0];
        try {
            handler.handle(Files.lines(Paths.get(file)), new PrintWriter(System.out, true));
            log.info("Successfully processed file: {}", file);
        } catch (Exception exception) {
            log.error("Failed to process file: {}", file, exception);
            printUsage(handler.getProcessors());
            System.exit(1);
        }
    }

    @SuppressWarnings("rawtypes")
    private static void printUsage(List<Processor> processors) {
        System.out.printf("Usage: %s <file>%n", ParkingLotApplication.class.getSimpleName());
        System.out.println("Prints Parking lot output to STDOUT");
        System.out.printf("  %-10s The text file with commands to process:%n%n", "<file>");
        System.out.printf("  %-10s args regexp%n", "Command");
        processors.stream()
                .map(processor -> String.format("  %-10s %s",
                        processor.getCommandName(), processor.getArgsRegexp()))
                .forEach(System.out::println);

    }
}
