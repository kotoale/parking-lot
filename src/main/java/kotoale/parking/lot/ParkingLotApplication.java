package kotoale.parking.lot;

import kotoale.parking.lot.service.CommandHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ParkingLotApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ParkingLotApplication.class, args);
        CommandHandler handler = context.getBean(CommandHandler.class);
        handler.handle(Arrays.stream((
                "create 6\n" +
                        "   park KA-01-HH-1234\n" +
                        "park KA-01-HH-9999\n" +
                        "park KA-01-BB-0001\n" +
                        "park KA-01-HH-7777\n" +
                        "park KA-01-HH-2701\n" +
                        "park KA-01-HH-3141\n" +
                        "leave KA-01-HH-3141 4\n" +
                        "status\n" +
                        "park KA-01-P-333\n" +
                        "park DL-12-AA-9999\n" +
                        "leave KA-01-HH-1234 4\n" +
                        "leave KA-01-BB-0001 6\n" +
                        "leave DL-12-AA-9999 2\n" +
                        "park KA-09-HH-0987\n" +
                        "park CA-09-IO-1111\n" +
                        "park KA-09-HH-0123\n" +
                        "status").split("\n")));
    }
}
