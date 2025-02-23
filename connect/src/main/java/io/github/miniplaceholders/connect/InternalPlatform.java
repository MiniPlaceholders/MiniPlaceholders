package io.github.miniplaceholders.connect;

public enum InternalPlatform {

    PAPER, VELOCITY,
    @Deprecated(forRemoval = true, since = "2.3.0")
    KRYPTON,
    FABRIC, SPONGE;

    private static InternalPlatform actualPlatform;

    public static void platform(final InternalPlatform platform){
        InternalPlatform.actualPlatform = platform;
    }

    public static InternalPlatform platform(){
        return InternalPlatform.actualPlatform;
    }
}
