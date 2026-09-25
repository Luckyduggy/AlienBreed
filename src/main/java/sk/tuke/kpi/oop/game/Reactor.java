package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Reactor extends AbstractActor {
    private int temperature;
    private int damage;
    private boolean state;

    private Light light;

    private final Animation normalAnimation;
    private final Animation hotAnimation;
    private final Animation brokenAnimation;
    private final Animation offAnimation;

    public Reactor() {
        this.temperature = 0;
        this.damage = 0;
        this.state = false;

        this.normalAnimation = new Animation("sprites/reactor_on.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        this.hotAnimation = new Animation("sprites/reactor_hot.png", 80, 80, 0.05f, Animation.PlayMode.LOOP_PINGPONG);
        this.brokenAnimation = new Animation("sprites/reactor_broken.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        this.offAnimation = new Animation("sprites/reactor.png");

        updateAnimation();
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getDamage() {
        return this.damage;
    }

    private void updateAnimation() {
        if (this.damage >= 100) {
            setAnimation(brokenAnimation);
        } else if (!this.state) {
            setAnimation(offAnimation);
        } else if (this.temperature >= 4000) {
            setAnimation(hotAnimation);
        } else {
            setAnimation(normalAnimation);
        }
        updateLight();
    }
    private void updateLight(){
        if(this.light != null){
            boolean reactorWorking = isRunning() && this.damage < 100;
            this.light.setElectricityFlow(reactorWorking);
        }
    }

    public void increaseTemperature(int increment){
        if (!isRunning()) return;
        if(increment <= 0) return;

        double factor = 1.0;
        if(this.damage >= 33 && this.damage <= 66) factor = 1.5;
        if(this.damage > 66) factor = 2;

        int finalIncrement = (int) Math.ceil(increment * factor);

        this.temperature += finalIncrement;

        if (this.temperature > 2000) {
            int finalDamage = (int) Math.floor((this.temperature - 2000) / 40.0);

            if (finalDamage > 100) {
                finalDamage = 100;
            }
            if (finalDamage > this.damage) {
                this.damage = finalDamage;
            }
        }
        if (this.temperature >= 6000) {
            this.state = false;
        }
        updateAnimation();
    }
    public void decreaseTemperature(int decrement){
        if (decrement <= 0) return;
        if(this.damage == 100) return;


        int finalDecrement = decrement;
        if (this.damage >= 50) {
            finalDecrement = decrement / 2;
        }

        this.temperature -= finalDecrement;
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
    public void repeirWith(Hammer hammer){
        if(hammer == null) return;
        if(this.damage == 0 || this.damage == 100) return;

        hammer.use();
        this.damage -= 50;

        if(this.damage < 0) this.damage = 0;

        this.temperature = 2000 + (this.damage * 40);

        updateAnimation();
    }

    public void turnOn(){
        if(this.damage < 100){
            this.state = true;
            updateAnimation();
        }
    }
    public void turnOff(){
        this.state = false;
        updateAnimation();
    }
    public boolean isRunning(){
        return this.state;
    }

    public void addLight(Light light){
        if(this.light == null) {
            this.light = light;
            updateLight();
        }
    }
    public void removeLight(Light light){
        if(this.light == light && light != null){
            this.light.setElectricityFlow(false);
            this.light = null;
        }
    }
}
