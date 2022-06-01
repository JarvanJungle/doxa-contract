package org.doxa.contract.microservices.DTO.entityService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressesResponseWrapper {
    private String status;
    private AddressDetails data;
}
