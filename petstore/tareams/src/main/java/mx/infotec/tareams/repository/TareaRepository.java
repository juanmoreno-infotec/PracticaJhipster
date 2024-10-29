package mx.infotec.tareams.repository;

import mx.infotec.tareams.domain.Tarea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Tarea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TareaRepository extends ReactiveMongoRepository<Tarea, String> {
    Flux<Tarea> findAllBy(Pageable pageable);
}
