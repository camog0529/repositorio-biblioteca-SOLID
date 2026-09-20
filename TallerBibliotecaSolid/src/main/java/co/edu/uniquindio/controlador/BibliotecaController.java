package co.edu.uniquindio.controlador;

import co.edu.uniquindio.dominio.ConfiguracionBiblioteca;
import co.edu.uniquindio.dominio.Libro;
import co.edu.uniquindio.dominio.Cliente; // Importamos el nuevo modelo puro
import co.edu.uniquindio.servicio.DevolucionLibro;
import co.edu.uniquindio.servicio.OperacionLibro;
import co.edu.uniquindio.servicio.PrestamoLibro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class BibliotecaController {

    @FXML
    private Label lblNombreBiblioteca;
    @FXML
    private Label lblDireccion;

    // TABLA PRINCIPAL: Totalidad de libros de la biblioteca (Muestra su estado actual)
    @FXML
    private TableView<Libro> tblLibros;
    @FXML
    private TableColumn<Libro, String> colCodigo;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAutor;
    @FXML
    private TableColumn<Libro, String> colCategoria;
    @FXML
    private TableColumn<Libro, String> colEstado;

    // TEXTFIELDS DEL LIBRO (Para registro)
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtCategoria;

    // === NUEVOS COMPONENTES: TEXTFIELDS DEL CLIENTE (Ingresados por el Administrativo) ===
    @FXML
    private TextField txtCedulaCliente;
    @FXML
    private TextField txtNombreCliente;
    @FXML
    private TextField txtTelefonoCliente;
    @FXML
    private TextField txtDireccionCliente;

    // LISTAS OBSERVABLES (Colecciones en memoria compartida por la interfaz)
    private ObservableList<Libro> listaLibros;

    // Listado de préstamos activos para saber a quién se le prestó cada ejemplar
    private ObservableList<Cliente> listaClientesConPrestamo;

    @FXML
    public void initialize() {
        // Carga de configuración única (Singleton)
        ConfiguracionBiblioteca config = ConfiguracionBiblioteca.getInstancia();
        lblNombreBiblioteca.setText(config.getNombre());
        lblDireccion.setText(config.getDireccion());

        // Mapeo de columnas de la tabla de libros
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Inicialización de colecciones globales
        listaLibros = FXCollections.observableArrayList(
                new Libro.Builder("001", "Cien años de soledad").autor("Gabriel García Márquez").categoria("Novela").build(),
                new Libro.Builder("002", "El ingenioso hidalgo Don Quijote").autor("Miguel de Cervantes").categoria("Clásico").build(),
                new Libro.Builder("003", "Java: The Complete Reference").autor("Herbert Schildt").categoria("Tecnología").build()
        );

        listaClientesConPrestamo = FXCollections.observableArrayList();

        tblLibros.setItems(listaLibros);
    }

    @FXML
    void manejarRegistrarLibro() {
        String codigo = txtCodigo.getText();
        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        String categoria = txtCategoria.getText();

        if (codigo.isEmpty() || titulo.isEmpty()) {
            mostrarAlerta("Error", "El código y el título son obligatorios.");
            return;
        }

        Libro.Builder builder = new Libro.Builder(codigo, titulo);
        if (!autor.isEmpty()) builder.autor(autor);
        if (!categoria.isEmpty()) builder.categoria(categoria);

        Libro nuevoLibro = builder.build();
        listaLibros.add(nuevoLibro);
        limpiarCamposLibro();
        mostrarAlerta("Éxito", "Libro registrado correctamente en el inventario.");
    }

    @FXML
    void manejarClonarLibro() {
        Libro seleccionado = tblLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un libro de la tabla para clonar.");
            return;
        }

        Libro clon = seleccionado.clone();
        Libro libroClonado = new Libro.Builder(clon.getCodigo() + "-COPIA", clon.getTitulo() + " (Copia)")
                .autor(clon.getAutor())
                .categoria(clon.getCategoria())
                .estado("Disponible")
                .build();

        listaLibros.add(libroClonado);
        mostrarAlerta("Éxito", "Libro clonado con éxito.");
    }

    /**
     * Captura los datos ingresados del cliente, toma el libro seleccionado y ejecuta el préstamo.
     * Guarda el registro en la lista de préstamos activos (A quién se le prestó).
     */
    @FXML
    void manejarPrestarLibro() {
        Libro libroSeleccionado = tblLibros.getSelectionModel().getSelectedItem();

        // 1. Validar selección del libro
        if (libroSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor seleccione un libro disponible de la lista.");
            return;
        }

        // 2. Capturar y validar datos del Cliente de los TextFields
        String cedula = txtCedulaCliente.getText();
        String nombre = txtNombreCliente.getText();
        String telefono = txtTelefonoCliente.getText();
        String direccion = txtDireccionCliente.getText();

        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Campos Requeridos", "El administrativo debe ingresar todos los datos del cliente para procesar la transacción.");
            return;
        }

        try {
            // 3. Crear el cliente y delegar el proceso al servicio correspondiente (Cumple SRP)
            Cliente cliente = new Cliente(cedula, nombre, telefono, direccion);
            OperacionLibro servicioPrestamo = new PrestamoLibro(cliente);

            servicioPrestamo.procesar(libroSeleccionado);

            // 4. Registrar en nuestra lista de control interno de préstamos
            listaClientesConPrestamo.add(cliente);

            // 5. Refrescar tabla visual para ver el cambio inmediato a "Prestado"
            tblLibros.refresh();
            limpiarCamposCliente();

            mostrarAlerta("Préstamo Exitoso", "El libro ha sido asignado a " + cliente.getNombre() + ".\n" +
                    "Fecha límite de entrega (3 días de plazo): " + cliente.getFechaMaxEntrega());

        } catch (IllegalStateException e) {
            mostrarAlerta("Error de Transacción", e.getMessage());
        }
    }

    /**
     * Busca al cliente que posee el libro seleccionado y procesa la devolución.
     * Evalúa automáticamente de forma interna si hay una multa de $5,000 por día de retraso.
     */
    @FXML
    void manejarDevolverLibro() {
        Libro libroSeleccionado = tblLibros.getSelectionModel().getSelectedItem();

        if (libroSeleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione el libro que va a ser devuelto.");
            return;
        }

        // Buscar qué cliente de nuestra lista tiene prestado ese libro en específico
        Cliente clienteAsociado = null;
        for (Cliente c : listaClientesConPrestamo) {
            if (c.getLibroPrestado() != null && c.getLibroPrestado().getCodigo().equals(libroSeleccionado.getCodigo())) {
                clienteAsociado = c;
                break;
            }
        }

        if (clienteAsociado == null) {
            mostrarAlerta("Error", "No se encontró ningún registro de cliente asociado a este préstamo.");
            return;
        }

        try {
            // Delegar cálculo de multas y actualización al servicio (Cumple SRP)
            OperacionLibro servicioDevolucion = new DevolucionLibro(clienteAsociado);
            servicioDevolucion.procesar(libroSeleccionado);

            // Remover de la lista de préstamos activos ya que fue devuelto
            listaClientesConPrestamo.remove(clienteAsociado);

            // Refrescar la interfaz gráfica
            tblLibros.refresh();

            mostrarAlerta("Devolución Exitosa", "El libro '" + libroSeleccionado.getTitulo() + "' está disponible de nuevo.\n" +
                    "Revise la consola de IntelliJ para verificar si el cliente acumuló alguna multa por mora.");

        } catch (IllegalStateException e) {
            mostrarAlerta("Error de Transacción", e.getMessage());
        }
    }

    private void limpiarCamposLibro() {
        txtCodigo.clear();
        txtTitulo.clear();
        txtAutor.clear();
        txtCategoria.clear();
    }

    private void limpiarCamposCliente() {
        txtCedulaCliente.clear();
        txtNombreCliente.clear();
        txtTelefonoCliente.clear();
        txtDireccionCliente.clear();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    // Método Getter por si necesitas exponer la lista de préstamos a otra vista o componente
    public ObservableList<Cliente> getListaClientesConPrestamo() {
        return listaClientesConPrestamo;
    }
}