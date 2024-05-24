package my.company.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Libros.
 */
@Entity
@Table(name = "libros")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Libros implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "isbn")
    private Integer isbn;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "autor")
    private String autor;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "paginas")
    private Integer paginas;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "numero_alquilados")
    private Integer numeroAlquilados;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Libros id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsbn() {
        return this.isbn;
    }

    public Libros isbn(Integer isbn) {
        this.setIsbn(isbn);
        return this;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Libros stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getAutor() {
        return this.autor;
    }

    public Libros autor(String autor) {
        this.setAutor(autor);
        return this;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getImagen() {
        return this.imagen;
    }

    public Libros imagen(String imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getPaginas() {
        return this.paginas;
    }

    public Libros paginas(Integer paginas) {
        this.setPaginas(paginas);
        return this;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Libros titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroAlquilados() {
        return this.numeroAlquilados;
    }

    public Libros numeroAlquilados(Integer numeroAlquilados) {
        this.setNumeroAlquilados(numeroAlquilados);
        return this;
    }

    public void setNumeroAlquilados(Integer numeroAlquilados) {
        this.numeroAlquilados = numeroAlquilados;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Libros)) {
            return false;
        }
        return getId() != null && getId().equals(((Libros) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Libros{" +
            "id=" + getId() +
            ", isbn=" + getIsbn() +
            ", stock=" + getStock() +
            ", autor='" + getAutor() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", paginas=" + getPaginas() +
            ", titulo='" + getTitulo() + "'" +
            ", numeroAlquilados=" + getNumeroAlquilados() +
            "}";
    }
}
