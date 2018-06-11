package exceptions;

public class InvalidValueException extends ConfigurationException {
    public InvalidValueException(String value, String key) {
        super(value + ":" + key);
    }
}
