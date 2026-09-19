package co.edu.uniquindio.dominio;

public class Libro implements Cloneable {
    private final String codigo;       // Obligatorio
    private final String titulo;       // Obligatorio
    private String autor;              // Opcional
    private String categoria;          // Opcional
    private String estado;             // Obligatorio ("Disponible" o "Prestado")

    // El constructor privado obliga a usar el Builder para crear un libro nuevo
    private Libro(Builder builder) {
        this.codigo = builder.codigo;
        this.titulo = builder.titulo;
        this.autor = builder.autor;
        this.categoria = builder.categoria;
        this.estado = builder.estado;
    }

    // Getters y Setters requeridos
    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Implementación del Patrón Prototype para clonación rápida
    @Override
    public Libro clone() {
        try {
            return (Libro) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Error al clonar el libro");
        }
    }

    // ==========================================
    // CLASE INTERNA: Patrón Builder
    // ==========================================
    public static class Builder {
        private final String codigo;
        private final String titulo;
        private String autor = "Anónimo";      // Valor por defecto si es opcional
        private String categoria = "General";   // Valor por defecto si es opcional
        private String estado = "Disponible";    // Estado inicial por defecto

        // El constructor del Builder exige los datos obligatorios
        public Builder(String codigo, String titulo) {
            this.codigo = codigo;
            this.titulo = titulo;
        }

        // Métodos fluidos para configurar los datos opcionales
        public Builder autor(String autor) {
            this.autor = autor;
            return this;
        }

        public Builder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public Builder estado(String estado) {
            this.estado = estado;
            return this;
        }

        // Método final que fabrica el objeto Libro real
        public Libro build() {
            return new Libro(this);
        }
    }
}