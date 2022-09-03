public class Conversion {
    public Conversion() {}

    public static int stringBinaryToInt(String str) {
        int number;
        number = Integer.parseInt(str, 2);
        return number;
    }

    public static String intToStringBinary(int number) {
        String str;
        str = Integer.toBinaryString(number);
        return  str;
    }
}