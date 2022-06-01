package org.doxa.contract.DTO.contractRequest;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CRAuditTrailDto {

    private String userUuid;

    private String userName;
    
    private String role;
    
    private String action;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant createdDate;
}
