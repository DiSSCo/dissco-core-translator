package eu.dissco.core.translator.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.properties.MasProperties;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebClientServiceTest {

  private static final String MAS_1 = "specimen-mas-1";
  private static final String MAS_2 = "specimen-mas-2";
  private MasProperties masProperties;
  @Mock
  private SourceSystemComponent sourceSystemComponent;

  @BeforeEach
  void setUp() {
    masProperties = new MasProperties();
  }

  @Test
  void testGetMachineAnnotationServicesConcatListSpecimen(){
    // Given
    masProperties.setAdditionalSpecimenMass(List.of(MAS_1));
    given(sourceSystemComponent.getSpecimenMass()).willReturn(List.of(MAS_2));
    var expected = Set.of(MAS_1, MAS_2);

    // When
    var result = WebClientService.getMachineAnnotationServices(false, masProperties, sourceSystemComponent);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testGetMachineAnnotationServicesConcatListSpecimenMedia(){
    // Given
    masProperties.setAdditionalMediaMass(List.of(MAS_1));
    given(sourceSystemComponent.getMediaMass()).willReturn(List.of(MAS_2));
    var expected = Set.of(MAS_1, MAS_2);

    // When
    var result = WebClientService.getMachineAnnotationServices(true, masProperties, sourceSystemComponent);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testGetMachineAnnotationServicesDuplicateSpecimen(){
    // Given
    masProperties.setAdditionalSpecimenMass(List.of(MAS_1));
    given(sourceSystemComponent.getSpecimenMass()).willReturn(List.of(MAS_1));
    var expected = Set.of(MAS_1);

    // When
    var result = WebClientService.getMachineAnnotationServices(false, masProperties, sourceSystemComponent);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testGetMachineAnnotationServicesDuplicateSpecimenMedia(){
    // Given
    masProperties.setAdditionalMediaMass(List.of(MAS_1));
    given(sourceSystemComponent.getMediaMass()).willReturn(List.of(MAS_1));
    var expected = Set.of(MAS_1);

    // When
    var result = WebClientService.getMachineAnnotationServices(true, masProperties, sourceSystemComponent);

    // Then
    assertThat(result).isEqualTo(expected);
  }

}
