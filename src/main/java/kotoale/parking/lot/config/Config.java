package kotoale.parking.lot.config;

import kotoale.parking.lot.calculator.ChargeCalculator;
import kotoale.parking.lot.calculator.FirstHoursFixedPriceChargeCalculator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        CalculatorProperties.class,
        ProcessorProperties.class
})
public class Config {

    @Bean
    public ChargeCalculator chargeCalculator(CalculatorProperties calculatorProperties) {
        return new FirstHoursFixedPriceChargeCalculator(calculatorProperties.getFixedPrice(),
                calculatorProperties.getFirstHours(), calculatorProperties.getHourlyRate());
    }
}
