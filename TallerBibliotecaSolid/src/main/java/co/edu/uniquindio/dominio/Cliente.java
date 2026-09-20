package co.edu.uniquindio.dominio;

import java.time.LocalDate;

public class Cliente {
    private final String cedula;
    private final String nombre;
    private final String telefono;
    private final String direccion;

    // Estos campos nos permitirán saber qué libro tiene y cuándo debe devolverlo
    private Libro libroPrestado;
    private LocalDate fechaPrestamo;
    private LocalDate fechaMaxEntrega;

    public Cliente(String cedula, String nombre, String telefono, String direccion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y Setters necesarios para el TableView de JavaFX
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }

    public Libro getLibroPrestado() { return libroPrestado; }
    public void setLibroPrestado(Libro libroPrestado) { this.libroPrestado = libroPrestado; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaMaxEntrega() { return fechaMaxEntrega; }
    public void setFechaMaxEntrega(LocalDate fechaMaxEntrega) { this.fechaMaxEntrega = fechaMaxEntrega; }
}
