package io.github.miniplaceholders.test.instances;

import net.kyori.adventure.audience.ForwardingAudience;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TestAudienceHolder(TestAudience audience) implements ForwardingAudience.Single {
    @Override
    public TestAudience audience() {
        return audience;
    }
}
