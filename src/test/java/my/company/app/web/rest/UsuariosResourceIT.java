package my.company.app.web.rest;

import static my.company.app.domain.UsuariosAsserts.*;
import static my.company.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import my.company.app.IntegrationTest;
import my.company.app.domain.Usuarios;
import my.company.app.repository.UsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsuariosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuariosResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_ALTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ALTA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuariosMockMvc;

    private Usuarios usuarios;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuarios createEntity(EntityManager em) {
        Usuarios usuarios = new Usuarios()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .direccion(DEFAULT_DIRECCION)
            .fechaAlta(DEFAULT_FECHA_ALTA);
        return usuarios;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuarios createUpdatedEntity(EntityManager em) {
        Usuarios usuarios = new Usuarios()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .direccion(UPDATED_DIRECCION)
            .fechaAlta(UPDATED_FECHA_ALTA);
        return usuarios;
    }

    @BeforeEach
    public void initTest() {
        usuarios = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuarios() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Usuarios
        var returnedUsuarios = om.readValue(
            restUsuariosMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarios)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Usuarios.class
        );

        // Validate the Usuarios in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUsuariosUpdatableFieldsEquals(returnedUsuarios, getPersistedUsuarios(returnedUsuarios));
    }

    @Test
    @Transactional
    void createUsuariosWithExistingId() throws Exception {
        // Create the Usuarios with an existing ID
        usuarios.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarios)))
            .andExpect(status().isBadRequest());

        // Validate the Usuarios in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuariosRepository.saveAndFlush(usuarios);

        // Get all the usuariosList
        restUsuariosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarios.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())));
    }

    @Test
    @Transactional
    void getUsuarios() throws Exception {
        // Initialize the database
        usuariosRepository.saveAndFlush(usuarios);

        // Get the usuarios
        restUsuariosMockMvc
            .perform(get(ENTITY_API_URL_ID, usuarios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuarios.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUsuarios() throws Exception {
        // Get the usuarios
        restUsuariosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsuarios() throws Exception {
        // Initialize the database
        usuariosRepository.saveAndFlush(usuarios);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarios
        Usuarios updatedUsuarios = usuariosRepository.findById(usuarios.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUsuarios are not directly saved in db
        em.detach(updatedUsuarios);
        updatedUsuarios
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .direccion(UPDATED_DIRECCION)
            .fechaAlta(UPDATED_FECHA_ALTA);

        restUsuariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuarios.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUsuarios))
            )
            .andExpect(status().isOk());

        // Validate the Usuarios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUsuariosToMatchAllProperties(updatedUsuarios);
    }

    @Test
    @Transactional
    void putNonExistingUsuarios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarios.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarios.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuarios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuarios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarios.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usuarios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuarios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuarios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarios.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuariosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarios)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuarios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuariosWithPatch() throws Exception {
        // Initialize the database
        usuariosRepository.saveAndFlush(usuarios);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarios using partial update
        Usuarios partialUpdatedUsuarios = new Usuarios();
        partialUpdatedUsuarios.setId(usuarios.getId());

        partialUpdatedUsuarios.password(UPDATED_PASSWORD).fechaAlta(UPDATED_FECHA_ALTA);

        restUsuariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuarios))
            )
            .andExpect(status().isOk());

        // Validate the Usuarios in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuariosUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUsuarios, usuarios), getPersistedUsuarios(usuarios));
    }

    @Test
    @Transactional
    void fullUpdateUsuariosWithPatch() throws Exception {
        // Initialize the database
        usuariosRepository.saveAndFlush(usuarios);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarios using partial update
        Usuarios partialUpdatedUsuarios = new Usuarios();
        partialUpdatedUsuarios.setId(usuarios.getId());

        partialUpdatedUsuarios
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .direccion(UPDATED_DIRECCION)
            .fechaAlta(UPDATED_FECHA_ALTA);

        restUsuariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuarios))
            )
            .andExpect(status().isOk());

        // Validate the Usuarios in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuariosUpdatableFieldsEquals(partialUpdatedUsuarios, getPersistedUsuarios(partialUpdatedUsuarios));
    }

    @Test
    @Transactional
    void patchNonExistingUsuarios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarios.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuarios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuarios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuarios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarios.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuarios))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuarios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuarios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarios.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuariosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(usuarios)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuarios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuarios() throws Exception {
        // Initialize the database
        usuariosRepository.saveAndFlush(usuarios);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the usuarios
        restUsuariosMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuarios.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return usuariosRepository.count();
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

    protected Usuarios getPersistedUsuarios(Usuarios usuarios) {
        return usuariosRepository.findById(usuarios.getId()).orElseThrow();
    }

    protected void assertPersistedUsuariosToMatchAllProperties(Usuarios expectedUsuarios) {
        assertUsuariosAllPropertiesEquals(expectedUsuarios, getPersistedUsuarios(expectedUsuarios));
    }

    protected void assertPersistedUsuariosToMatchUpdatableProperties(Usuarios expectedUsuarios) {
        assertUsuariosAllUpdatablePropertiesEquals(expectedUsuarios, getPersistedUsuarios(expectedUsuarios));
    }
}
