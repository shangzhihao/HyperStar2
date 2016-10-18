package fu.mi.fitting.utils;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by shang on 6/1/2016.
 * some useful static methods
 */
public class CommonUtils {
    private CommonUtils() {
    }

    public static int strToInt(final String str, final int defaultValue) {
        int res = defaultValue;
        try {
            res = Integer.parseInt(str);
        } catch (Exception ignored) {
        }
        return res;
    }

    public static double strToDouble(final String str, final double defaultValue) {
        double res = defaultValue;
        try {
            res = Integer.parseInt(str);
        } catch (Exception ignored) {
        }
        return res;
    }

    public static String matrixToString(final RealMatrix matrix) {
        StringBuilder res = new StringBuilder();
        int row = matrix.getRowDimension();
        int col = matrix.getColumnDimension();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                res.append(String.format("%.4f,", matrix.getEntry(i, j)));
            }
            res.deleteCharAt(res.length()-1);
            res.append("\n");
        }
        return res.toString();
    }

    public static String vectorToString(final RealVector vector) {
        StringBuilder res = new StringBuilder();
        int dim = vector.getDimension();
        for (int i = 0; i < dim; i++) {
            res.append(String.format("%.4f,", vector.getEntry(i)));
        }
        res.deleteCharAt(res.length()-1);
        return res.toString();
    }
}
