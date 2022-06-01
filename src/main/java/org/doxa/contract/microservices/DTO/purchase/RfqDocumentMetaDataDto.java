package org.doxa.contract.microservices.DTO.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RfqDocumentMetaDataDto {

    @JsonProperty
    @NotNull
    private String guid;

    @JsonProperty
    @NotNull
    private String fileLabel;

    @JsonProperty
    private String fileDescription;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    @NotNull
    private Instant uploadedOn;

    @JsonProperty
    @NotNull
    private String uploadedBy;

    @JsonProperty
    @NotNull
    private String uploaderUuid;

    @JsonProperty
    @NotNull
    private boolean externalDocument;

    @JsonProperty
    private String supplierCompanyUuid;

}
