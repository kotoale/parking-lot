package kotoale.parking.lot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@ConfigurationProperties(prefix = "app.calculator")
@Validated
public class CalculatorProperties {
    @NotNull
    @Positive
    private Integer fixedPrice;
    @NotNull
    @Positive
    private Integer firstHours;
    @NotNull
    @Positive
    private Integer hourlyRate;

    public Integer getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(Integer fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public Integer getFirstHours() {
        return firstHours;
    }

    public void setFirstHours(Integer firstHours) {
        this.firstHours = firstHours;
    }

    public Integer getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Integer hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
