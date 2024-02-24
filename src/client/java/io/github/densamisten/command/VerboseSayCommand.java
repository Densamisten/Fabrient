package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.util.Clipboard;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class VerboseSayCommand {
    private static final Clipboard clipboardManager = new Clipboard();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("vsay")
                .executes(ctx -> sendVerboseMessage(ctx.getSource())));
    }

    private static int sendVerboseMessage(ServerCommandSource source) {
        // Get the clipboard contents
        String clipboardContents = clipboardManager.getClipboard(GLFW.glfwGetCurrentContext(), GLFWErrorCallback.createPrint(System.err));
        // Perform send message action
        source.sendMessage(Text.of(clipboardContents));
        // Return 1 for success
        return 1;
    }
}
