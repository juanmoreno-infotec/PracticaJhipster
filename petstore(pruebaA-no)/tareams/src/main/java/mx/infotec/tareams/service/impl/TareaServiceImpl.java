package mx.infotec.tareams.service.impl;

import mx.infotec.tareams.repository.TareaRepository;
import mx.infotec.tareams.service.TareaService;
import mx.infotec.tareams.service.dto.TareaDTO;
import mx.infotec.tareams.service.mapper.TareaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.tareams.domain.Tarea}.
 */
@Service
public class TareaServiceImpl implements TareaService {

    private static final Logger LOG = LoggerFactory.getLogger(TareaServiceImpl.class);

    private final TareaRepository tareaRepository;

    private final TareaMapper tareaMapper;

    public TareaServiceImpl(TareaRepository tareaRepository, TareaMapper tareaMapper) {
        this.tareaRepository = tareaRepository;
        this.tareaMapper = tareaMapper;
    }

    @Override
    public Mono<TareaDTO> save(TareaDTO tareaDTO) {
        LOG.debug("Request to save Tarea : {}", tareaDTO);
        return tareaRepository.save(tareaMapper.toEntity(tareaDTO)).map(tareaMapper::toDto);
    }

    @Override
    public Mono<TareaDTO> update(TareaDTO tareaDTO) {
        LOG.debug("Request to update Tarea : {}", tareaDTO);
        return tareaRepository.save(tareaMapper.toEntity(tareaDTO)).map(tareaMapper::toDto);
    }

    @Override
    public Mono<TareaDTO> partialUpdate(TareaDTO tareaDTO) {
        LOG.debug("Request to partially update Tarea : {}", tareaDTO);

        return tareaRepository
            .findById(tareaDTO.getId())
            .map(existingTarea -> {
                tareaMapper.partialUpdate(existingTarea, tareaDTO);

                return existingTarea;
            })
            .flatMap(tareaRepository::save)
            .map(tareaMapper::toDto);
    }

    @Override
    public Flux<TareaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tareas");
        return tareaRepository.findAllBy(pageable).map(tareaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return tareaRepository.count();
    }

    @Override
    public Mono<TareaDTO> findOne(String id) {
        LOG.debug("Request to get Tarea : {}", id);
        return tareaRepository.findById(id).map(tareaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Tarea : {}", id);
        return tareaRepository.deleteById(id);
    }
}
