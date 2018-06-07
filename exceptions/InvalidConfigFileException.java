package exceptions;

public class InvalidConfigFileException extends ConfigurationException {
    public InvalidConfigFileException(String message) {
        super(message);
    }

    public InvalidConfigFileException(){

    }
}
