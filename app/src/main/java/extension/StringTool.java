package extension;

import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.Entity.Player;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;

/**
 * Created by kangleif on 11/12/2015.
 */
public class StringTool {

    public static String preparePartyText(String preText){
        if(preText == null || CenterController.controller().partyGame == null){
            return preText;
        } else {
            preText = preText.replace("<moderator>", PartyGame.getModerator().name);
            String players = "";
            int index = 0;

            for (Player player : PartyGame.players) {
                if(index != PartyGame.moderatorIndex) {
                    players = players + ", " + player.name;
                }
                index++;
            }
            players = players.substring(0,players.lastIndexOf(","))+" and"+ players.substring(players.lastIndexOf(",")+1, players.length());
            players = players.substring(1);
            return preText.replaceAll("<challengers>", players);
        }
    }
}
