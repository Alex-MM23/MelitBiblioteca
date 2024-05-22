package my.company.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import my.company.app.domain.Usuarios;
import my.company.app.repository.UsuariosRepository;
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
 * REST controller for managing {@link my.company.app.domain.Usuarios}.
 */
@RestController
@RequestMapping("/api/usuarios")
@Transactional
public class UsuariosResource {

    private final Logger log = LoggerFactory.getLogger(UsuariosResource.class);

    private static final String ENTITY_NAME = "usuarios";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuariosRepository usuariosRepository;

    public UsuariosResource(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    /**
     * {@code POST  /usuarios} : Create a new usuarios.
     *
     * @param usuarios the usuarios to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarios, or with status {@code 400 (Bad Request)} if the usuarios has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Usuarios> createUsuarios(@RequestBody Usuarios usuarios) throws URISyntaxException {
        log.debug("REST request to save Usuarios : {}", usuarios);
        if (usuarios.getId() != null) {
            throw new BadRequestAlertException("A new usuarios cannot already have an ID", ENTITY_NAME, "idexists");
        }
        usuarios = usuariosRepository.save(usuarios);
        return ResponseEntity.created(new URI("/api/usuarios/" + usuarios.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, usuarios.getId().toString()))
            .body(usuarios);
    }

    /**
     * {@code PUT  /usuarios/:id} : Updates an existing usuarios.
     *
     * @param id the id of the usuarios to save.
     * @param usuarios the usuarios to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarios,
     * or with status {@code 400 (Bad Request)} if the usuarios is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarios couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuarios> updateUsuarios(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Usuarios usuarios
    ) throws URISyntaxException {
        log.debug("REST request to update Usuarios : {}, {}", id, usuarios);
        if (usuarios.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarios.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuariosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        usuarios = usuariosRepository.save(usuarios);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, usuarios.getId().toString()))
            .body(usuarios);
    }

    /**
     * {@code PATCH  /usuarios/:id} : Partial updates given fields of an existing usuarios, field will ignore if it is null
     *
     * @param id the id of the usuarios to save.
     * @param usuarios the usuarios to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarios,
     * or with status {@code 400 (Bad Request)} if the usuarios is not valid,
     * or with status {@code 404 (Not Found)} if the usuarios is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarios couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Usuarios> partialUpdateUsuarios(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Usuarios usuarios
    ) throws URISyntaxException {
        log.debug("REST request to partial update Usuarios partially : {}, {}", id, usuarios);
        if (usuarios.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarios.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuariosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Usuarios> result = usuariosRepository
            .findById(usuarios.getId())
            .map(existingUsuarios -> {
                if (usuarios.getUsername() != null) {
                    existingUsuarios.setUsername(usuarios.getUsername());
                }
                if (usuarios.getPassword() != null) {
                    existingUsuarios.setPassword(usuarios.getPassword());
                }
                if (usuarios.getEmail() != null) {
                    existingUsuarios.setEmail(usuarios.getEmail());
                }
                if (usuarios.getNombre() != null) {
                    existingUsuarios.setNombre(usuarios.getNombre());
                }
                if (usuarios.getApellido() != null) {
                    existingUsuarios.setApellido(usuarios.getApellido());
                }
                if (usuarios.getDireccion() != null) {
                    existingUsuarios.setDireccion(usuarios.getDireccion());
                }
                if (usuarios.getFechaAlta() != null) {
                    existingUsuarios.setFechaAlta(usuarios.getFechaAlta());
                }

                return existingUsuarios;
            })
            .map(usuariosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, usuarios.getId().toString())
        );
    }

    /**
     * {@code GET  /usuarios} : get all the usuarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarios in body.
     */
    @GetMapping("")
    public List<Usuarios> getAllUsuarios() {
        log.debug("REST request to get all Usuarios");
        return usuariosRepository.findAll();
    }

    /**
     * {@code GET  /usuarios/:id} : get the "id" usuarios.
     *
     * @param id the id of the usuarios to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarios, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> getUsuarios(@PathVariable("id") Long id) {
        log.debug("REST request to get Usuarios : {}", id);
        Optional<Usuarios> usuarios = usuariosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(usuarios);
    }

    /**
     * {@code DELETE  /usuarios/:id} : delete the "id" usuarios.
     *
     * @param id the id of the usuarios to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarios(@PathVariable("id") Long id) {
        log.debug("REST request to delete Usuarios : {}", id);
        usuariosRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
