package eu.dissco.core.translator.domain;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public final class TranslatorJobReport {

  private final Map<String, Integer> digitalSpecimenIssues;
  private final Map<String, Integer> digitalMediaIssues;
  private Integer successfulSpecimen;
  private Integer successfulMedia;

  public TranslatorJobReport(Map<String, Integer> digitalSpecimenIssues,
      Map<String, Integer> digitalMediaIssues, Integer successfulSpecimen,
      Integer successfulMedia) {
    this.digitalSpecimenIssues = digitalSpecimenIssues;
    this.digitalMediaIssues = digitalMediaIssues;
    this.successfulSpecimen = successfulSpecimen;
    this.successfulMedia = successfulMedia;
  }

  public TranslatorJobReport() {
    this(new HashMap<>(), new HashMap<>(), 0, 0);
  }

  public void addSpecimenIssues(String key) {
    digitalSpecimenIssues.put(key, digitalSpecimenIssues.getOrDefault(key, 0) + 1);
  }

  public void addMediaIssues(String key) {
    digitalMediaIssues.put(key, digitalMediaIssues.getOrDefault(key, 0) + 1);
  }

  public void incrementSuccessfulRecords(int digitalMediaCount) {
    successfulSpecimen = successfulSpecimen + 1;
    successfulMedia = successfulMedia + digitalMediaCount;
  }

}
