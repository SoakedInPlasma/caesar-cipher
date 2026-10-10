import java.util.Scanner;

public class CaesarCipher {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Secret Message Encryptor ===");
        System.out.println("Lock a message with a secret key. Only someone with the same key can unlock it.");

        while (running) {
            System.out.println();
            System.out.println("1. Encrypt a message");
            System.out.println("2. Decrypt a message");
            System.out.println("3. Show how it works, step by step");
            System.out.println("4. Exit");
            System.out.print("> ");

            String choice = input.nextLine().trim();

            if (choice.equals("1")) {
                String message = readMessage(input, "Enter the message to encrypt: ");
                String key = readKey(input);
                System.out.println("Encrypted: " + encrypt(message, key));

            } else if (choice.equals("2")) {
                String message = readMessage(input, "Enter the encrypted message: ");
                String key = readKey(input);
                System.out.println("Decrypted: " + decrypt(message, key));
                System.out.println("(If this looks like nonsense, the key was wrong.)");

            } else if (choice.equals("3")) {
                String message = readMessage(input, "Enter a message: ");
                String key = readKey(input);
                showSteps(message, key);

            } else if (choice.equals("4")) {
                running = false;
                System.out.println("Goodbye!");

            } else {
                System.out.println("Invalid choice. Please enter a number from 1 to 4.");
            }
        }

        input.close();
    }

    public static String readMessage(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine();
            if (!line.trim().isEmpty()) {
                return line;
            }
            System.out.println("Error: the message can't be empty.");
        }
    }

    // The key must be a word made of letters only, like "DOG" or "secret"
    public static String readKey(Scanner input) {
        while (true) {
            System.out.print("Enter the secret key (letters only): ");
            String key = input.nextLine().trim();

            boolean valid = key.length() > 0;
            for (int i = 0; i < key.length(); i++) {
                if (!Character.isLetter(key.charAt(i))) {
                    valid = false;
                }
            }

            if (valid) {
                return key.toUpperCase();
            }
            System.out.println("Error: the key must be at least one letter and contain only letters.");
        }
    }

    // Each letter of the key says how far to move a letter of the message:
    // A = 0, B = 1, C = 2, ... Z = 25.
    // The key repeats if the message is longer than the key.
    public static String encrypt(String text, String key) {
        String result = "";
        int keyIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            if (Character.isLetter(current)) {
                int amount = key.charAt(keyIndex % key.length()) - 'A';
                result += moveLetter(current, amount);
                keyIndex++;
            } else {
                result += current;
            }
        }

        return result;
    }

    // Decrypting moves each letter backward by the same amount.
    // Moving back by n is the same as moving forward by 26 - n.
    public static String decrypt(String text, String key) {
        String result = "";
        int keyIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            if (Character.isLetter(current)) {
                int amount = key.charAt(keyIndex % key.length()) - 'A';
                result += moveLetter(current, 26 - amount);
                keyIndex++;
            } else {
                result += current;
            }
        }

        return result;
    }

    // Moves one letter forward in the alphabet, wrapping from Z back to A.
    // Letters that aren't A-Z (spaces, digits, symbols) are never passed in.
    public static char moveLetter(char letter, int amount) {
        if (letter >= 'A' && letter <= 'Z') {
            return (char) ('A' + (letter - 'A' + amount) % 26);
        } else if (letter >= 'a' && letter <= 'z') {
            return (char) ('a' + (letter - 'a' + amount) % 26);
        }
        return letter;
    }

    // Prints a table showing which key letter was used on each message letter
    public static void showSteps(String text, String key) {
        System.out.println();
        System.out.println("Message  Key  Moves  Result");
        int keyIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            if (Character.isLetter(current)) {
                char keyLetter = key.charAt(keyIndex % key.length());
                int amount = keyLetter - 'A';
                char encrypted = moveLetter(current, amount);
                System.out.println("   " + current + "      " + keyLetter + "     " + amount
                        + (amount < 10 ? " " : "") + "     " + encrypted);
                keyIndex++;
            }
        }

        System.out.println();
        System.out.println("Encrypted: " + encrypt(text, key));
    }
}
