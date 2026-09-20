package co.edu.uniquindio.controlador;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BibliotecaApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargamos el archivo FXML desde la carpeta de recursos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BibliotecaVista.fxml"));
            Parent root = loader.load();

            // Configuramos la escena y el título de la ventana
            primaryStage.setTitle("Sistema de Gestión de Biblioteca - SOLID");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz gráfica: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
