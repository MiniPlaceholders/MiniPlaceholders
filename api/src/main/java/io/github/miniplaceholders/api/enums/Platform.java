package io.github.miniplaceholders.api.enums;

/**Platforms on which MiniPlaceholders can be used */
public enum Platform {
    /**Paper Platform */
    PAPER,
    /**Velocity Platform */
    VELOCITY,
    /**Krypton Platform */
    @Deprecated(forRemoval = true, since = "1.3.0")
    KRYPTON,
    /**Fabric Platform */
    FABRIC,
    /**Sponge Platform */
    SPONGE
}
