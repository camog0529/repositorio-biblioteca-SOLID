package co.edu.uniquindio.servicio;

import co.edu.uniquindio.dominio.Libro;
import co.edu.uniquindio.dominio.Cliente;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DevolucionLibro implements OperacionLibro {

    private final Cliente cliente;
    private static final double MULTA_DIARIA = 5000.0;

    public DevolucionLibro(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void procesar(Libro libro) {
        if (libro == null || !"Prestado".equalsIgnoreCase(libro.getEstado())) {
            throw new IllegalStateException("El libro no se encuentra bajo un estado de préstamo válido.");
        }

        // 1. Liberar el libro en la biblioteca
        libro.setEstado("Disponible");

        // 2. Calcular la multa usando la fecha del sistema (Simulando el día de hoy)
        LocalDate fechaDevolucionReal = LocalDate.now();
        long diasDeRetraso = ChronoUnit.DAYS.between(cliente.getFechaMaxEntrega(), fechaDevolucionReal);

        System.out.println("==========================================");
        System.out.println("Procesando devolución de: " + libro.getTitulo());

        if (diasDeRetraso > 0) {
            double multaTotal = diasDeRetraso * MULTA_DIARIA;
            System.out.println("⚠️ RETRASO DETECTADO: " + diasDeRetraso + " días.");
            System.out.println("💰 Multa acumulada a pagar: $" + multaTotal + " COP");
        } else {
            System.out.println("✅ Devuelto a tiempo. ¡Sin multas!");
        }

        // 3. Limpiar los datos de préstamo del cliente
        cliente.setLibroPrestado(null);
        cliente.setFechaPrestamo(null);
        cliente.setFechaMaxEntrega(null);
        System.out.println("==========================================");
    }
}
