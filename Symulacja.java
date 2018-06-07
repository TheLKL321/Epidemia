import assistants.Parser;
import exceptions.*;
import world.World;

public class Symulacja {

    public static void main(String[] args) {
        try {
            Parser.parse();
            World.getInstance().start();
            World.getInstance().getReporter().giveReport();
        } catch (NoDefaultPropertiesException e) {
            System.out.println("Brak pliku default.properties");
        } catch (InvalidDefaultPropertiesException e){
            System.out.println("default.properties nie jest tekstowy");
        } catch (NoConfigFileException e){
            System.out.println("Brak pliku simulation-conf.xml");
        } catch (InvalidConfigFileException e){
            System.out.println("simulation-conf.xml nie jest XML");
        } catch (NoValueException e){
            System.out.println("Brak wartości dla klucza " + e.getMessage());
        } catch (ConfigurationException e){
            // must be InvalidValueException
            String[] values = e.getMessage().split(":", 2);
            System.out.println("Niedozwolona wartość " + values[1] + " dla klucza " + values[0]);
        }

    }
}
