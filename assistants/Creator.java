package assistants;

import world.World;
import agent.Agent;
import agent.NormalAgent;
import agent.SocialAgent;

import java.util.*;

public class Creator {

    public static LinkedHashMap<Agent, HashSet<Agent>> createNet(long seed, int population, int averageFriends, double percentageSocial){
        LinkedHashMap<Agent, HashSet<Agent>> result = new LinkedHashMap<>();

        Random random = new Random(seed);

        Agent patientZero;
        if (random.nextDouble() < percentageSocial)
            patientZero = new SocialAgent(true, 1);
        else
            patientZero = new NormalAgent(true, 1);
        result.put(patientZero, new HashSet<>());

        for (int i = 1; i < population; i++) {
            Agent tempAgent;
            if (random.nextDouble() < percentageSocial)
                tempAgent = new SocialAgent(false, i + 1);
            else
                tempAgent = new NormalAgent(false, i + 1);
            result.put(tempAgent, new HashSet<>());
        }

        return rollFriendships(result, population, averageFriends);
    }

    private static LinkedHashMap<Agent, HashSet<Agent>> rollFriendships(LinkedHashMap<Agent, HashSet<Agent>> result, int population, int averageFriends){
        Random random = World.getInstance().getRandom();
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
