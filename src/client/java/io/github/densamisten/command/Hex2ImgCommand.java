package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.util.Clipboard;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class Hex2ImgCommand {
    private static final Clipboard clipboardManager = new Clipboard();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("hex2img")
                .executes(context -> {
                    try {
                        return execute(context.getSource());
                    } catch (DecoderException | IOException | AWTException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }

    private static int execute(ServerCommandSource source) throws DecoderException, IOException, AWTException {
        String clipboard = clipboardManager.getClipboard(GLFW.glfwGetCurrentContext(), GLFWErrorCallback.createPrint(System.err));
        if (clipboard == null || clipboard.isEmpty()) {
            source.sendError(Text.of("Clipboard is empty."));
            return 0;
        }

        try {
            byte[] imageData = hexStringToBytes(clipboard);
            BufferedImage image = loadImageFromBytes(imageData);
            saveImageToFile(image);
            return 1;
        } catch (DecoderException | IOException e) {
            throw new DecoderException("Failed to convert hex to image: " + e.getMessage());
        }
    }

    private static byte[] hexStringToBytes(String hexString) throws DecoderException {
        return Hex.decodeHex(hexString.toCharArray());
    }

    private static BufferedImage loadImageFromBytes(byte[] imageData) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageData));
    }

    private static void saveImageToFile(BufferedImage image) throws IOException {
        ImageIO.write(image, "png", new File("output.png"));
    }

    private static byte[] imageToBytes(BufferedImage image) {
        return ((DataBufferByte) image.getData().getDataBuffer()).getData();
    }
}
