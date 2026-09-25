package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class FireExtinguisher extends AbstractActor{
    private int uses;

    public FireExtinguisher(){
        this(4);
    }

    public FireExtinguisher(int uses){
        this.uses = uses;
        Animation animation = new Animation("sprites/extinguisher.png");
        setAnimation(animation);
    }

    public int getUses(){
        return this.uses;
    }
    public void use(){
        if(this.uses <= 0) return;

        this.uses--;

        if(this.uses == 0 && getScene() != null){
            getScene().removeActor(this);
        }
    }
}
