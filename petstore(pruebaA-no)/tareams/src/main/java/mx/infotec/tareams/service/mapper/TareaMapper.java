package mx.infotec.tareams.service.mapper;

import mx.infotec.tareams.domain.Tarea;
import mx.infotec.tareams.service.dto.TareaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tarea} and its DTO {@link TareaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TareaMapper extends EntityMapper<TareaDTO, Tarea> {}
