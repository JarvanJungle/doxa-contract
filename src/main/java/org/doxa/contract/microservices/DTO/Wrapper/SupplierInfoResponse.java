package org.doxa.contract.microservices.DTO.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.doxa.contract.microservices.DTO.SupplierInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierInfoResponse {

    private String status;
    private SupplierInfo data;
    private Long timestamp;
}
