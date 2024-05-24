package my.company.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Pedidos.
 */
@Entity
@Table(name = "pedidos")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pedidos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "fecha_alta")
    private Instant fechaAlta;

    @Column(name = "fecha_entrega")
    private Instant fechaEntrega;

    @Column(name = "username")
    private String username;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pedidos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Pedidos direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Instant getFechaAlta() {
        return this.fechaAlta;
    }

    public Pedidos fechaAlta(Instant fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(Instant fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Instant getFechaEntrega() {
        return this.fechaEntrega;
    }

    public Pedidos fechaEntrega(Instant fechaEntrega) {
        this.setFechaEntrega(fechaEntrega);
        return this;
    }

    public void setFechaEntrega(Instant fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getUsername() {
        return this.username;
    }

    public Pedidos username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedidos)) {
            return false;
        }
        return getId() != null && getId().equals(((Pedidos) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pedidos{" +
            "id=" + getId() +
            ", direccion='" + getDireccion() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaEntrega='" + getFechaEntrega() + "'" +
            ", username='" + getUsername() + "'" +
            "}";
    }
}
