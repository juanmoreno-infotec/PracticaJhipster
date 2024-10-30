package mx.infotec.alimentoms.domain;

import java.util.UUID;

public class MascotaTestSamples {

    public static Mascota getMascotaSample1() {
        return new Mascota().id("id1");
    }

    public static Mascota getMascotaSample2() {
        return new Mascota().id("id2");
    }

    public static Mascota getMascotaRandomSampleGenerator() {
        return new Mascota().id(UUID.randomUUID().toString());
    }
}
