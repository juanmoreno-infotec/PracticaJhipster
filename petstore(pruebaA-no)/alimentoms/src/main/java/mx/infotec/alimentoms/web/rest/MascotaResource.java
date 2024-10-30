package mx.infotec.alimentoms.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import mx.infotec.alimentoms.domain.Mascota;
import mx.infotec.alimentoms.repository.MascotaRepository;
import mx.infotec.alimentoms.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link mx.infotec.alimentoms.domain.Mascota}.
 */
@RestController
@RequestMapping("/api/mascotas")
public class MascotaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MascotaResource.class);

    private static final String ENTITY_NAME = "alimentomsMascota";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MascotaRepository mascotaRepository;

    public MascotaResource(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    /**
     * {@code POST  /mascotas} : Create a new mascota.
     *
     * @param mascota the mascota to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mascota, or with status {@code 400 (Bad Request)} if the mascota has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Mascota>> createMascota(@RequestBody Mascota mascota) throws URISyntaxException {
        LOG.debug("REST request to save Mascota : {}", mascota);
        if (mascota.getId() != null) {
            throw new BadRequestAlertException("A new mascota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return mascotaRepository
            .save(mascota)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/mascotas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code GET  /mascotas} : get all the mascotas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mascotas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Mascota>> getAllMascotas() {
        LOG.debug("REST request to get all Mascotas");
        return mascotaRepository.findAll().collectList();
    }

    /**
     * {@code GET  /mascotas} : get all the mascotas as a stream.
     * @return the {@link Flux} of mascotas.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Mascota> getAllMascotasAsStream() {
        LOG.debug("REST request to get all Mascotas as a stream");
        return mascotaRepository.findAll();
    }

    /**
     * {@code GET  /mascotas/:id} : get the "id" mascota.
     *
     * @param id the id of the mascota to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mascota, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mascota>> getMascota(@PathVariable("id") String id) {
        LOG.debug("REST request to get Mascota : {}", id);
        Mono<Mascota> mascota = mascotaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mascota);
    }

    /**
     * {@code DELETE  /mascotas/:id} : delete the "id" mascota.
     *
     * @param id the id of the mascota to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMascota(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Mascota : {}", id);
        return mascotaRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
