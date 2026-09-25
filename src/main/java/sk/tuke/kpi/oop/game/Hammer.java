package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Hammer extends AbstractActor {
    private int uses;


    public Hammer(){
        this(1);
    }

    public Hammer(int uses){
        this.uses = uses;
        Animation animation = new Animation("sprites/hammer.png");
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
