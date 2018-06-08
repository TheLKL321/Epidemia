package world;

import agent.Agent;
import agent.Meeting;
import assistants.Reporter;

import java.util.*;

// świat symulacji - trzyma dane dotyczące choroby, agentów, spotkań i przeprowadza kolejne dni symulacji
public class World {
    private static World instance = null;
    private Random random;
    private double meetingProb, infectiousness, recoverability, mortality;
    private int duration, currentDay = 1;
    private Reporter reporter;
    private Map<Agent, HashSet<Agent>> net;
    private ArrayList<LinkedList<Meeting>> timeline = new ArrayList<>();

    private World(){
        // służy do zapobiegania powstawaniu instancji
    }

    public static void create(Random random, double meetingProb, double infectiousness, double recoverability, double mortality, Reporter reporter, Map<Agent, HashSet<Agent>> net, int duration){
        if (instance == null) {
            instance = new World();
            instance.random = random;
            instance.meetingProb = meetingProb;
            instance.infectiousness = infectiousness;
            instance.recoverability = recoverability;
            instance.mortality = mortality;
            instance.reporter = reporter;
            instance.net = net;
            instance.duration = duration;
            for (int i = 0; i < duration; i++)
                instance.timeline.add(new LinkedList<>());
        }
    }

    public static World getInstance(){
        return instance;
    }

    public void start(){
        reporter.noteDayHeadline();
        while (currentDay <= duration) {
            revolve();
            reporter.noteDay();
            this.currentDay++;
        }
    }

    private void revolve(){
        cullTheWeak();
        saveTheStrong();

        for (Agent agent : net.keySet())
            agent.keepMeeting();

        for (Meeting meeting : timeline.get(currentDay - 1))
            meeting.conductMeeting();
    }

    private void cullTheWeak(){
        Set<Agent> naughtyList = new HashSet<>();

        for (Agent agent : net.keySet()) {
            if (agent.ifInfected() && random.nextDouble() < mortality)
                naughtyList.add(agent);
        }

        for (Agent agent : naughtyList) {
            killAgent(agent);
        }
    }

    private void killAgent(Agent agent){
        reporter.reportDeath();

        net.remove(agent);
        for (HashSet<Agent> friends : net.values())
            friends.remove(agent);

        for (LinkedList<Meeting> meetings : timeline.subList(currentDay, timeline.size())) {
            Set<Meeting> cancelled = new HashSet<>();

            for (Meeting meeting : meetings) {
                if (meeting.contains(agent))
                    cancelled.add(meeting);
            }

            for (Meeting meeting : cancelled)
                meetings.remove(meeting);
        }
    }

    private void saveTheStrong(){
        for (Agent agent : net.keySet()){
            if (agent.ifInfected() && random.nextDouble() < recoverability)
                agent.pullThrough();
        }
    }

    public void addMeeting(Meeting meeting, int when){
        this.timeline.get(when).add(meeting);
    }

    public Reporter getReporter() {
        return reporter;
    }

    public Random getRandom() {
        return random;
    }

    public double getMeetingProb() {
        return meetingProb;
    }

    public double getInfectiousness() {
        return infectiousness;
    }

    public Map<Agent, HashSet<Agent>> getNet() {
        return net;
    }

    public int getCurrentDay(){
        return currentDay;
    }

    public int getDuration(){
        return duration;
    }
}
