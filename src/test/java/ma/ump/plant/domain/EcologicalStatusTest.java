package ma.ump.plant.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.ump.plant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EcologicalStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EcologicalStatus.class);
        EcologicalStatus ecologicalStatus1 = new EcologicalStatus();
        ecologicalStatus1.setId(1L);
        EcologicalStatus ecologicalStatus2 = new EcologicalStatus();
        ecologicalStatus2.setId(ecologicalStatus1.getId());
        assertThat(ecologicalStatus1).isEqualTo(ecologicalStatus2);
        ecologicalStatus2.setId(2L);
        assertThat(ecologicalStatus1).isNotEqualTo(ecologicalStatus2);
        ecologicalStatus1.setId(null);
        assertThat(ecologicalStatus1).isNotEqualTo(ecologicalStatus2);
    }
}
