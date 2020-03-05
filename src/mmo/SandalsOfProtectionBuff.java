package mmo;

import mmo.events.ICurseCastEventListener;

public class SandalsOfProtectionBuff implements IBuff, ICurseCastEventListener {

    @Override
    public void buff(final IMunchkin munchkin) {
        munchkin.addBuff(this);
    }

    @Override
    public void debuff(final IMunchkin munchkin) {
        munchkin.removeBuff(this);
    }

    @Override
    public CurseCastEvent handleEvent(final CurseCastEvent event) {
        return event;
    }
}
