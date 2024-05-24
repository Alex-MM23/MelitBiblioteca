package my.company.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import my.company.app.domain.Tematica;
import my.company.app.repository.TematicaRepository;
import my.company.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link my.company.app.domain.Tematica}.
 */
@RestController
@RequestMapping("/api/tematicas")
@Transactional
public class TematicaResource {

    private final Logger log = LoggerFactory.getLogger(TematicaResource.class);

    private static final String ENTITY_NAME = "tematica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TematicaRepository tematicaRepository;

    public TematicaResource(TematicaRepository tematicaRepository) {
        this.tematicaRepository = tematicaRepository;
    }

    /**
     * {@code POST  /tematicas} : Create a new tematica.
     *
     * @param tematica the tematica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tematica, or with status {@code 400 (Bad Request)} if the tematica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Tematica> createTematica(@RequestBody Tematica tematica) throws URISyntaxException {
        log.debug("REST request to save Tematica : {}", tematica);
        if (tematica.getId() != null) {
            throw new BadRequestAlertException("A new tematica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tematica = tematicaRepository.save(tematica);
        return ResponseEntity.created(new URI("/api/tematicas/" + tematica.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tematica.getId().toString()))
            .body(tematica);
    }

    /**
     * {@code PUT  /tematicas/:id} : Updates an existing tematica.
     *
     * @param id the id of the tematica to save.
     * @param tematica the tematica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tematica,
     * or with status {@code 400 (Bad Request)} if the tematica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tematica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tematica> updateTematica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Tematica tematica
    ) throws URISyntaxException {
        log.debug("REST request to update Tematica : {}, {}", id, tematica);
        if (tematica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tematica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tematicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tematica = tematicaRepository.save(tematica);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tematica.getId().toString()))
            .body(tematica);
    }

    /**
     * {@code PATCH  /tematicas/:id} : Partial updates given fields of an existing tematica, field will ignore if it is null
     *
     * @param id the id of the tematica to save.
     * @param tematica the tematica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tematica,
     * or with status {@code 400 (Bad Request)} if the tematica is not valid,
     * or with status {@code 404 (Not Found)} if the tematica is not found,
     * or with status {@code 500 (Internal Server Error)} if the tematica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tematica> partialUpdateTematica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Tematica tematica
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tematica partially : {}, {}", id, tematica);
        if (tematica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tematica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tematicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tematica> result = tematicaRepository
            .findById(tematica.getId())
            .map(existingTematica -> {
                if (tematica.getAbreviatura() != null) {
                    existingTematica.setAbreviatura(tematica.getAbreviatura());
                }
                if (tematica.getDescripcion() != null) {
                    existingTematica.setDescripcion(tematica.getDescripcion());
                }

                return existingTematica;
            })
            .map(tematicaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tematica.getId().toString())
        );
    }

    /**
     * {@code GET  /tematicas} : get all the tematicas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tematicas in body.
     */
    @GetMapping("")
    public List<Tematica> getAllTematicas() {
        log.debug("REST request to get all Tematicas");
        return tematicaRepository.findAll();
    }

    /**
     * {@code GET  /tematicas/:id} : get the "id" tematica.
     *
     * @param id the id of the tematica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tematica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tematica> getTematica(@PathVariable("id") Long id) {
        log.debug("REST request to get Tematica : {}", id);
        Optional<Tematica> tematica = tematicaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tematica);
    }

    /**
     * {@code DELETE  /tematicas/:id} : delete the "id" tematica.
     *
     * @param id the id of the tematica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTematica(@PathVariable("id") Long id) {
        log.debug("REST request to delete Tematica : {}", id);
        tematicaRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
