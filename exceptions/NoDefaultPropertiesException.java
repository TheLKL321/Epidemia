package exceptions;

public class NoDefaultPropertiesException extends ConfigurationException {
    public NoDefaultPropertiesException(String message) {
        super(message);
    }

    public NoDefaultPropertiesException(){

    }
}
