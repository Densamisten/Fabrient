package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class RailFenceCipherCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cipher")
                .then(CommandManager.literal("railfence")
                        .then(CommandManager.literal("encrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .then(CommandManager.argument("rails", StringArgumentType.string()) // Specify the number of rails
                                                .executes(context -> {
                                                    String inputText = context.getArgument("text", String.class);
                                                    int rails = context.getArgument("rails", Integer.class);
                                                    String encryptedText = encrypt(inputText, rails);
                                                    context.getSource().sendMessage(Text.of("Encrypted text: " + encryptedText));
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("decrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .then(CommandManager.argument("rails", StringArgumentType.string()) // Specify the number of rails
                                                .executes(context -> {
                                                    String inputText = context.getArgument("text", String.class);
                                                    int rails = context.getArgument("rails", Integer.class);
                                                    String decryptedText = decrypt(inputText, rails);
                                                    context.getSource().sendMessage(Text.of("Decrypted text: " + decryptedText));
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }

    private static String encrypt(String text, int rails) {
        StringBuilder encryptedText = new StringBuilder();
        String[] railFence = new String[rails];
        for (int i = 0; i < rails; i++) {
            railFence[i] = "";
        }

        int rail = 0;
        boolean down = false;

        for (char ch : text.toCharArray()) {
            railFence[rail] += ch;
            if (rail == 0 || rail == rails - 1) {
                down = !down;
            }
            rail += down ? 1 : -1;
        }

        for (String line : railFence) {
            encryptedText.append(line);
        }

        return encryptedText.toString();
    }

    private static String decrypt(String text, int rails) {
        StringBuilder decryptedText = new StringBuilder();
        int textLength = text.length();
        int cycle = 2 * (rails - 1);

        for (int i = 0; i < rails; i++) {
            int step = 2 * i;
            for (int j = i, k = cycle - i; j < textLength || k < textLength; j += cycle, k += cycle) {
                if (j < textLength) {
                    decryptedText.append(text.charAt(j));
                }
                if (step != 0 && step != cycle && k < textLength) {
                    decryptedText.append(text.charAt(k));
                }
            }
        }

        return decryptedText.toString();
    }
}
