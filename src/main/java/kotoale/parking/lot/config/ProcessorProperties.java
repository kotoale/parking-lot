package kotoale.parking.lot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "app.processor")
@Validated
public class ProcessorProperties {
    @NotNull
    private String plateRegexp;

    public String getPlateRegexp() {
        return plateRegexp;
    }

    public void setPlateRegexp(String plateRegexp) {
        this.plateRegexp = plateRegexp;
    }
}
