package com.exchange.exchangerate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.exchange.model.Currency;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    @JsonProperty("base_code")
    private Currency base;
    @JsonProperty("time_last_update_unix")
    private long lastUpdateTimeUnix;
    @JsonProperty("time_next_update_unix")
    private long nextUpdateTimeUnix;
    Map<String, Double> rates;

    public LocalDateTime getLastUpdateDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(this.lastUpdateTimeUnix), ZoneOffset.UTC);
    }

    public LocalDateTime getNextUpdateDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(this.nextUpdateTimeUnix), ZoneOffset.UTC);
    }
}
