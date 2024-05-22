package my.company.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Tematica.
 */
@Entity
@Table(name = "tematica")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tematica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "abreviatura")
    private String abreviatura;

    @Column(name = "descripcion")
    private String descripcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tematica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return this.abreviatura;
    }

    public Tematica abreviatura(String abreviatura) {
        this.setAbreviatura(abreviatura);
        return this;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Tematica descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tematica)) {
            return false;
        }
        return getId() != null && getId().equals(((Tematica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tematica{" +
            "id=" + getId() +
            ", abreviatura='" + getAbreviatura() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
