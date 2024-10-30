package mx.infotec.alimentoms.web.rest;

import static mx.infotec.alimentoms.domain.MascotaAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import mx.infotec.alimentoms.IntegrationTest;
import mx.infotec.alimentoms.domain.Mascota;
import mx.infotec.alimentoms.repository.MascotaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link MascotaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MascotaResourceIT {

    private static final String ENTITY_API_URL = "/api/mascotas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Mascota mascota;

    private Mascota insertedMascota;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mascota createEntity() {
        return new Mascota();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mascota createUpdatedEntity() {
        return new Mascota();
    }

    @BeforeEach
    public void initTest() {
        mascota = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMascota != null) {
            mascotaRepository.delete(insertedMascota).block();
            insertedMascota = null;
        }
    }

    @Test
    void createMascota() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mascota
        var returnedMascota = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascota))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Mascota.class)
            .returnResult()
            .getResponseBody();

        // Validate the Mascota in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMascotaUpdatableFieldsEquals(returnedMascota, getPersistedMascota(returnedMascota));

        insertedMascota = returnedMascota;
    }

    @Test
    void createMascotaWithExistingId() throws Exception {
        // Create the Mascota with an existing ID
        mascota.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascota))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMascotasAsStream() {
        // Initialize the database
        mascotaRepository.save(mascota).block();

        List<Mascota> mascotaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Mascota.class)
            .getResponseBody()
            .filter(mascota::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(mascotaList).isNotNull();
        assertThat(mascotaList).hasSize(1);
        Mascota testMascota = mascotaList.get(0);

        assertMascotaAllPropertiesEquals(mascota, testMascota);
    }

    @Test
    void getAllMascotas() {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        // Get all the mascotaList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(mascota.getId()));
    }

    @Test
    void getMascota() {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        // Get the mascota
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, mascota.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(mascota.getId()));
    }

    @Test
    void getNonExistingMascota() {
        // Get the mascota
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void deleteMascota() {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mascota
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, mascota.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mascotaRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Mascota getPersistedMascota(Mascota mascota) {
        return mascotaRepository.findById(mascota.getId()).block();
    }

    protected void assertPersistedMascotaToMatchAllProperties(Mascota expectedMascota) {
        assertMascotaAllPropertiesEquals(expectedMascota, getPersistedMascota(expectedMascota));
    }

    protected void assertPersistedMascotaToMatchUpdatableProperties(Mascota expectedMascota) {
        assertMascotaAllUpdatablePropertiesEquals(expectedMascota, getPersistedMascota(expectedMascota));
    }
}
