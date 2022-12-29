package Test;

import Core.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class Main {

    private static final String ERROR_UTILITY_CLASS_INSTANTIATION = "Utility class cannot be instantiated.";
    private static final String ERROR_NOT_ALLOW_ARGUMENT_IN_MAIN = "Error, Main class doesn't require any parameter!";

    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_MULTIPLE_SPLIT = ",";
    private static final String COMMAND_EMPTY = "";
    private static final int EMPTY_VALUE = 0;

    public static void main(String[] args){
        if (args.length != 0) {
            System.out.println((ERROR_NOT_ALLOW_ARGUMENT_IN_MAIN));
            return;
        }
        /*
         *------------------------------------------------------------------------
         *-------------------------------test part--------------------------------
         *------------------------------------------------------------------------
         */
        Mode mode = Mode.ONEVERSUSONE;

        //initial players
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Jack"));
        players.add(new Player("Minh"));

        //intial champs
        ArrayList<Champion> champions = new ArrayList<Champion>();
        for (ChampionData data: ChampionData.values()) {
            champions.add(new Champion(data));
        }
        //set default champ cause singleton
        Champion.setChampionPatterns(champions);

        //--------------------------------initialize---------------------------------

        Game game = new Game(players, mode);
        GameController gc = game.getGameController();
        gc.gameStart();
    }

    public static ArrayList<Card> askForCard(Player player, CardSpace targetSpace, int min, int max, boolean optional, String reason){
        outputPlayerIsOn(player);
        System.out.println(reason);
        System.out.println("you have to pick "+min+"~"+max+"Card from Space:"+"reason:" + reason +"\n"+"CardSpace:");
        if (optional){
            System.out.println("you can ");
        }

        displayCardList(targetSpace);
        InputConditions conditions = new InputConditions();
        conditions.update(min,max,0,targetSpace.getSum()-1,reason,false,optional);
        Integer[] pick = readNext(conditions);
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < pick.length; i++){
            cards.add(targetSpace.getCards().get(pick[i]));
        }
        return cards;
    }
    public static Champion championSelect(Player player, ArrayList<Champion> championList){
        outputPlayerIsOn(player);
        for (int i = 0; i < championList.size() ; i++){
            Champion champion = championList.get(i);
            System.out.print("Champ"+i+":"+ champion.getName()+", Skills:");
            for (Skill skill : champion.getSkills()){
                System.out.print("," + skill.getName()+":"+skill.getDescription());
            }
            System.out.println();
        }
        InputConditions conditions = new InputConditions();
        conditions.update(1,1,0,championList.size()-1,"Select your champion",false,false);
        Integer[] pick = readNext(conditions);

        return championList.get(pick[0]);
    }
    public static boolean askForConfirm(Player player, String reason){
        outputPlayerIsOn(player);
        System.out.println(reason);
        InputConditions conditions = new InputConditions();
        conditions.update(1,1,0,1,"0 for no and 1 for yes",false, false);
        Integer[] pick = readNext(conditions);
        return pick[0] == 1;
    }



    private static void outputPlayerIsOn(Player player){
        System.out.println("--------Player:" + player.getName() + " on Action--------");
    }
    private static Integer[] readNext(InputConditions conditions) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (true) {
            System.out.println(conditions.getAllowance());
            try {
                boolean invalid = false;
                input = scanner.nextLine();

                String[] inputSplit = input.split(COMMAND_MULTIPLE_SPLIT, -1);
                Integer[] inputConvert = new Integer[inputSplit.length];
                for (int i = 0; i < inputSplit.length; i++) {
                    if (conditions.isAllowEmpty() && input.equals(COMMAND_EMPTY)) {
                        inputConvert[i] = EMPTY_VALUE;
                    } else {
                        inputConvert[i] = Integer.parseInt(inputSplit[i]);
                    }
                    if (inputConvert[i] > conditions.getRangeMax()
                            || inputConvert[i] < conditions.getRangeMin()
                            || (inputConvert[i] <= 0 && !inputSplit[i].equals(COMMAND_EMPTY))
                            || (!conditions.isAllowEmpty() && inputSplit[i].equals(COMMAND_EMPTY))) {
                        invalid = true;
                    }
                }

                if (!invalid && ((inputConvert.length >= conditions.getAmountMin())
                        && inputConvert.length <= conditions.getAmountMax())
                        && (conditions.isAllowDuplicate() || !arrayHasDuplicate(inputConvert))) {
                    return inputConvert;
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
    private static void displayCardList(CardSpace cardSpace){
        for (int i = 0; i < cardSpace.getCards().size(); i++){
            System.out.print(i+"th Card:" + cardToString(cardSpace.getCards().get(i)));
        }
        System.out.println();
    }
    private static String cardToString(Card card){
        return card.getName() + ":" +card.getSymbol() + card.getPoint();
    }
    private static <T> boolean arrayHasDuplicate(T[] input) {
        HashSet<T> checkSet = new HashSet<>();
        for (T object : input) {
            if (!checkSet.add(object)) {
                return true;
            }
        }
        return false;
    }
}
