package co.edu.uniquindio.servicio;

import co.edu.uniquindio.dominio.Libro;

public class PrestamoLibro implements OperacionLibro {
    @Override
    public void procesar(Libro libro) {
        if ("Disponible".equalsIgnoreCase(libro.getEstado())) {
            libro.setEstado("Prestado");
            System.out.println("El libro '" + libro.getTitulo() + "' ha sido prestado con éxito.");
        } else {
            throw new IllegalStateException("El libro ya se encuentra prestado o no está disponible.");
        }
    }
}