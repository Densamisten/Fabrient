package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CaesarCipherCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cipher")
                .then(CommandManager.literal("caesar")
                        .then(CommandManager.literal("encrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .then(CommandManager.argument("shift", IntegerArgumentType.integer(0))
                                                .executes(context -> {
                                                    String inputText = context.getArgument("text", String.class);
                                                    int shift = context.getArgument("shift", Integer.class);
                                                    String encryptedText = encrypt(inputText, shift);
                                                    context.getSource().sendMessage(Text.of("Encrypted text: " + encryptedText));
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("decrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .then(CommandManager.argument("shift", IntegerArgumentType.integer(0))
                                                .executes(context -> {
                                                    String inputText = context.getArgument("text", String.class);
                                                    int shift = context.getArgument("shift", Integer.class);
                                                    String decryptedText = decrypt(inputText, shift);
                                                    context.getSource().sendMessage(Text.of("Decrypted text: " + decryptedText));
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }

    private static String encrypt(String text, int shift) {
        StringBuilder encryptedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char shifted = (char) (ch + shift);
                if (Character.isLowerCase(ch)) {
                    if (shifted > 'z') {
                        shifted = (char) ('a' + (shifted - 'z' - 1));
                    } else if (shifted < 'a') {
                        shifted = (char) ('z' - ('a' - shifted - 1));
                    }
                } else if (Character.isUpperCase(ch)) {
                    if (shifted > 'Z') {
                        shifted = (char) ('A' + (shifted - 'Z' - 1));
                    } else if (shifted < 'A') {
                        shifted = (char) ('Z' - ('A' - shifted - 1));
                    }
                }
                encryptedText.append(shifted);
            } else {
                encryptedText.append(ch);
            }
        }
        return encryptedText.toString();
    }

    private static String decrypt(String text, int shift) {
        return encrypt(text, -shift);
    }
}
