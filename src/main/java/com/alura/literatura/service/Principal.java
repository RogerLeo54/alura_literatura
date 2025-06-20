package com.alura.literatura.service;

import com.alura.literatura.model.*;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import org.hibernate.LazyInitializationException;

import java.util.*;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository repository, AutorRepository autorRepository){
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    public void showMenu(){
        var flag = true;
        var mensaje =  """
                Elija una opción a través de su número:
                \t1 - Buscar libro por titúlo
                \t2 - Listar libros registrados
                \t3 - Listar autores registrados
                \t4 - Lista autores vivos en un determinado año
                \t5 - Listar libros por idioma
                \t0 - Salir
                """;

        while (flag){
            System.out.println(mensaje);
            int option = Integer.parseInt(scanner.nextLine());

            switch (option){
                case 1:
                    agregaLibrosYAutores();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnDeterminadoAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    salir();
                    flag = false;
                    break;
                default:
                    System.out.println("La opción seleccionada no es valida!");
                    break;
            }
        }
    }

    public DatosLibros getLibrosPorNombre(){
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var buscarLibro = scanner.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE.concat("?search=").concat(buscarLibro.replace(" ", "%20")));
        var datos = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libros = datos.resultados().stream()
                .filter(d -> d.titulo().toLowerCase()
                        .contains(buscarLibro.toLowerCase()))
                .findFirst();

        return libros.orElse(null);
    }

    public void agregaLibrosYAutores(){
        var buscarLibro = getLibrosPorNombre();

        // Valida que la API retorne información
        if(buscarLibro != null){
            // Traemos todos los libros registrados en la base de datos
            List<Libro> libros = repository.findAll();

            // Compara si el libro buscado es igual al registrado en la base de datos
            Optional<Libro> libro = libros.stream()
                    .filter(l->l.getTitulo().toLowerCase()
                            .contains(buscarLibro.titulo().toLowerCase()))
                    .findFirst();

            if(libro.isPresent()){
                System.out.println("Libro ya esta registrado en la base de datos! ");
            }else{

                // Obtiene nombre del autor del libro buscado
                var nombreAutor = buscarLibro.autores().stream().map(DatosAutor::nombre).findFirst().get();


                // Obtener todos los autores de la api
                List<Autor> autoresApi = buscarLibro.autores().stream()
                        .map(Autor::new)
                        .toList();

                //
                List<Libro> buscarAutores = repository.findAll(); // Retorna autores de la db
                Optional<Libro> autorBuscado = libros.stream()
                        .filter(l -> l.getAutores().stream()
                                .anyMatch(autor -> autor.getNombre().toLowerCase().contains(nombreAutor.toLowerCase())))
                        .findFirst();

                // Válida si el autor ya existe en la base de datos
                Libro saveLibro = new Libro(buscarLibro);
                if (autorBuscado.isPresent()){
                    // Autor existe
                    Autor autor = autorRepository.findByNombre(nombreAutor);
                    saveLibro.setAutores(new HashSet<>(Collections.singleton(autor)));
                    repository.save(saveLibro);
                }else{
                    // Autor no existe

                    System.out.println("Guardando autor");
                    saveLibro.setAutores(new HashSet<>(autoresApi)); // Asigna los autores al libro
                    repository.save(saveLibro); // Guarda el libro y la relación
                }
            }
        }else{
            System.out.println("El libro no existe!");
        }
    }

    public void listarLibrosRegistrados(){

        try{
            List<Libro> libros = repository.findAll();

            libros.stream().forEach(l ->
                    System.out.println(
                            "\n---LIBRO---"+
                                    "\nTitulo: "+ l.getTitulo()+
                                    "\nAutor: " + l.getAutores().stream()
                                    .map(a-> new DatosAutor(a.getNombre(), a.getAnioNacido(), a.getAnioMuerte()))
                                    .toList().stream().map(datos -> datos.nombre()).findFirst().get()+
                                    "\nIdioma " + l.getIdiomas()+
                                    "\nNúmero de descargas: " + l.getNumeroDeDesargas()));

        }catch (LazyInitializationException e){
            System.out.println(e);
        }

    }

    public void listarAutoresRegistrados(){
        System.out.println("--- AUTORES ---");
        List<Autor> autores = autorRepository.findAll();

        autores.stream().forEach(a ->
                System.out.println(
                        "--- AUTOR ---" +
                                "\nAutor: " + a.getNombre()+
                                "\nAño nacimiento: " + a.getAnioNacido()+
                                "\nAño fallecimiento: " + a.getAnioMuerte()
                ));


    }

    public void listarAutoresVivosEnDeterminadoAnio() {
        System.out.println("Ingrese el año para buscar autores que estaban vivos:");
        var anio = scanner.nextLine();

        List<Autor> autores = autorRepository.findAll();

        List<Autor> autoresVivos = autores.stream()
                .filter(a -> {
                    try {
                        int nacimiento = Integer.parseInt(a.getAnioNacido());
                        int muerte = a.getAnioMuerte() == null || a.getAnioMuerte().isEmpty()
                                ? Integer.MAX_VALUE
                                : Integer.parseInt(a.getAnioMuerte());

                        int anioConsulta = Integer.parseInt(anio);
                        return nacimiento <= anioConsulta && anioConsulta <= muerte;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .toList();

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año.");
        } else {
            System.out.println("\n--- AUTORES VIVOS EN " + anio + " ---");
            autoresVivos.forEach(a -> System.out.println(
                    "\nNombre: " + a.getNombre() +
                            "\nAño de nacimiento: " + a.getAnioNacido() +
                            "\nAño de fallecimiento: " + (a.getAnioMuerte().isEmpty() ? "Vive o sin dato" : a.getAnioMuerte())
            ));
        }
    }

    public void listarLibrosPorIdioma() {
        System.out.println("Ingrese el código del idioma (ej: en, es, fr): ");
        var idioma = scanner.nextLine();

        List<Libro> libros = repository.findAll();

        List<Libro> librosFiltrados = libros.stream()
                .filter(l -> l.getIdiomas().contains(idioma))
                .toList();

        if (librosFiltrados.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma.");
        } else {
            System.out.println("\n--- LIBROS EN IDIOMA: " + idioma + " ---");
            librosFiltrados.forEach(l -> System.out.println(
                    "\nTítulo: " + l.getTitulo() +
                            "\nAutor(es): " + l.getAutores().stream()
                            .map(Autor::getNombre)
                            .reduce((a1, a2) -> a1 + ", " + a2).orElse("Desconocido") +
                            "\nIdiomas: " + l.getIdiomas() +
                            "\nNúmero de descargas: " + l.getNumeroDeDesargas()
            ));
        }
    }


    public void salir(){
        System.out.println("Saliendo del sistema...");
    }
}
