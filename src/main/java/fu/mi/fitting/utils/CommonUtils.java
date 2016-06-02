package fu.mi.fitting.utils;

/**
 * Created by shang on 6/1/2016.
 * some useful static methods
 */
public class CommonUtils {
    private CommonUtils() {
    }

    public static int strToInt(String str, int defaultValue) {
        int res = defaultValue;
        try {
            res = Integer.parseInt(str);
        } catch (Exception ignored) {
        }
        return res;
    }

    public static double strToDouble(String str, double defaultValue) {
        double res = defaultValue;
        try {
            res = Integer.parseInt(str);
        } catch (Exception ignored) {
        }
        return res;
    }
}
