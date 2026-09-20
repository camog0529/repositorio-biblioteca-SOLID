package co.edu.uniquindio.controlador;

import co.edu.uniquindio.dominio.ConfiguracionBiblioteca;
import co.edu.uniquindio.dominio.Libro;
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

public class BibliotecaController {

    @FXML
    private Label lblNombreBiblioteca;
    @FXML
    private Label lblDireccion;
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

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtCategoria;

    private ObservableList<Libro> listaLibros;

    private final OperacionLibro servicioPrestamo = new PrestamoLibro();
    private final OperacionLibro servicioDevolucion = new DevolucionLibro();

    @FXML
    public void initialize() {
        ConfiguracionBiblioteca config = ConfiguracionBiblioteca.getInstancia();
        lblNombreBiblioteca.setText(config.getNombre());
        lblDireccion.setText(config.getDireccion());

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        listaLibros = FXCollections.observableArrayList(
                new Libro.Builder("001", "Cien años de soledad").autor("Gabriel García Márquez").categoria("Novela").build(),
                new Libro.Builder("002", "El ingenioso hidalgo Don Quijote").autor("Miguel de Cervantes").categoria("Clásico").build(),
                new Libro.Builder("003", "Java: The Complete Reference").autor("Herbert Schildt").categoria("Tecnología").build()
        );
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
        limpiarCampos();
        mostrarAlerta("Éxito", "Libro registrado correctamente.");
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

    @FXML
    void manejarPrestarLibro() {
        Libro seleccionado = tblLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un libro para prestar.");
            return;
        }

        try {
            servicioPrestamo.procesar(seleccionado);
            tblLibros.refresh();
        } catch (IllegalStateException e) {
            mostrarAlerta("Error de Transacción", e.getMessage());
        }
    }

    @FXML
    void manejarDevolverLibro() {
        Libro seleccionado = tblLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un libro para devolver.");
            return;
        }

        try {
            servicioDevolucion.procesar(seleccionado);
            tblLibros.refresh();
        } catch (IllegalStateException e) {
            mostrarAlerta("Error de Transacción", e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtTitulo.clear();
        txtAutor.clear();
        txtCategoria.clear();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}