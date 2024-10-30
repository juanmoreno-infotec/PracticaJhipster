package mx.infotec.alimentoms.repository;

import mx.infotec.alimentoms.domain.Mascota;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Mascota entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MascotaRepository extends ReactiveMongoRepository<Mascota, String> {}
