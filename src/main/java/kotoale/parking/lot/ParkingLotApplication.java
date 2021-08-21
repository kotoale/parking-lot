package kotoale.parking.lot;

import kotoale.parking.lot.processors.Processor;
import kotoale.parking.lot.service.CommandHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class ParkingLotApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ParkingLotApplication.class, args);
        CommandHandler handler = context.getBean(CommandHandler.class);

        if (args.length == 0) {
            printUsage(handler.getProcessors());
            return;
        }

        try {
            handler.handle(Files.lines(Paths.get(args[0])));
        } catch (Exception exception) {
            exception.printStackTrace();
            printUsage(handler.getProcessors());
        }
    }

    @SuppressWarnings("rawtypes")
    private static void printUsage(List<Processor> processors) {
        System.out.printf("Usage: %s <file>%n", ParkingLotApplication.class.getSimpleName());
        System.out.println("Prints Parking lot output to STDOUT");
        System.out.printf("  %-10s The text file with commands to process:%n%n", "<file>");
        System.out.printf("  %-10s RegExp%n", "Command");
        processors.stream()
                .map(processor -> String.format("  %-10s %s",
                        processor.getCommandName(), processor.getCommandRegexp()))
                .forEach(System.out::println);

    }
}
