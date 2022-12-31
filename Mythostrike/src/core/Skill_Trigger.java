package core;

import events.FunctionObserver;

public class Skill_Trigger extends Skill {
    FunctionObserver listener;

    public Skill_Trigger(TriggerSkillData data){
        super(data);
        listener = data.getFunctionListener();
    }

    public void initialEvent(){
    }

    public FunctionObserver getListener() {
        return listener;
    }

    public void setListener(FunctionObserver listener) {
        this.listener = listener;
    }
}
