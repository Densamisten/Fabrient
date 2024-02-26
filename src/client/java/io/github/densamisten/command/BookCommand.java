package io.github.densamisten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.util.Clipboard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class BookCommand {

    private static final Clipboard clipboardManager = new Clipboard();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("book")
                        .then(CommandManager.literal("write")
                                .then(CommandManager.literal("nbt")
                                        .executes(BookCommand::writeNbt))
                                .then(CommandManager.literal("text")
                                        .executes(BookCommand::writeText)))
                        .then(CommandManager.literal("read")
                                .then(CommandManager.literal("nbt")
                                        .executes(BookCommand::readNbt))
                                .then(CommandManager.literal("text")
                                        .executes(BookCommand::readText))));
    }


    private static int writeText(CommandContext<ServerCommandSource> context) {
        PlayerEntity player = context.getSource().getPlayer();
        ItemStack heldItem = player.getMainHandStack();

        // Check if the held item is a writable book
        if (heldItem.getItem() == Items.WRITABLE_BOOK) {
            String clipboardText = clipboardManager.getClipboard(GLFW.glfwGetCurrentContext(), GLFWErrorCallback.createPrint(System.err));

            // Check if clipboard has text
            if (clipboardText.isEmpty()) {
                context.getSource().sendError(Text.literal("Clipboard is empty!"));
                return 0;
            }

            NbtList pages = new NbtList();
            StringBuilder currentPage = new StringBuilder();

            // Iterate over clipboard contents and write them to book pages
            for (int i = 0; i < clipboardText.length(); i++) {
                // Append character to current page
                currentPage.append(clipboardText.charAt(i));

                // Check if the current page exceeds the character limit or if all clipboard content has been processed
                if (currentPage.length() >= 256 || i == clipboardText.length() - 1) {
                    // Add current page to the list of pages
                    pages.add(NbtString.of(currentPage.toString()));

                    // Reset current page
                    currentPage = new StringBuilder();
                }
            }

            // Add pages to the book
            writeTextToBook(heldItem, pages);

            context.getSource().sendMessage(Text.literal("Clipboard contents written to the book!"));
            return 1;
        } else {
            context.getSource().sendError(Text.literal("You need to hold a writable book in your hand!"));
            return 0;
        }
    }

    private static void writeTextToBook(ItemStack book, NbtList pages) {
        NbtCompound bookTag = book.getOrCreateNbt();
        bookTag.put("pages", pages);
    }


    private static int writeNbt(CommandContext<ServerCommandSource> context) {
        PlayerEntity player = context.getSource().getPlayer();
        ItemStack heldItem = player.getMainHandStack();

        // Check if the held item is a writable book
        if (heldItem.getItem() == Items.WRITABLE_BOOK) {
            String clipboardText = clipboardManager.getClipboard(GLFW.glfwGetCurrentContext(), GLFWErrorCallback.createPrint(System.err));

            // Check if clipboard has text
            if (clipboardText.isEmpty()) {
                context.getSource().sendError(Text.literal("Clipboard is empty!"));
                return 0;
            }

            // Add clipboard text as raw data to the book
            NbtCompound bookTag = heldItem.getOrCreateNbt();
            bookTag.putString("raw_data", clipboardText);

            context.getSource().sendMessage(Text.literal("Clipboard contents written to the book!"));
            return 1;
        } else {
            context.getSource().sendError(Text.literal("You need to hold a writable book in your hand!"));
            return 0;
        }
    }

    private static int readNbt(CommandContext<ServerCommandSource> context) {
        PlayerEntity player = context.getSource().getPlayer();
        ItemStack heldItem = player.getMainHandStack();

        if (heldItem.getItem() == Items.WRITABLE_BOOK || heldItem.getItem() == Items.WRITTEN_BOOK) {
            NbtCompound bookTag = heldItem.getNbt();
            if (bookTag != null && bookTag.contains("raw_data")) {
                String rawData = bookTag.getString("raw_data");
                clipboardManager.setClipboard(GLFW.glfwGetCurrentContext(), rawData);
                context.getSource().sendMessage(Text.literal("Clipboard contents read from the book!"));
                return 1;
            } else {
                context.getSource().sendError(Text.literal("The book does not contain raw data!"));
                return 0;
            }
        } else {
            context.getSource().sendError(Text.literal("You need to hold a written book in your hand!"));
            return 0;
        }
    }

    private static int readText(CommandContext<ServerCommandSource> context) {
        PlayerEntity player = context.getSource().getPlayer();
        ItemStack heldItem = player.getMainHandStack();

        if (heldItem.getItem() == Items.WRITABLE_BOOK || heldItem.getItem() == Items.WRITTEN_BOOK) {
            NbtCompound bookTag = heldItem.getNbt();
            if (bookTag != null && bookTag.contains("pages")) {
                NbtList pagesTag = bookTag.getList("pages", 8); // Assuming it's a ListTag of strings
                StringBuilder textBuilder = new StringBuilder();
                for (int i = 0; i < pagesTag.size(); i++) {
                    textBuilder.append(pagesTag.getString(i)).append("\n");
                }
                String text = textBuilder.toString().trim();
                clipboardManager.setClipboard(GLFW.glfwGetCurrentContext(), text);
                return 1;
            } else {
                context.getSource().sendError(Text.literal("The book is empty!"));
                return 0;
            }
        } else {
            context.getSource().sendError(Text.literal("You need to hold a written book in your hand!"));
            return 0;
        }
    }

}
