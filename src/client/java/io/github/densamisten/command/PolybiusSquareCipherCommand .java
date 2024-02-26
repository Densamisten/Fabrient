package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class PolybiusSquareCipherCommand {
    private static final char[][] polybiusSquare = {
            {'A', 'B', 'C', 'D', 'E'},
            {'F', 'G', 'H', 'I', 'K'},
            {'L', 'M', 'N', 'O', 'P'},
            {'Q', 'R', 'S', 'T', 'U'},
            {'V', 'W', 'X', 'Y', 'Z'}
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cipher")
                .then(CommandManager.literal("polybius")
                        .then(CommandManager.literal("encrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .executes(context -> {
                                            String inputText = context.getArgument("text", String.class);
                                            String encryptedText = encrypt(inputText.toUpperCase());
                                            context.getSource().sendFeedback(Text.of("Encrypted text: " + encryptedText), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(CommandManager.literal("decrypt")
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .executes(context -> {
                                            String inputText = context.getArgument("text", String.class);
                                            String decryptedText = decrypt(inputText.toUpperCase());
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
                int[] coordinates = findCoordinates(ch);
                encryptedText.append(coordinates[0]).append(coordinates[1]);
            } else {
                encryptedText.append(ch); // Keep non-letter characters as is
            }
        }
        return encryptedText.toString();
    }

    private static String decrypt(String text) {
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            int x = Character.getNumericValue(text.charAt(i)) - 1;
            int y = Character.getNumericValue(text.charAt(i + 1)) - 1;
            if (x >= 0 && x < 5 && y >= 0 && y < 5) {
                decryptedText.append(polybiusSquare[x][y]);
            } else {
                decryptedText.append(text.charAt(i)).append(text.charAt(i + 1)); // Keep non-coordinate characters as is
            }
        }
        return decryptedText.toString();
    }

    private static int[] findCoordinates(char letter) {
        int[] coordinates = new int[2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (polybiusSquare[i][j] == letter) {
                    coordinates[0] = i + 1;
                    coordinates[1] = j + 1;
                    return coordinates;
                }
            }
        }
        return coordinates;
    }
}
