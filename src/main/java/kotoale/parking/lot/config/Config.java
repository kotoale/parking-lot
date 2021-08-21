package kotoale.parking.lot.config;

import kotoale.parking.lot.calculator.ChargeCalculator;
import kotoale.parking.lot.calculator.FirstHoursFixedPriceChargeCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintWriter;

@Configuration
public class Config {
    @Bean
    public ChargeCalculator chargeCalculator() {
        return new FirstHoursFixedPriceChargeCalculator(10, 2, 10);
    }

    @Bean
    public PrintWriter printWriter() {
        return new PrintWriter(System.out);
    }
}
