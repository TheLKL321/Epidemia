package exceptions;

import java.io.IOException;

// Potomkowie tej klasy służą do zgłaszania odpowiednich wyjątków podczas czytania danych z plików
public class ConfigurationException extends IOException {
    ConfigurationException(String message) {
        super(message);
    }

    ConfigurationException(){

    }
}
