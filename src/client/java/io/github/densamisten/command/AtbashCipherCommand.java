package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AtbashCipherCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cipher")
                .then(CommandManager.literal("atbash")
                        .then(CommandManager.literal("encrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .executes(context -> {
                                            String inputText = context.getArgument("text", String.class);
                                            String encryptedText = encrypt(inputText);
                                            context.getSource().sendFeedback(Text.of("Encrypted text: " + encryptedText), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(CommandManager.literal("decrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .executes(context -> {
                                            String inputText = context.getArgument("text", String.class);
                                            String decryptedText = decrypt(inputText);
                                            context.getSource().sendFeedback(Text.of("Decrypted text: " + decryptedText), false);
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }

    private static String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char encryptedChar = (char) ('Z' - (Character.toUpperCase(ch) - 'A'));
                encryptedText.append(Character.isLowerCase(ch) ? Character.toLowerCase(encryptedChar) : encryptedChar);
            } else {
                encryptedText.append(ch);
            }
        }
        return encryptedText.toString();
    }

    private static String decrypt(String text) {
        return encrypt(text); // In Atbash cipher, encryption and decryption are the same
    }
}
