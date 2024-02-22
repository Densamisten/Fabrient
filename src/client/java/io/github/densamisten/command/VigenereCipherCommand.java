package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class VigenereCipherCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cipher")
                .then(CommandManager.literal("vigenere")
                        .then(CommandManager.literal("encrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .then(CommandManager.argument("key", StringArgumentType.string())
                                                .executes(context -> {
                                                    String inputText = context.getArgument("text", String.class);
                                                    String key = context.getArgument("key", String.class);
                                                    String encryptedText = encrypt(inputText, key);
                                                    context.getSource().sendFeedback(Text.of("Encrypted text: " + encryptedText), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("decrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .then(CommandManager.argument("key", StringArgumentType.string())
                                                .executes(context -> {
                                                    String inputText = context.getArgument("text", String.class);
                                                    String key = context.getArgument("key", String.class);
                                                    String decryptedText = decrypt(inputText, key);
                                                    context.getSource().sendFeedback(Text.of("Decrypted text: " + decryptedText), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }

    private static String encrypt(String text, String key) {
        StringBuilder encryptedText = new StringBuilder();
        int keyLength = key.length();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char shift = key.charAt(i % keyLength);
                int keyShift = Character.toUpperCase(shift) - 'A';
                char encryptedChar = (char) ('A' + (Character.toUpperCase(ch) - 'A' + keyShift) % 26);
                encryptedText.append(Character.isLowerCase(ch) ? Character.toLowerCase(encryptedChar) : encryptedChar);
            } else {
                encryptedText.append(ch);
            }
        }
        return encryptedText.toString();
    }

    private static String decrypt(String text, String key) {
        StringBuilder decryptedText = new StringBuilder();
        int keyLength = key.length();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char shift = key.charAt(i % keyLength);
                int keyShift = Character.toUpperCase(shift) - 'A';
                char decryptedChar = (char) ('A' + (Character.toUpperCase(ch) - 'A' - keyShift + 26) % 26);
                decryptedText.append(Character.isLowerCase(ch) ? Character.toLowerCase(decryptedChar) : decryptedChar);
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }
}
