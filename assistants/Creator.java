package assistants;

import agent.Agent;
import agent.NormalAgent;
import agent.SocialAgent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;

// Służy do losowania grafu znajomości
class Creator {

    // Tworzy i zwraca losowy graf znajomości według podanych zmiennych
    static LinkedHashMap<Agent, HashSet<Agent>> createNet(Random random, int population, int averageFriends, double percentageSocial){
        LinkedHashMap<Agent, HashSet<Agent>> result = new LinkedHashMap<>();

        int patientZero = random.nextInt(population) + 1;
        for (int i = 1; i <= population; i++) {
            Agent tempAgent;
            if (random.nextDouble() < percentageSocial)
                tempAgent = new SocialAgent(i == patientZero, i);
            else
                tempAgent = new NormalAgent(i == patientZero, i);
            result.put(tempAgent, new HashSet<>());
        }

        return rollFriendships(random, result, population, averageFriends);
    }

    // Losuje znajomości (krawędzie) w podanym grafie
    private static LinkedHashMap<Agent, HashSet<Agent>> rollFriendships(Random random, LinkedHashMap<Agent, HashSet<Agent>> result, int population, int averageFriends){
        int totalFriendships = (averageFriends * population)/2;
        ArrayList<Agent> agentList = new ArrayList<>(result.keySet());

        for (int i = 0; i < totalFriendships; i++) {
            Agent agent1 = agentList.get(random.nextInt(agentList.size()));
            ArrayList<Agent> possibleFriends = new ArrayList<>(agentList);
            possibleFriends.removeAll(result.get(agent1));
            possibleFriends.remove(agent1);

            Agent agent2 = possibleFriends.get(random.nextInt(possibleFriends.size()));

            result.get(agent1).add(agent2);
            result.get(agent2).add(agent1);

            if (result.get(agent1).size() == population - 1)
                agentList.remove(agent1);

            if (result.get(agent2).size() == population - 1)
                agentList.remove(agent2);
        }

        return result;
    }
}
