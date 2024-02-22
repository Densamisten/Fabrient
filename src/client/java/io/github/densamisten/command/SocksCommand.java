package io.github.densamisten.command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.net.*;
import java.io.*;

public class SocksCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("socks")
                .then(CommandManager.argument("port", IntegerArgumentType.integer())
                        .then(CommandManager.argument("proxyHost", StringArgumentType.word())
                                .then(CommandManager.argument("proxyPort", IntegerArgumentType.integer())
                                        .executes(context -> connectToOnion(
                                                context,
                                                "http://qmfwcrzhdou57wdqnxmqcu2i6vd4vy27d53oblnupprl26gas5ssj4id.onion",
                                                IntegerArgumentType.getInteger(context, "port"),
                                                StringArgumentType.getString(context, "proxyHost"),
                                                IntegerArgumentType.getInteger(context, "proxyPort")
                                        ))))));
    }

    private static int connectToOnion(CommandContext<ServerCommandSource> context, String onionUrl, int port, String proxyHost, int proxyPort) {
        try {
            // Set up proxy
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, proxyPort));

            // Create socket
            Socket socket = new Socket(proxy);

            // Connect to .onion address
            socket.connect(new InetSocketAddress(onionUrl, port));

            // Now you can communicate with the .onion server through the socket

            // Example: Send an HTTP GET request
            String httpRequest = "GET / HTTP/1.1\r\nHost: " + onionUrl + "\r\n\r\n";
            socket.getOutputStream().write(httpRequest.getBytes());

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                String finalResponseLine = responseLine;
                context.getSource().sendFeedback(Text.of(finalResponseLine), false);
            }

            // Close socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            context.getSource().sendError(Text.of("An error occurred while connecting to the .onion server."));
        }

        return 1;
    }
}
