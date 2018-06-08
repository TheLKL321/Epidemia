package assistants;

import world.World;
import agent.Agent;
import exceptions.*;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Służy do czytania danych z wymaganych plików i stworzenia instancji World na ich podstawie
public class Parser {

    // Czyta pliki simulation-conf i default i tworzy świat
    public static void parse() throws ConfigurationException {
        Properties defaultProperties = new Properties();
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String defaultPath = rootPath + "default.properties";
        String configPath = rootPath + "simulation-conf.xml";

        try (FileInputStream stream = new FileInputStream(defaultPath);
             Reader reader = Channels.newReader(stream.getChannel(), StandardCharsets.UTF_8.name())) {
            defaultProperties.load(reader);
        } catch (MalformedInputException e) {
            throw new InvalidDefaultPropertiesException();
        } catch (IOException e) {
            throw new NoDefaultPropertiesException();
        }

        Properties config = new Properties(defaultProperties);

        try (FileInputStream stream = new FileInputStream(configPath)){
            config.loadFromXML(stream);
        } catch (InvalidValueException e) {
            throw new InvalidConfigFileException();
        } catch (IOException e) {
            throw new NoConfigFileException();
        }

        if (config.size() < 11) {
            String[] keys = {"seed", "liczbaAgentów", "liczbaDni", "śrZnajomych", "prawdTowarzyski", "prawdSpotkania",
                    "prawdZarażenia", "prawdZarażenia", "prawdWyzdrowienia", "śmiertelność", "plikZRaportem"};
            for (String token : keys) {
                if (config.getProperty(token) == null)
                    throw new NoValueException(token);
            }
        }

        long seed;
        int population, duration, averageFriends;
        double percentageSocial, meetingProb, infectiousness, recoverability, mortality;
        String reportPath;

        try {
            seed = Long.parseLong(config.getProperty("seed"));
        } catch (NumberFormatException e){
            throw new InvalidValueException("seed:" + config.getProperty("seed"));
        }

        try {
            population = Integer.parseInt(config.getProperty("liczbaAgentów"));
            if (population > 1000000 || population < 1)
                throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new InvalidValueException("liczbaAgentów:" + config.getProperty("liczbaAgentów"));
        }

        try {
            duration = Integer.parseInt(config.getProperty("liczbaDni"));
            if (duration > 1000 || duration < 1)
                throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new InvalidValueException("liczbaDni:" + config.getProperty("liczbaDni"));
        }

        try {
            averageFriends = Integer.parseInt(config.getProperty("śrZnajomych"));
            if (averageFriends >= population || averageFriends < 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new InvalidValueException("śrZnajomych:" + config.getProperty("śrZnajomych"));
        }

        try {
            percentageSocial = Double.parseDouble(config.getProperty("prawdTowarzyski"));
            if (percentageSocial > 1 || percentageSocial < 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new InvalidValueException("prawdTowarzyski:" + config.getProperty("prawdTowarzyski"));
        }

        try {
            meetingProb = Double.parseDouble(config.getProperty("prawdSpotkania"));
            if (meetingProb >= 1 || meetingProb < 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new InvalidValueException("prawdSpotkania:" + config.getProperty("prawdSpotkania"));
        }

        try {
            infectiousness = Double.parseDouble(config.getProperty("prawdZarażenia"));
            if (infectiousness > 1 || infectiousness < 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new InvalidValueException("prawdZarażenia:" + config.getProperty("prawdZarażenia"));
        }

        try {
            recoverability = Double.parseDouble(config.getProperty("prawdWyzdrowienia"));
            if (recoverability > 1 || recoverability < 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new InvalidValueException("prawdWyzdrowienia:" + config.getProperty("prawdWyzdrowienia"));
        }

        try {
            mortality = Double.parseDouble(config.getProperty("śmiertelność"));
            if (mortality > 1 || mortality < 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new InvalidValueException("śmiertelność:" + config.getProperty("śmiertelność"));
        }

        reportPath = config.getProperty("plikZRaportem");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath))){
            bw.write("[RESERVATION]");
        } catch (IOException e) {
            throw new InvalidValueException("plikZRaportem:" + reportPath);
        }

        Random random = new Random(seed);
        LinkedHashMap<Agent, HashSet<Agent>> net = Creator.createNet(random, population, averageFriends, percentageSocial);
        Reporter reporter = new Reporter(reportPath, config);
        reporter.noteInitialAgentInfo(net.keySet());
        reporter.noteInitialNet(net);

        World.create(random, meetingProb, infectiousness, recoverability, mortality, reporter, net, duration);
    }
}
