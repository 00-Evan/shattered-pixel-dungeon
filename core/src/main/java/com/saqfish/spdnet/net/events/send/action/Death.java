package com.saqfish.spdnet.net.events.send.action;

import com.saqfish.spdnet.actors.buffs.Bleeding;
import com.saqfish.spdnet.actors.buffs.Burning;
import com.saqfish.spdnet.actors.mobs.Mob;
import com.saqfish.spdnet.levels.features.Chasm;

public class Death {
    public String cause;

    public Death(Object cause){
        if(cause instanceof Mob) this.cause = "was killed by a " + ((Mob)cause).name();
        else if(cause instanceof Chasm) this.cause = "fell to death";
        else if(cause instanceof Bleeding) this.cause = "bled to death";
        else if(cause instanceof Burning) this.cause = "burned to death";
        else this.cause = "died";
    }
}
