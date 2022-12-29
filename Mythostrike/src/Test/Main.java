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
        GameManager gameManager = new GameManager();
        InputConditions inputCondition = new InputConditions();

        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.println(inputCondition.getHint());
            try {
                boolean invalid = false;
                input = scanner.nextLine();
                if (COMMAND_QUIT.equals(input)) {
                    scanner.close();
                    return;
                }
                //using limit -1 in order to avoid deleting last part with empty string
                String[] inputSplit = input.split(COMMAND_MULTIPLE_SPLIT, -1);
                Integer[] inputConvert = new Integer[inputSplit.length];
                for (int i = 0; i < inputSplit.length; i++) {
                    if (inputCondition.isAllowEmpty() && input.equals(COMMAND_EMPTY)) {
                        inputConvert[i] = EMPTY_VALUE;
                    } else {
                        inputConvert[i] = Integer.parseInt(inputSplit[i]);
                    }
                    if (inputConvert[i] > inputCondition.getRange()
                            || (inputConvert[i] <= 0 && !inputSplit[i].equals(COMMAND_EMPTY))
                            || (!inputCondition.isAllowEmpty() && inputSplit[i].equals(COMMAND_EMPTY))) {
                        invalid = true;
                        break;
                    }
                }

                if (!invalid && ((inputConvert.length == inputCondition.getAmountNumber())
                        || inputCondition.getAmountNumber() < 0)
                        && (inputCondition.isAllowDuplicate() || !arrayHasDuplicate(inputConvert))) {
                    gameManager.pushProgress(inputConvert);
                }
            } catch (NumberFormatException ignored) {
                /*do nothing literally*/
                continue;
            }
        }
        scanner.close();

    }

    public void init(){
        ArrayList<Player> players = new ArrayList<Player>();
        Mode mode = Mode.ONEVERSUSONE;

        Champion.getAllChampion().add(new Champion(ChampionData.ARES));
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
