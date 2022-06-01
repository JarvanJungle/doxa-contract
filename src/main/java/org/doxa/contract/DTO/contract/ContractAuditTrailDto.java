package org.doxa.contract.DTO.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
public class ContractAuditTrailDto {
    private String userName;
    private String userUuid;
    private String role;
    private String action;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant date;
}
