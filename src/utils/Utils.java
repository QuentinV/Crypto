package utils;

import java.math.BigInteger;

public class Utils {
    public static BigInteger convertString(String s)
    {
        return new BigInteger(s.getBytes());
    }

    public static String convertToString(BigInteger b)
    {
        return new String(b.toByteArray());
    }
}
