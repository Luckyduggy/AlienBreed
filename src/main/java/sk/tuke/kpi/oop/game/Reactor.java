package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Reactor extends AbstractActor {
    private int temperature;
    private int damage;
    private Animation normalAnimation;

    public Reactor() {
        this.temperature = 0;
        this.damage = 0;

        this.normalAnimation = new Animation(
            "sprites/reactor_on.png",
            80,
            80,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        setAnimation(normalAnimation);
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getDamage() {
        return this.damage;
    }

    public void increaseTemperature(int increment){
        if(increment <= 0) return;

        double factor = 1.0;
        if(this.damage >= 33 && this.damage <= 66) factor = 1.5;
        if(this.damage > 66) factor = 2;

        int finalIncrement = (int) Math.ceil(increment * factor);

        this.temperature += finalIncrement;

        if (this.temperature > 2000) {
            int calculatedDamage = (int) Math.floor((this.temperature - 2000) / 40.0);

            if (calculatedDamage > 100) {
                calculatedDamage = 100;
            }
            if (calculatedDamage > this.damage) {
                this.damage = calculatedDamage;
            }
        }

        if (this.temperature >= 6000) {
            Animation brokenAnimation = new Animation(
                "sprites/reactor_broken.png",
                80,
                80,
                0.1f,
                Animation.PlayMode.LOOP_PINGPONG
            );
            setAnimation(brokenAnimation);
        } else if (this.temperature >= 4000) {
            Animation hotAnimation = new Animation(
                "sprites/reactor_hot.png",
                80,
                80,
                0.05f,
                Animation.PlayMode.LOOP_PINGPONG
            );
            setAnimation(hotAnimation);
        }
    }
    public void decreaseTemperature(int decrement){
        if (decrement <= 0) return;
        if(this.damage == 100) return;


        int actualDecrement = decrement;
        if (this.damage >= 50) {
            actualDecrement = decrement / 2;
        }

        this.temperature -= actualDecrement;
        if (this.temperature < 0) {
            this.temperature = 0;
        }

        if(this.temperature <= 4000){
            Animation normalAnimation = new Animation(
                "sprites/reactor_on.png",
                80,
                80,
                0.1f,
                Animation.PlayMode.LOOP_PINGPONG
            );
            setAnimation(normalAnimation);
        }
    }
}
