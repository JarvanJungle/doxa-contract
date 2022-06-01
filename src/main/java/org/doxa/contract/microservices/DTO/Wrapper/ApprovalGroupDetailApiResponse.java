package org.doxa.contract.microservices.DTO.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.doxa.contract.microservices.DTO.ApprovalGroupDetail;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalGroupDetailApiResponse {

    public String status;
    public ApprovalGroupDetail data;
    public Long timeStamp;
}
