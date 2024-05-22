package my.company.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Perfil.
 */
@Entity
@Table(name = "perfil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Perfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "admin")
    private String admin;

    @Column(name = "usuario")
    private String usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Perfil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdmin() {
        return this.admin;
    }

    public Perfil admin(String admin) {
        this.setAdmin(admin);
        return this;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public Perfil usuario(String usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Perfil)) {
            return false;
        }
        return getId() != null && getId().equals(((Perfil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Perfil{" +
            "id=" + getId() +
            ", admin='" + getAdmin() + "'" +
            ", usuario='" + getUsuario() + "'" +
            "}";
    }
}
