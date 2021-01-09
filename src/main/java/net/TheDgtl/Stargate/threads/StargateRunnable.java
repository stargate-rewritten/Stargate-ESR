package net.TheDgtl.Stargate.threads;

import java.util.Objects;
import net.TheDgtl.Stargate.Stargate;
import org.jetbrains.annotations.NotNull;

public abstract class StargateRunnable implements Runnable {

    protected final Stargate stargate;

    public StargateRunnable(@NotNull Stargate stargate) {
        this.stargate = Objects.requireNonNull(stargate);
    }

    @NotNull
    public Stargate getStargate() {
        return stargate;
    }
}
