package com.alura.literatura.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private List<String> idiomas;
    private String numeroDeDescargas;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "libro_autor", joinColumns = @JoinColumn(name = "libro_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id", referencedColumnName = "id")
    )
    private Set<Autor> autores = new HashSet<>();

    public Libro(){} // Por defecto

    public Libro(DatosLibros d){
        this.titulo = d.titulo();
        this.idiomas = d.idiomas();
        this.numeroDeDescargas = d.numeroDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public String getNumeroDeDesargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDesargas(String numeroDeDesargas) {
        this.numeroDeDescargas = numeroDeDesargas;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", idiomas=" + idiomas +
                ", numeroDeDesargas=" + numeroDeDescargas +
                ", autores=" + autores;
    }
}