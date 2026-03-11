package eu.dissco.core.translator.domain;

import lombok.Getter;

@Getter
public enum License {
  CC0("https://creativecommons.org/publicdomain/zero/1.0/legalcode"),
  CC_BY("https://creativecommons.org/licenses/by/4.0/legalcode"),
  CC_BY_SA("https://creativecommons.org/licenses/by-sa/4.0/legalcode"),
  CC_BY_NC("https://creativecommons.org/licenses/by-nc/4.0/legalcode"),
  CC_BY_ND("https://creativecommons.org/licenses/by-nd/4.0/legalcode"),
  CC_BY_NC_ND("https://creativecommons.org/licenses/by-nc-nd/4.0/legalcode")  ;

  private final String url;

  License(String url) {
    this.url = url;
  }

}
