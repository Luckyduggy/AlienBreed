package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor {
    private boolean isOn;
    private boolean isPowered;

    private final Animation lightOn;
    private final Animation lightOff;

    public Light() {
        this.isOn = false;
        this.isPowered = false;

        this.lightOn = new Animation("sprites/light_on.png");
        this.lightOff = new Animation("sprites/light_off.png");

        updateAnimation();
    }

    public void toggle() {
        this.isOn = !this.isOn;
        updateAnimation();
    }
    public void setElectricityFlow(boolean isPowered) {
        this.isPowered = isPowered;
        updateAnimation();
    }
    private void updateAnimation() {
        if (this.isOn && this.isPowered) {
            setAnimation(lightOn);
        } else {
            setAnimation(lightOff);
        }
    }
}
