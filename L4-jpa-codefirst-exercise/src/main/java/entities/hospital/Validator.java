package entities.hospital;

public class Validator {

    public static void validateString(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException("You must enter not empty string! null value is not acceptable too!");
        }
    }


}
