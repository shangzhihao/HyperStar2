package fu.mi.fitting.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonUtilsTest {

    @Test
    public void strToDoubleParsesDecimalValues() {
        assertEquals(12.5, CommonUtils.strToDouble("12.5", 0.0), 0.0);
        assertEquals(-0.75, CommonUtils.strToDouble("-0.75", 0.0), 0.0);
    }

    @Test
    public void strToDoubleFallsBackOnInvalidInput() {
        assertEquals(3.14, CommonUtils.strToDouble("abc", 3.14), 0.0);
    }
}
