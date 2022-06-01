package org.doxa.contract.DTO.contractRequest;

import java.time.Instant;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CRDocumentDto {

    @NotNull
	private String guid;

    @NotNull
	private String fileLabel;

	private String fileDescription;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
	private Instant upLoadedOn;

	private String uploadedByName;

	private String uploadedByUuid;

	@JsonProperty
	private boolean externalDocument;

}
