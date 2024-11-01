package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CaptureDeviceTest {

  private final CaptureDevice captureDevice = new CaptureDevice();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var captureDeviceString = "Canon EOS 5D Mark IV";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:captureDevice", captureDeviceString);

    // When
    var result = captureDevice.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(captureDeviceString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var captureDeviceString = "Canon EOS 5D Mark IV";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:captureEquipment", captureDeviceString);

    // When
    var result = captureDevice.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(captureDeviceString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = captureDevice.getTerm();

    // Then
    assertThat(result).isEqualTo(CaptureDevice.TERM);
  }

}
