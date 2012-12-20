package bootstraponline.export_sauce;


public class Time {

    /** Converts long to minutes and seconds. Does not deal with negative time. **/
    public static String longToTime(long time) {
        time = Math.abs(time);

        if (time == 1) {
            return "1 second";
        }

        if (time < 60) {
            return time + " seconds";
        }

        final long seconds = time % 60;
        final long minutes = time / 60L;
        String result = minutes + " minute";

        if (minutes > 1) {
            result += "s";
        }

        if (seconds < 1) {
            return result;
        }
        
        result += " " + seconds + " second";

        if (seconds > 1) {
            result += "s";
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(longToTime(-1)); // 1 second
        System.out.println(longToTime(0)); // 0 seconds
        System.out.println(longToTime(1)); // 1 second
        System.out.println(longToTime(59)); // 59 seconds
        System.out.println(longToTime(60)); // 1 minute
        System.out.println(longToTime(61)); // 1 minute 1 second
        System.out.println(longToTime(2523)); // 42 minutes 3 seconds
        System.out.println(longToTime(1339188276)); // 22319804 minutes 36 seconds
    }
}