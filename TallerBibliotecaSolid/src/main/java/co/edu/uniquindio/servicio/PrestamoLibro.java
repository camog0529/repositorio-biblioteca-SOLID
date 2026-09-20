package co.edu.uniquindio.servicio;

import co.edu.uniquindio.dominio.Libro;

import co.edu.uniquindio.dominio.Cliente;

import java.time.LocalDate;

public class PrestamoLibro implements OperacionLibro {

    private final Cliente cliente;

    // El administrativo pasa el cliente capturado desde los TextFields de la vista
    public PrestamoLibro(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void procesar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("Debe seleccionar un libro válido.");
        }

        if ("Disponible".equalsIgnoreCase(libro.getEstado())) {
            // 1. Cambiar el estado del libro (Actualización inmediata)
            libro.setEstado("Prestado");

            // 2. Asignar los datos del préstamo al cliente usando la fecha del sistema
            LocalDate fechaSistema = LocalDate.now();
            cliente.setLibroPrestado(libro);
            cliente.setFechaPrestamo(fechaSistema);
            cliente.setFechaMaxEntrega(fechaSistema.plusDays(3)); // Regla de los 3 días

            System.out.println("✅ Préstamo procesado con éxito para: " + cliente.getNombre());
            System.out.println("Fecha límite de entrega sin multa: " + cliente.getFechaMaxEntrega());
        } else {
            throw new IllegalStateException("El libro '" + libro.getTitulo() + "' ya se encuentra prestado.");
        }
    }
}