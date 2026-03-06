package eu.dissco.core.translator.terms.utils;

import eu.dissco.core.translator.domain.License;

import java.util.List;
import java.util.Map;

public class LicenseUtils {

    private LicenseUtils() {
        // Private constructor to prevent instantiation
    }

    private static final List<String> ACCEPTED_LIST = List.of(
            License.CC0.getUrl(),
            License.CC_BY.getUrl()
    );

    private static final Map<License, String> LICENSE_MAP = Map.of(
            License.CC0,
            "creativecommons.org/publicdomain/zero/1.0/",
            License.CC_BY,
            "creativecommons.org/licenses/by/4.0/",
            License.CC_BY_SA,
            "creativecommons.org/licenses/by-sa/4.0/",
            License.CC_BY_NC,
            "creativecommons.org/licenses/by-nc/4.0/",
            License.CC_BY_ND,
            "creativecommons.org/licenses/by-nd/4.0/",
            License.CC_BY_NC_ND,
            "creativecommons.org/licenses/by-nc-nd/4.0/"
    );

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
        for (var entry : LICENSE_MAP.entrySet()) {
            if (license.contains(entry.getValue())) {
                return entry.getKey();
            }
        }
        var normalisedLicense = license.replace("/", "").replace("_", "").replace("-", "")
                .replace(" ", "").toUpperCase();
        // First most strict then more open as sometimes sentences are reused for example
        // "Creative Commons Attribution-ShareAlike 4.0 International License" contains both "BY-SA" and "BY", but should be classified as BY-SA
        if (normalisedLicense.contains("BYNCND") || normalisedLicense.contains("NONCOMMERCIALNONDERIVATIVES")) {
            return License.CC_BY_NC_ND;
        } else if (normalisedLicense.contains("BYND") || normalisedLicense.contains("NONDERIVATIVES")) {
            return License.CC_BY_ND;
        } else if (normalisedLicense.contains("BYNC") || normalisedLicense.contains("NONCOMMERCIAL")) {
            return License.CC_BY_NC;
        } else if (normalisedLicense.contains("BYSA") || normalisedLicense.contains("SHAREALIKE")) {
            return License.CC_BY_SA;
        } else if (normalisedLicense.contains("CCBY") || normalisedLicense.contains(
                "CREATIVECOMMONSATTRIBUTION")) {
            return License.CC_BY;
        } else if (normalisedLicense.contains("CC0") || normalisedLicense.contains("PUBLICDOMAIN")
                || normalisedLicense.contains("NORIGHTS")) {
            return License.CC0;
        } else {
            return null;
        }
    }

}
