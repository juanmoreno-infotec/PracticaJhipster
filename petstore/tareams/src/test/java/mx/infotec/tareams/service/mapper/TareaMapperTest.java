package mx.infotec.tareams.service.mapper;

import static mx.infotec.tareams.domain.TareaAsserts.*;
import static mx.infotec.tareams.domain.TareaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TareaMapperTest {

    private TareaMapper tareaMapper;

    @BeforeEach
    void setUp() {
        tareaMapper = new TareaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTareaSample1();
        var actual = tareaMapper.toEntity(tareaMapper.toDto(expected));
        assertTareaAllPropertiesEquals(expected, actual);
    }
}
