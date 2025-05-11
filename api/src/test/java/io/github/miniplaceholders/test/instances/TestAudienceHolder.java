package io.github.miniplaceholders.test.instances;

import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;

public record TestAudienceHolder(TestAudience audience) implements ForwardingAudience.Single {
    @Override
    public @NotNull TestAudience audience() {
        return audience;
    }
}
