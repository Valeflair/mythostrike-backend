package Core;

import java.util.ArrayList;
import java.util.Collections;

public class GameController {
    public static final int PICK_CHAMPION_COUNT = 3;
    public static final int PICK_CHAMPION_COUNT_GODKING = 5;
    private Game game;

    public GameController(Game game){
        this.game = game;
    }
    public void gameStart(ArrayList<Player> players, Mode mode){
        //only shuffle in identitymode
        if (game.getMode().equals(Mode.IDENTITYFOREIGHT) || game.getMode().equals(Mode.IDENTITYFORFIVE)){
            Collections.shuffle(players);
        }
        //player get its own identity
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setIdentity(mode.identities.get(i));
        }
        //set godking into the first place
        if (!players.get(0).getIdentity().equals(Identity.GODKING) &&
                (mode.equals(Mode.IDENTITYFORFIVE) || mode.equals(Mode.IDENTITYFOREIGHT))){
            for (int i = 0; i < players.size();i++){
                if (players.get(i).getIdentity().equals(Identity.GODKING)){
                    Player godking = players.get(i);
                    players.set(i,players.get(0));
                    players.set(0,godking);
                }
            }
        }

        ArrayList<Champion> championList = Champion.getAllChampion();
        for (Player player : players) {

            ArrayList<Champion> list = new ArrayList<Champion>();
            //ask player to pick champion
            int championCount = PICK_CHAMPION_COUNT;
            if (player.getIdentity().equals(Identity.GODKING)) {
                championCount = PICK_CHAMPION_COUNT_GODKING;
            }
            //liste aufstellen
            while (list.size() < championCount) {
                Collections.shuffle(championList);
                if (!list.contains(championList.get(0))) {
                    list.add(championList.get(0));
                }
            }
            //wahl aussuchen
            player.setChampion(championSelect(player, list));
        }

    }
    public Card judge(){
        return game.getDrawDeck().subtractCard();
    }
    public Champion championSelect(Player player, ArrayList<Champion> championList){


        return null;
    }
}
