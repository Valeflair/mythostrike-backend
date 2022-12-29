package Test;

import Core.*;
import Events.Event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Function;

public class test {


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
            String o1 = "1";
            int o2 = 3;
            Boolean o3 = true;
            ArrayList<Object> objects = new ArrayList<Object>();
            objects.add(o1);
            objects.add(o2);
            objects.add(o3);

            Function<ArrayList<Object>, Boolean> function = new Function<ArrayList<Object>, Boolean>() {
                @Override
                public Boolean apply(ArrayList<Object> args) {
                    String o1 = (String) args.get(0);
                    int o2 = (int) args.get(1);
                    Boolean o3 = (Boolean) args.get(2);
                    return o3;
                }
            };
            System.out.println(function.apply(objects));

        Skill skillA = new Skill("drawOneMore", "zieht eine Karte mehr", true, new Function<Object, Boolean>() {
            @Override
            public Boolean apply(Object o) {
                System.out.println("Core.Skill A invoke!");
                return true;
            }
        Skill revenge = new TriggerSkill("Revenge", Event.DAMAGED, "asdasdasd", true, new Function<Object, Boolean>() {
            @Override
            public Boolean apply(EventArgs args) {

                Player victim = (Player)eventArgs[0];
                Player dealer = (Player)eventArgs[1];
                Damage damage = (Damage)eventArgs[2];
                if(askForSkillInvoke(player, skill)){
                    Card judgeCard = gameController.judge();
                    if(judgeCard.getSymbol() != CardSymbol.HEART){
                        if(askForCard(dealer, 2)){
                            throwCard();
                        } else {
                            applyDmage(new Damage(victim, dealer, 1,"Revenge",DamageType.NORMAL));
                        }
                    }
                }
            }
        });

    }
}
