package eu.dissco.core.translator.domain.eml;

import lombok.Builder;

@Builder
public record Agent(
    Name individualName,
    Address address,
    String electronicMailAddress,
    String phone,
    String role
) {

}

