package time;

public class Time {
    public static long time = 0;
    public static float deltaTime = 0;
    public static long timeStartup = 0;

    public static float getTimeFromStart() {
        return time - timeStartup;
    }
}
