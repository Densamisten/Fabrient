package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SubstitutionCipherCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cipher")
                .then(CommandManager.literal("substitution")
                        .then(CommandManager.literal("encrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .then(CommandManager.argument("key", StringArgumentType.string())
                                                .executes(context -> {
                                                    String inputText = context.getArgument("text", String.class);
                                                    String key = context.getArgument("key", String.class);
                                                    String encryptedText = encrypt(inputText, key);
                                                    context.getSource().sendMessage(Text.of("Encrypted text: " + encryptedText));
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
                                                    context.getSource().sendMessage(Text.of("Decrypted text: " + decryptedText));
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
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char substitute = key.charAt(Character.toUpperCase(ch) - 'A');
                encryptedText.append(Character.isLowerCase(ch) ? Character.toLowerCase(substitute) : substitute);
            } else {
                encryptedText.append(ch);
            }
        }
        return encryptedText.toString();
    }

    private static String decrypt(String text, String key) {
        StringBuilder decryptedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char substitute = (char) ('A' + key.indexOf(Character.toUpperCase(ch)));
                decryptedText.append(Character.isLowerCase(ch) ? Character.toLowerCase(substitute) : substitute);
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }
}

