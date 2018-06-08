package assistants;

import agent.Agent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Reporter {
    private StringBuilder sb = new StringBuilder();
    private int currentHealthy, currentInfected, currentImmune;
    private String reportPath;

    public Reporter(String reportPath, Properties config) {
        this.reportPath = reportPath;
        noteInitialState(config);
        this.currentHealthy = Integer.parseInt(config.getProperty("liczbaAgentów")) - 1;
        this.currentInfected = 1;
        this.currentImmune = 0;
    }

    public void reportInfection(){
        this.currentHealthy--;
        this.currentInfected++;
    }

    public void reportDeath(){
        this.currentInfected--;
    }

    public void reportRecovery(){
        this.currentInfected--;
        this.currentImmune++;
    }

    private void noteInitialState(Properties config){
        sb.append("# twoje wyniki powinny zawierać te komentarze\n");
        for (String token : config.stringPropertyNames()) {
            sb.append(token).append("=").append(config.getProperty(token)).append("\n");
        }
        sb.append("\n");
    }

    public void noteInitialAgentInfo(Set<Agent> agents){
        sb.append("# agenci jako: id typ lub id* typ dla chorego\n");
        for (Agent agent : agents) {
            sb.append(agent.getId());
            if (agent.ifInfected())
                sb.append("*");
            sb.append(" ").append(agent).append("\n");
        }
        sb.append("\n");
    }

    public void noteInitialNet(HashMap<Agent, HashSet<Agent>> net){
        sb.append("# graf\n");
        for (Agent agent : net.keySet()) {
            sb.append(agent.getId()).append(" ");
            for (Agent friend : net.get(agent)) {
                sb.append(friend.getId()).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");
    }

    public void noteDayHeadline(){
        sb.append("# liczność w kolejnych dniach\n");
    }

    public void noteDay(){
        sb.append(currentHealthy).append(" ").append(currentInfected).append(" ").append(currentImmune).append("\n");
    }

    public void giveReport(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath))){
            bw.write(sb.toString());
        } catch (IOException e) {
            // Nie dojdzie do tego gdyż plik był tworzony już wcześniej, w funkcji Parser.parse()
            e.printStackTrace();
        }
    }
}
