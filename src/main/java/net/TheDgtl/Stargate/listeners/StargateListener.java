package net.TheDgtl.Stargate.listeners;

import java.util.Objects;
import net.TheDgtl.Stargate.Stargate;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public abstract class StargateListener implements Listener {

    protected final Stargate stargate;

    public StargateListener(@NotNull Stargate stargate) {
        this.stargate = Objects.requireNonNull(stargate);
    }

    @NotNull
    public final Stargate getStargate() {
        return stargate;
    }
}
