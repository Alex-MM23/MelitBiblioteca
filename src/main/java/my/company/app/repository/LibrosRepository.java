package my.company.app.repository;

import my.company.app.domain.Libros;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Libros entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LibrosRepository extends JpaRepository<Libros, Long> {}
