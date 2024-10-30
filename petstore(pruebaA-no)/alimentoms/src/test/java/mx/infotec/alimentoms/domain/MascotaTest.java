package mx.infotec.alimentoms.domain;

import static mx.infotec.alimentoms.domain.MascotaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.alimentoms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MascotaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mascota.class);
        Mascota mascota1 = getMascotaSample1();
        Mascota mascota2 = new Mascota();
        assertThat(mascota1).isNotEqualTo(mascota2);

        mascota2.setId(mascota1.getId());
        assertThat(mascota1).isEqualTo(mascota2);

        mascota2 = getMascotaSample2();
        assertThat(mascota1).isNotEqualTo(mascota2);
    }

    @Test
    void hashCodeVerifier() {
        Mascota mascota = new Mascota();
        assertThat(mascota.hashCode()).isZero();

        Mascota mascota1 = getMascotaSample1();
        mascota.setId(mascota1.getId());
        assertThat(mascota).hasSameHashCodeAs(mascota1);
    }
}
