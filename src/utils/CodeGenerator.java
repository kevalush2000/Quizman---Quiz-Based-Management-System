package utils;

import java.util.Random;

public class CodeGenerator {
    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 4;

    public static String generateUniqueCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
