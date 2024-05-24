package my.company.app.web.rest;

import static my.company.app.domain.PedidosAsserts.*;
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
import my.company.app.domain.Pedidos;
import my.company.app.repository.PedidosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PedidosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PedidosResourceIT {

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_ALTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ALTA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_ENTREGA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ENTREGA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pedidos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPedidosMockMvc;

    private Pedidos pedidos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedidos createEntity(EntityManager em) {
        Pedidos pedidos = new Pedidos()
            .direccion(DEFAULT_DIRECCION)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaEntrega(DEFAULT_FECHA_ENTREGA)
            .username(DEFAULT_USERNAME);
        return pedidos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedidos createUpdatedEntity(EntityManager em) {
        Pedidos pedidos = new Pedidos()
            .direccion(UPDATED_DIRECCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .username(UPDATED_USERNAME);
        return pedidos;
    }

    @BeforeEach
    public void initTest() {
        pedidos = createEntity(em);
    }

    @Test
    @Transactional
    void createPedidos() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pedidos
        var returnedPedidos = om.readValue(
            restPedidosMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidos)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Pedidos.class
        );

        // Validate the Pedidos in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPedidosUpdatableFieldsEquals(returnedPedidos, getPersistedPedidos(returnedPedidos));
    }

    @Test
    @Transactional
    void createPedidosWithExistingId() throws Exception {
        // Create the Pedidos with an existing ID
        pedidos.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidos)))
            .andExpect(status().isBadRequest());

        // Validate the Pedidos in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        // Get all the pedidosList
        restPedidosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedidos.getId().intValue())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(DEFAULT_FECHA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)));
    }

    @Test
    @Transactional
    void getPedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        // Get the pedidos
        restPedidosMockMvc
            .perform(get(ENTITY_API_URL_ID, pedidos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pedidos.getId().intValue()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaEntrega").value(DEFAULT_FECHA_ENTREGA.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME));
    }

    @Test
    @Transactional
    void getNonExistingPedidos() throws Exception {
        // Get the pedidos
        restPedidosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidos
        Pedidos updatedPedidos = pedidosRepository.findById(pedidos.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPedidos are not directly saved in db
        em.detach(updatedPedidos);
        updatedPedidos
            .direccion(UPDATED_DIRECCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .username(UPDATED_USERNAME);

        restPedidosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPedidos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPedidos))
            )
            .andExpect(status().isOk());

        // Validate the Pedidos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPedidosToMatchAllProperties(updatedPedidos);
    }

    @Test
    @Transactional
    void putNonExistingPedidos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidosMockMvc
            .perform(put(ENTITY_API_URL_ID, pedidos.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidos)))
            .andExpect(status().isBadRequest());

        // Validate the Pedidos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPedidos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedidos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPedidos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedidos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePedidosWithPatch() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidos using partial update
        Pedidos partialUpdatedPedidos = new Pedidos();
        partialUpdatedPedidos.setId(pedidos.getId());

        partialUpdatedPedidos.direccion(UPDATED_DIRECCION).fechaEntrega(UPDATED_FECHA_ENTREGA);

        restPedidosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedidos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedidos))
            )
            .andExpect(status().isOk());

        // Validate the Pedidos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidosUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPedidos, pedidos), getPersistedPedidos(pedidos));
    }

    @Test
    @Transactional
    void fullUpdatePedidosWithPatch() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidos using partial update
        Pedidos partialUpdatedPedidos = new Pedidos();
        partialUpdatedPedidos.setId(pedidos.getId());

        partialUpdatedPedidos
            .direccion(UPDATED_DIRECCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .username(UPDATED_USERNAME);

        restPedidosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedidos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedidos))
            )
            .andExpect(status().isOk());

        // Validate the Pedidos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidosUpdatableFieldsEquals(partialUpdatedPedidos, getPersistedPedidos(partialUpdatedPedidos));
    }

    @Test
    @Transactional
    void patchNonExistingPedidos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pedidos.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pedidos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedidos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPedidos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedidos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPedidos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pedidos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedidos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pedidos
        restPedidosMockMvc
            .perform(delete(ENTITY_API_URL_ID, pedidos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pedidosRepository.count();
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

    protected Pedidos getPersistedPedidos(Pedidos pedidos) {
        return pedidosRepository.findById(pedidos.getId()).orElseThrow();
    }

    protected void assertPersistedPedidosToMatchAllProperties(Pedidos expectedPedidos) {
        assertPedidosAllPropertiesEquals(expectedPedidos, getPersistedPedidos(expectedPedidos));
    }

    protected void assertPersistedPedidosToMatchUpdatableProperties(Pedidos expectedPedidos) {
        assertPedidosAllUpdatablePropertiesEquals(expectedPedidos, getPersistedPedidos(expectedPedidos));
    }
}
