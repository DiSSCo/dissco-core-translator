package eu.dissco.core.translator.domain;

import eu.dissco.core.translator.database.jooq.enums.JobState;

public record TranslatorJobResult(
    JobState jobState,
    int processedRecords
) {

}
