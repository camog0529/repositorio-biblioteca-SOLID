package co.edu.uniquindio.servicio;

import co.edu.uniquindio.dominio.Libro;

public class DevolucionLibro implements OperacionLibro {
    @Override
    public void procesar(Libro libro) {
        if ("Prestado".equalsIgnoreCase(libro.getEstado())) {
            libro.setEstado("Disponible");
            System.out.println("El libro '" + libro.getTitulo() + "' ha sido devuelto con éxito.");
        } else {
            throw new IllegalStateException("El libro no se encuentra bajo un estado de préstamo válido.");
        }
    }
}
