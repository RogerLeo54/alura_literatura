package com.alura.literatura.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "autores")

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String anioNacido;
    private String anioMuerte;
    @ManyToMany(mappedBy = "autores")
    private Set<Libro> libros = new HashSet<>();

    public Autor(){}

    public Autor (DatosAutor autor){
        this.nombre = autor.nombre();
        this.anioNacido = autor.anioNacido();
        this.anioMuerte = autor.anioMuerte();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAnioNacido() {
        return anioNacido;
    }

    public void setAnioNacido(String anioNacido) {
        this.anioNacido = anioNacido;
    }

    public String getAnioMuerte() {
        return anioMuerte;
    }

    public void setAnioMuerte(String anioMuerte) {
        this.anioMuerte = anioMuerte;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", anioNacido='" + anioNacido + '\'' +
                ", anioMuerte='" + anioMuerte + '\'' +
                ", libros=" + libros;
    }
}
