package my.company.app.web.rest;

import static my.company.app.domain.LibrosAsserts.*;
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
import my.company.app.domain.Libros;
import my.company.app.repository.LibrosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LibrosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LibrosResourceIT {

    private static final Integer DEFAULT_ISBN = 1;
    private static final Integer UPDATED_ISBN = 2;

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final String DEFAULT_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEN = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGINAS = 1;
    private static final Integer UPDATED_PAGINAS = 2;

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_ALQUILADOS = 1;
    private static final Integer UPDATED_NUMERO_ALQUILADOS = 2;

    private static final String ENTITY_API_URL = "/api/libros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LibrosRepository librosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibrosMockMvc;

    private Libros libros;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libros createEntity(EntityManager em) {
        Libros libros = new Libros()
            .isbn(DEFAULT_ISBN)
            .stock(DEFAULT_STOCK)
            .autor(DEFAULT_AUTOR)
            .imagen(DEFAULT_IMAGEN)
            .paginas(DEFAULT_PAGINAS)
            .titulo(DEFAULT_TITULO)
            .numeroAlquilados(DEFAULT_NUMERO_ALQUILADOS);
        return libros;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libros createUpdatedEntity(EntityManager em) {
        Libros libros = new Libros()
            .isbn(UPDATED_ISBN)
            .stock(UPDATED_STOCK)
            .autor(UPDATED_AUTOR)
            .imagen(UPDATED_IMAGEN)
            .paginas(UPDATED_PAGINAS)
            .titulo(UPDATED_TITULO)
            .numeroAlquilados(UPDATED_NUMERO_ALQUILADOS);
        return libros;
    }

    @BeforeEach
    public void initTest() {
        libros = createEntity(em);
    }

    @Test
    @Transactional
    void createLibros() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Libros
        var returnedLibros = om.readValue(
            restLibrosMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libros)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Libros.class
        );

        // Validate the Libros in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLibrosUpdatableFieldsEquals(returnedLibros, getPersistedLibros(returnedLibros));
    }

    @Test
    @Transactional
    void createLibrosWithExistingId() throws Exception {
        // Create the Libros with an existing ID
        libros.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibrosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libros)))
            .andExpect(status().isBadRequest());

        // Validate the Libros in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLibros() throws Exception {
        // Initialize the database
        librosRepository.saveAndFlush(libros);

        // Get all the librosList
        restLibrosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libros.getId().intValue())))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.[*].paginas").value(hasItem(DEFAULT_PAGINAS)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].numeroAlquilados").value(hasItem(DEFAULT_NUMERO_ALQUILADOS)));
    }

    @Test
    @Transactional
    void getLibros() throws Exception {
        // Initialize the database
        librosRepository.saveAndFlush(libros);

        // Get the libros
        restLibrosMockMvc
            .perform(get(ENTITY_API_URL_ID, libros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(libros.getId().intValue()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.autor").value(DEFAULT_AUTOR))
            .andExpect(jsonPath("$.imagen").value(DEFAULT_IMAGEN))
            .andExpect(jsonPath("$.paginas").value(DEFAULT_PAGINAS))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.numeroAlquilados").value(DEFAULT_NUMERO_ALQUILADOS));
    }

    @Test
    @Transactional
    void getNonExistingLibros() throws Exception {
        // Get the libros
        restLibrosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLibros() throws Exception {
        // Initialize the database
        librosRepository.saveAndFlush(libros);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libros
        Libros updatedLibros = librosRepository.findById(libros.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLibros are not directly saved in db
        em.detach(updatedLibros);
        updatedLibros
            .isbn(UPDATED_ISBN)
            .stock(UPDATED_STOCK)
            .autor(UPDATED_AUTOR)
            .imagen(UPDATED_IMAGEN)
            .paginas(UPDATED_PAGINAS)
            .titulo(UPDATED_TITULO)
            .numeroAlquilados(UPDATED_NUMERO_ALQUILADOS);

        restLibrosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLibros.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLibros))
            )
            .andExpect(status().isOk());

        // Validate the Libros in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLibrosToMatchAllProperties(updatedLibros);
    }

    @Test
    @Transactional
    void putNonExistingLibros() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libros.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibrosMockMvc
            .perform(put(ENTITY_API_URL_ID, libros.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libros)))
            .andExpect(status().isBadRequest());

        // Validate the Libros in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLibros() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libros.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibrosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(libros))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libros in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLibros() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libros.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibrosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libros)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libros in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLibrosWithPatch() throws Exception {
        // Initialize the database
        librosRepository.saveAndFlush(libros);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libros using partial update
        Libros partialUpdatedLibros = new Libros();
        partialUpdatedLibros.setId(libros.getId());

        partialUpdatedLibros.paginas(UPDATED_PAGINAS).numeroAlquilados(UPDATED_NUMERO_ALQUILADOS);

        restLibrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibros.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibros))
            )
            .andExpect(status().isOk());

        // Validate the Libros in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibrosUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLibros, libros), getPersistedLibros(libros));
    }

    @Test
    @Transactional
    void fullUpdateLibrosWithPatch() throws Exception {
        // Initialize the database
        librosRepository.saveAndFlush(libros);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libros using partial update
        Libros partialUpdatedLibros = new Libros();
        partialUpdatedLibros.setId(libros.getId());

        partialUpdatedLibros
            .isbn(UPDATED_ISBN)
            .stock(UPDATED_STOCK)
            .autor(UPDATED_AUTOR)
            .imagen(UPDATED_IMAGEN)
            .paginas(UPDATED_PAGINAS)
            .titulo(UPDATED_TITULO)
            .numeroAlquilados(UPDATED_NUMERO_ALQUILADOS);

        restLibrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibros.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibros))
            )
            .andExpect(status().isOk());

        // Validate the Libros in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibrosUpdatableFieldsEquals(partialUpdatedLibros, getPersistedLibros(partialUpdatedLibros));
    }

    @Test
    @Transactional
    void patchNonExistingLibros() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libros.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, libros.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(libros))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libros in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLibros() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libros.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibrosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libros))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libros in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLibros() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libros.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibrosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(libros)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libros in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLibros() throws Exception {
        // Initialize the database
        librosRepository.saveAndFlush(libros);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the libros
        restLibrosMockMvc
            .perform(delete(ENTITY_API_URL_ID, libros.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return librosRepository.count();
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

    protected Libros getPersistedLibros(Libros libros) {
        return librosRepository.findById(libros.getId()).orElseThrow();
    }

    protected void assertPersistedLibrosToMatchAllProperties(Libros expectedLibros) {
        assertLibrosAllPropertiesEquals(expectedLibros, getPersistedLibros(expectedLibros));
    }

    protected void assertPersistedLibrosToMatchUpdatableProperties(Libros expectedLibros) {
        assertLibrosAllUpdatablePropertiesEquals(expectedLibros, getPersistedLibros(expectedLibros));
    }
}
