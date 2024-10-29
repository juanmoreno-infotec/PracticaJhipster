package mx.infotec.tareams.service;

import mx.infotec.tareams.service.dto.TareaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.tareams.domain.Tarea}.
 */
public interface TareaService {
    /**
     * Save a tarea.
     *
     * @param tareaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TareaDTO> save(TareaDTO tareaDTO);

    /**
     * Updates a tarea.
     *
     * @param tareaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TareaDTO> update(TareaDTO tareaDTO);

    /**
     * Partially updates a tarea.
     *
     * @param tareaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TareaDTO> partialUpdate(TareaDTO tareaDTO);

    /**
     * Get all the tareas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TareaDTO> findAll(Pageable pageable);

    /**
     * Returns the number of tareas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" tarea.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TareaDTO> findOne(String id);

    /**
     * Delete the "id" tarea.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
