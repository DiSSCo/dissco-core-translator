package eu.dissco.core.translator.terms.utils;

import eu.dissco.core.translator.domain.License;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static eu.dissco.core.translator.domain.License.*;
import static org.junit.jupiter.api.Assertions.*;


class LicenseUtilsTest {

    public static Stream<Arguments> harmoniseLicenseTestData() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("CC0 1.0", CC0),
                Arguments.of("https://creativecommons.org/licenses/by/4.0/", CC_BY),
                Arguments.of("CC_BY_4_0", CC_BY),
                Arguments.of("all rights reserved", null),
                Arguments.of("https://creativecommons.org/licenses/by-nc/4.0/legalcode.es", CC_BY_NC),
                Arguments.of("Attribution-ShareAlike (BY-SA) Creative Commons License and GNU Free Documentation License (GFDL)", CC_BY_SA),
                Arguments.of("https://huh.harvard.edu/access-digital-reproductions-works-public-domain", CC0),
                Arguments.of("https://creativecommons.org/licenses/by-nc-nd/4.0/legalcode", CC_BY_NC_ND),
                Arguments.of("© The Board of Trustees of the Royal Botanic Gardens, Kew.", null),
                Arguments.of("Share Alike", CC_BY_SA),
                Arguments.of("Creative Commons Attribution-ShareAlike 4.0 International License", CC_BY_SA),
                Arguments.of("Non Commercial", CC_BY_NC),
                Arguments.of("Non Derivatives", CC_BY_ND),
                Arguments.of("Create Commons Attribution-NonCommercial-NonDerivatives 4.0 International License", CC_BY_NC_ND)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://creativecommons.org/publicdomain/zero/1.0/legalcode",
            "https://creativecommons.org/licenses/by/4.0/legalcode"})
    void testLicenseComplies(String license) {
        // Given

        // When
        var result = LicenseUtils.licenseComplies(license);

        // Then
        assertTrue(result);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"https://creativecommons.org/licenses/by-sa/4.0/legalcode",
            "https://creativecommons.org/licenses/by-nc/4.0/legalcode",
            "some random license",
            "I don't want my data to be used"})
    void testLicenseDoesNotComply(String license) {
        // Given

        // When
        var result = LicenseUtils.licenseComplies(license);

        // Then
        assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("harmoniseLicenseTestData")
    void testHarmoniseLicense(String license, License harmonisedLicense) {
        // Given

        // When
        var result = LicenseUtils.harmoniseLicense(license);

        // Then
        assertEquals(harmonisedLicense, result);
    }
}
