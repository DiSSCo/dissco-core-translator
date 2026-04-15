package eu.dissco.core.translator.domain;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public final class TranslatorJobReport {

	private final Map<String, Integer> digitalSpecimenRejected;

	private final Map<String, Integer> digitalMediaRejected;

	private Integer successfulSpecimen;

	private Integer successfulMedia;

	private Integer rejectedSpecimen;

	private Integer rejectedMedia;

	public TranslatorJobReport(Map<String, Integer> digitalSpecimenIssues, Map<String, Integer> digitalMediaIssues,
			Integer successfulSpecimen, Integer successfulMedia, Integer rejectedSpecimen, Integer rejectedMedia) {
		this.digitalSpecimenRejected = digitalSpecimenIssues == null ? new HashMap<>()
				: new HashMap<>(digitalSpecimenIssues);
		this.digitalMediaRejected = digitalMediaIssues == null ? new HashMap<>() : new HashMap<>(digitalMediaIssues);
		this.successfulSpecimen = successfulSpecimen == null ? 0 : successfulSpecimen;
		this.successfulMedia = successfulMedia == null ? 0 : successfulMedia;
		this.rejectedSpecimen = rejectedSpecimen == null ? 0 : rejectedSpecimen;
		this.rejectedMedia = rejectedMedia == null ? 0 : rejectedMedia;
	}

	public TranslatorJobReport() {
		this(new HashMap<>(), new HashMap<>(), 0, 0, 0, 0);
	}

	public void addRejectedSpecimen(String key) {
		rejectedSpecimen = rejectedSpecimen + 1;
		digitalSpecimenRejected.put(key, digitalSpecimenRejected.getOrDefault(key, 0) + 1);
	}

	public void addRejectedMedia(String key) {
		rejectedMedia = rejectedMedia + 1;
		digitalMediaRejected.put(key, digitalMediaRejected.getOrDefault(key, 0) + 1);
	}

	public void incrementSuccessfulRecords(int digitalMediaCount) {
		successfulSpecimen = successfulSpecimen + 1;
		successfulMedia = successfulMedia + digitalMediaCount;
	}

}
