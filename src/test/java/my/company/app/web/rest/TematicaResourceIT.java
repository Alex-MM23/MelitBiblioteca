package my.company.app.web.rest;

import static my.company.app.domain.TematicaAsserts.*;
import static my.company.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import my.company.app.IntegrationTest;
import my.company.app.domain.Tematica;
import my.company.app.repository.TematicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TematicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TematicaResourceIT {

    private static final String DEFAULT_ABREVIATURA = "AAAAAAAAAA";
    private static final String UPDATED_ABREVIATURA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tematicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TematicaRepository tematicaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTematicaMockMvc;

    private Tematica tematica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tematica createEntity(EntityManager em) {
        Tematica tematica = new Tematica().abreviatura(DEFAULT_ABREVIATURA).descripcion(DEFAULT_DESCRIPCION);
        return tematica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tematica createUpdatedEntity(EntityManager em) {
        Tematica tematica = new Tematica().abreviatura(UPDATED_ABREVIATURA).descripcion(UPDATED_DESCRIPCION);
        return tematica;
    }

    @BeforeEach
    public void initTest() {
        tematica = createEntity(em);
    }

    @Test
    @Transactional
    void createTematica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tematica
        var returnedTematica = om.readValue(
            restTematicaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tematica)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Tematica.class
        );

        // Validate the Tematica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTematicaUpdatableFieldsEquals(returnedTematica, getPersistedTematica(returnedTematica));
    }

    @Test
    @Transactional
    void createTematicaWithExistingId() throws Exception {
        // Create the Tematica with an existing ID
        tematica.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTematicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tematica)))
            .andExpect(status().isBadRequest());

        // Validate the Tematica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTematicas() throws Exception {
        // Initialize the database
        tematicaRepository.saveAndFlush(tematica);

        // Get all the tematicaList
        restTematicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tematica.getId().intValue())))
            .andExpect(jsonPath("$.[*].abreviatura").value(hasItem(DEFAULT_ABREVIATURA)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTematica() throws Exception {
        // Initialize the database
        tematicaRepository.saveAndFlush(tematica);

        // Get the tematica
        restTematicaMockMvc
            .perform(get(ENTITY_API_URL_ID, tematica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tematica.getId().intValue()))
            .andExpect(jsonPath("$.abreviatura").value(DEFAULT_ABREVIATURA))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingTematica() throws Exception {
        // Get the tematica
        restTematicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTematica() throws Exception {
        // Initialize the database
        tematicaRepository.saveAndFlush(tematica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tematica
        Tematica updatedTematica = tematicaRepository.findById(tematica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTematica are not directly saved in db
        em.detach(updatedTematica);
        updatedTematica.abreviatura(UPDATED_ABREVIATURA).descripcion(UPDATED_DESCRIPCION);

        restTematicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTematica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTematica))
            )
            .andExpect(status().isOk());

        // Validate the Tematica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTematicaToMatchAllProperties(updatedTematica);
    }

    @Test
    @Transactional
    void putNonExistingTematica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tematica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTematicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tematica.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tematica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tematica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTematica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tematica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTematicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tematica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tematica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTematica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tematica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTematicaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tematica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tematica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTematicaWithPatch() throws Exception {
        // Initialize the database
        tematicaRepository.saveAndFlush(tematica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tematica using partial update
        Tematica partialUpdatedTematica = new Tematica();
        partialUpdatedTematica.setId(tematica.getId());

        partialUpdatedTematica.abreviatura(UPDATED_ABREVIATURA);

        restTematicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTematica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTematica))
            )
            .andExpect(status().isOk());

        // Validate the Tematica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTematicaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTematica, tematica), getPersistedTematica(tematica));
    }

    @Test
    @Transactional
    void fullUpdateTematicaWithPatch() throws Exception {
        // Initialize the database
        tematicaRepository.saveAndFlush(tematica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tematica using partial update
        Tematica partialUpdatedTematica = new Tematica();
        partialUpdatedTematica.setId(tematica.getId());

        partialUpdatedTematica.abreviatura(UPDATED_ABREVIATURA).descripcion(UPDATED_DESCRIPCION);

        restTematicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTematica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTematica))
            )
            .andExpect(status().isOk());

        // Validate the Tematica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTematicaUpdatableFieldsEquals(partialUpdatedTematica, getPersistedTematica(partialUpdatedTematica));
    }

    @Test
    @Transactional
    void patchNonExistingTematica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tematica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTematicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tematica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tematica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tematica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTematica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tematica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTematicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tematica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tematica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTematica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tematica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTematicaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tematica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tematica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTematica() throws Exception {
        // Initialize the database
        tematicaRepository.saveAndFlush(tematica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tematica
        restTematicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tematica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tematicaRepository.count();
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

    protected Tematica getPersistedTematica(Tematica tematica) {
        return tematicaRepository.findById(tematica.getId()).orElseThrow();
    }

    protected void assertPersistedTematicaToMatchAllProperties(Tematica expectedTematica) {
        assertTematicaAllPropertiesEquals(expectedTematica, getPersistedTematica(expectedTematica));
    }

    protected void assertPersistedTematicaToMatchUpdatableProperties(Tematica expectedTematica) {
        assertTematicaAllUpdatablePropertiesEquals(expectedTematica, getPersistedTematica(expectedTematica));
    }
}
