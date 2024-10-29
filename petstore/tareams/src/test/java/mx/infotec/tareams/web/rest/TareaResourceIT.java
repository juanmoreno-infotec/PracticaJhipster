package mx.infotec.tareams.web.rest;

import static mx.infotec.tareams.domain.TareaAsserts.*;
import static mx.infotec.tareams.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import mx.infotec.tareams.IntegrationTest;
import mx.infotec.tareams.domain.Tarea;
import mx.infotec.tareams.repository.TareaRepository;
import mx.infotec.tareams.service.dto.TareaDTO;
import mx.infotec.tareams.service.mapper.TareaMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TareaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TareaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_LIMITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_LIMITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tareas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private TareaMapper tareaMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Tarea tarea;

    private Tarea insertedTarea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createEntity() {
        return new Tarea().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION).fechaLimite(DEFAULT_FECHA_LIMITE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createUpdatedEntity() {
        return new Tarea().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaLimite(UPDATED_FECHA_LIMITE);
    }

    @BeforeEach
    public void initTest() {
        tarea = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTarea != null) {
            tareaRepository.delete(insertedTarea).block();
            insertedTarea = null;
        }
    }

    @Test
    void createTarea() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);
        var returnedTareaDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(TareaDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Tarea in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTarea = tareaMapper.toEntity(returnedTareaDTO);
        assertTareaUpdatableFieldsEquals(returnedTarea, getPersistedTarea(returnedTarea));

        insertedTarea = returnedTarea;
    }

    @Test
    void createTareaWithExistingId() throws Exception {
        // Create the Tarea with an existing ID
        tarea.setId("existing_id");
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTareas() {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea).block();

        // Get all the tareaList
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
            .value(hasItem(tarea.getId()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION))
            .jsonPath("$.[*].fechaLimite")
            .value(hasItem(DEFAULT_FECHA_LIMITE.toString()));
    }

    @Test
    void getTarea() {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea).block();

        // Get the tarea
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tarea.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tarea.getId()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION))
            .jsonPath("$.fechaLimite")
            .value(is(DEFAULT_FECHA_LIMITE.toString()));
    }

    @Test
    void getNonExistingTarea() {
        // Get the tarea
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea
        Tarea updatedTarea = tareaRepository.findById(tarea.getId()).block();
        updatedTarea.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaLimite(UPDATED_FECHA_LIMITE);
        TareaDTO tareaDTO = tareaMapper.toDto(updatedTarea);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tareaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTareaToMatchAllProperties(updatedTarea);
    }

    @Test
    void putNonExistingTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tareaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.descripcion(UPDATED_DESCRIPCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedTarea))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTareaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTarea, tarea), getPersistedTarea(tarea));
    }

    @Test
    void fullUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaLimite(UPDATED_FECHA_LIMITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedTarea))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTareaUpdatableFieldsEquals(partialUpdatedTarea, getPersistedTarea(partialUpdatedTarea));
    }

    @Test
    void patchNonExistingTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tareaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(tareaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTarea() {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tarea
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tarea.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tareaRepository.count().block();
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

    protected Tarea getPersistedTarea(Tarea tarea) {
        return tareaRepository.findById(tarea.getId()).block();
    }

    protected void assertPersistedTareaToMatchAllProperties(Tarea expectedTarea) {
        assertTareaAllPropertiesEquals(expectedTarea, getPersistedTarea(expectedTarea));
    }

    protected void assertPersistedTareaToMatchUpdatableProperties(Tarea expectedTarea) {
        assertTareaAllUpdatablePropertiesEquals(expectedTarea, getPersistedTarea(expectedTarea));
    }
}
