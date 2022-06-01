package org.doxa.contract.DTO.contractRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitResponseCRDto {

    private String contractRequestNumber;

    private String contractRequestUuid;

    private String globalCRNumber;
}
