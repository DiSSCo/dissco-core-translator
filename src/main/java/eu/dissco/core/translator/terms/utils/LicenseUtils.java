package eu.dissco.core.translator.terms.utils;

import eu.dissco.core.translator.domain.License;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static eu.dissco.core.translator.domain.License.*;
import static eu.dissco.core.translator.domain.License.CC0;

public class LicenseUtils {

    private LicenseUtils() {
        // Private constructor to prevent instantiation
    }

    private static final List<String> ACCEPTED_LIST = List.of(
            CC0.getUrl(),
            CC_BY.getUrl()
    );

    private static final Map<License, String> LICENSE_URL_MAP = Map.of(
            CC0,
            "creativecommons.org/publicdomain/zero/1.0/",
            CC_BY,
            "creativecommons.org/licenses/by/4.0/",
            CC_BY_SA,
            "creativecommons.org/licenses/by-sa/4.0/",
            CC_BY_NC,
            "creativecommons.org/licenses/by-nc/4.0/",
            CC_BY_ND,
            "creativecommons.org/licenses/by-nd/4.0/",
            CC_BY_NC_ND,
            "creativecommons.org/licenses/by-nc-nd/4.0/"
    );

     private static final Map<License, Set<String>> LICENSE_FUZY_MATCH;
     static {
         LICENSE_FUZY_MATCH = new LinkedHashMap<>();
         LICENSE_FUZY_MATCH.put(CC_BY_NC_ND, Set.of("BYNCND", "NONCOMMERCIALNONDERIVATIVES"));
         LICENSE_FUZY_MATCH.put(CC_BY_ND, Set.of("BYND", "NONDERIVATIVES"));
         LICENSE_FUZY_MATCH.put(CC_BY_NC, Set.of("BYNC", "NONCOMMERCIAL"));
         LICENSE_FUZY_MATCH.put(CC_BY_SA, Set.of("BYSA", "SHAREALIKE"));
         LICENSE_FUZY_MATCH.put(CC_BY, Set.of("CCBY", "CREATIVECOMMONSATTRIBUTION"));
         LICENSE_FUZY_MATCH.put(CC0, Set.of("CC0", "PUBLICDOMAIN", "NORIGHTS"));
     }

    public static boolean licenseComplies(String license) {
        if (license == null) {
            return false;
        }
        return ACCEPTED_LIST.contains(license);
    }

    public static License harmoniseLicense(String license) {
        if (license == null) {
            return null;
        }
        for (var entry : LICENSE_URL_MAP.entrySet()) {
            if (license.contains(entry.getValue())) {
                return entry.getKey();
            }
        }
        var normalisedLicense = normalize(license);
        for (var entry : LICENSE_FUZY_MATCH.entrySet()) {
            for (var token : entry.getValue()) {
                if (normalisedLicense.contains(token)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    // Helper to centralise normalization rules; makes changes easier and reduces noise in the main method
    private static String normalize(String license) {
        return license.replace("/", "")
                .replace("_", "")
                .replace("-", "")
                .replace(" ", "")
                .toUpperCase();
    }

}
