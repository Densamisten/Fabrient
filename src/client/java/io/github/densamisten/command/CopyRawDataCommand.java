package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.util.Clipboard;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CopyRawDataCommand {
    private static final Clipboard clipboardManager = new Clipboard();


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("copyraw")
                .then(CommandManager.argument("file", StringArgumentType.string())
                        .executes(CopyRawDataCommand::copyRawData)
                )
        );
    }

    private static int copyRawData(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String filePath = context.getArgument("file", String.class);
        try {
            byte[] rawData = Files.readAllBytes(Path.of(filePath));
            clipboardManager.setClipboard(GLFW.glfwGetCurrentContext(), bytesToHexString(rawData));
            context.getSource().sendFeedback(Text.of("Raw data copied to clipboard."), false);
        } catch (IOException e) {
            context.getSource().sendError(Text.of("Failed to read file: " + e.getMessage()));
        }
        return 1;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}