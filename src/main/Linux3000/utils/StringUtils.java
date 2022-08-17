package main.Linux3000.utils;

public class StringUtils {

    public static String repeatString(String s, int h) {
        String g = "";
        for(int i = 0; i<h; i++) {
            g+= s;
        }
        return g;
    }

}
