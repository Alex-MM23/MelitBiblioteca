package my.company.app.repository;

import my.company.app.domain.Tematica;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tematica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TematicaRepository extends JpaRepository<Tematica, Long> {}
