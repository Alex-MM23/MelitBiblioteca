package my.company.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import my.company.app.domain.Libros;
import my.company.app.repository.LibrosRepository;
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
 * REST controller for managing {@link my.company.app.domain.Libros}.
 */
@RestController
@RequestMapping("/api/libros")
@Transactional
public class LibrosResource {

    private final Logger log = LoggerFactory.getLogger(LibrosResource.class);

    private static final String ENTITY_NAME = "libros";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LibrosRepository librosRepository;

    public LibrosResource(LibrosRepository librosRepository) {
        this.librosRepository = librosRepository;
    }

    /**
     * {@code POST  /libros} : Create a new libros.
     *
     * @param libros the libros to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new libros, or with status {@code 400 (Bad Request)} if the libros has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Libros> createLibros(@RequestBody Libros libros) throws URISyntaxException {
        log.debug("REST request to save Libros : {}", libros);
        if (libros.getId() != null) {
            throw new BadRequestAlertException("A new libros cannot already have an ID", ENTITY_NAME, "idexists");
        }
        libros = librosRepository.save(libros);
        return ResponseEntity.created(new URI("/api/libros/" + libros.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, libros.getId().toString()))
            .body(libros);
    }

    /**
     * {@code PUT  /libros/:id} : Updates an existing libros.
     *
     * @param id the id of the libros to save.
     * @param libros the libros to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libros,
     * or with status {@code 400 (Bad Request)} if the libros is not valid,
     * or with status {@code 500 (Internal Server Error)} if the libros couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libros> updateLibros(@PathVariable(value = "id", required = false) final Long id, @RequestBody Libros libros)
        throws URISyntaxException {
        log.debug("REST request to update Libros : {}, {}", id, libros);
        if (libros.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libros.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!librosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        libros = librosRepository.save(libros);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, libros.getId().toString()))
            .body(libros);
    }

    /**
     * {@code PATCH  /libros/:id} : Partial updates given fields of an existing libros, field will ignore if it is null
     *
     * @param id the id of the libros to save.
     * @param libros the libros to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libros,
     * or with status {@code 400 (Bad Request)} if the libros is not valid,
     * or with status {@code 404 (Not Found)} if the libros is not found,
     * or with status {@code 500 (Internal Server Error)} if the libros couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Libros> partialUpdateLibros(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Libros libros
    ) throws URISyntaxException {
        log.debug("REST request to partial update Libros partially : {}, {}", id, libros);
        if (libros.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libros.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!librosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Libros> result = librosRepository
            .findById(libros.getId())
            .map(existingLibros -> {
                if (libros.getIsbn() != null) {
                    existingLibros.setIsbn(libros.getIsbn());
                }
                if (libros.getStock() != null) {
                    existingLibros.setStock(libros.getStock());
                }
                if (libros.getAutor() != null) {
                    existingLibros.setAutor(libros.getAutor());
                }
                if (libros.getImagen() != null) {
                    existingLibros.setImagen(libros.getImagen());
                }
                if (libros.getPaginas() != null) {
                    existingLibros.setPaginas(libros.getPaginas());
                }
                if (libros.getTitulo() != null) {
                    existingLibros.setTitulo(libros.getTitulo());
                }
                if (libros.getNumeroAlquilados() != null) {
                    existingLibros.setNumeroAlquilados(libros.getNumeroAlquilados());
                }
                if (libros.getIdTematica() != null) {
                    existingLibros.setIdTematica(libros.getIdTematica());
                }

                return existingLibros;
            })
            .map(librosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, libros.getId().toString())
        );
    }

    /**
     * {@code GET  /libros} : get all the libros.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of libros in body.
     */
    @GetMapping("")
    public List<Libros> getAllLibros() {
        log.debug("REST request to get all Libros");
        return librosRepository.findAll();
    }

    /**
     * {@code GET  /libros/:id} : get the "id" libros.
     *
     * @param id the id of the libros to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the libros, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Libros> getLibros(@PathVariable("id") Long id) {
        log.debug("REST request to get Libros : {}", id);
        Optional<Libros> libros = librosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(libros);
    }

    /**
     * {@code DELETE  /libros/:id} : delete the "id" libros.
     *
     * @param id the id of the libros to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibros(@PathVariable("id") Long id) {
        log.debug("REST request to delete Libros : {}", id);
        librosRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
