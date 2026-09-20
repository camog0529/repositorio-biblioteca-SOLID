package co.edu.uniquindio;

import co.edu.uniquindio.controlador.BibliotecaApp;

public class Main {
    public static void main(String[] args) {
        // Al llamar al main desde una clase que NO hereda de Application,
        // JavaFX arranca sin validar el módulo ni tirar el error de componentes
        BibliotecaApp.main(args);
    }
}
