package co.edu.uniquindio.dominio;

public class ConfiguracionBiblioteca {
    private static ConfiguracionBiblioteca instancia;
    private String nombre;
    private String direccion;
    private double porcentajeMulta;

    // El constructor privado evita que se creen instancias con 'new' desde fuera
    private ConfiguracionBiblioteca() {
        this.nombre = "Biblioteca Universitaria UQ";
        this.direccion = "Carrera 15 Calle 12N - Armenia";
        this.porcentajeMulta = 0.05; // 5% de multa por defecto
    }

    // Punto de acceso global único para la instancia
    public static synchronized ConfiguracionBiblioteca getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionBiblioteca();
        }
        return instancia;
    }

    // Getters y Setters para modificar o consultar los datos globales
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getPorcentajeMulta() {
        return porcentajeMulta;
    }

    public void setPorcentajeMulta(double porcentajeMulta) {
        this.porcentajeMulta = porcentajeMulta;
    }
}

