package eu.dissco.core.translator.domain;

import java.util.List;

public record SourceSystemInformation(String sourceSystemName, String sourceSystemUrl, List<String> sourceSystemFilters,
                                      List<String> specimenMass, List<String> mediaMass) {

}
