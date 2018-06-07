package exceptions;

public class NoConfigFileException extends ConfigurationException {
    public NoConfigFileException(String message) {
        super(message);
    }

    public NoConfigFileException(){

    }
}
