package sps.core;

public class Core {
    public static final String Non_Player = "Non_Player";
    public static final String Friendly = "Friendly";
    public static final String Player = "Player";

    public static final String Non_Free = "Non_Free";
    public static final String Free = "Free";
    public static final String All = "All";

    public static final String Particle = "Particle";
    public static final String Debug = "Debug";
    public static final String Default_Text = "Default_Text";

    public static final float SpriteRadius = (float) Math.sqrt(Math.pow(Settings.get().spriteHeight / 2, 2) + Math.pow(Settings.get().spriteWidth, 2));
    public static final int AnimationFps = 20;
}