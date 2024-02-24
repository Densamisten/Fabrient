package io.github.densamisten;

import io.github.FabrientRegistries;
import io.github.densamisten.util.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import static io.github.densamisten.util.BoatState.*;
import static io.github.densamisten.util.FlyingState.*;
import static io.github.densamisten.util.GliderState.*;
import static io.github.densamisten.util.JetPackState.*;
import static io.github.densamisten.util.NoFallState.*;

public class FabrientClient implements ClientModInitializer {

	public static boolean senthelp, sentfly, sentboat, sentjet, sentfall, sentglide = false;
	public static boolean nofallonce = false;
	public static FlyingState flyingState = NOT_FLYING;
	public static BoatState boatState = OFFB;
	public static JetPackState jetPackState = OFF;
	public static NoFallState fallState = F;
	public static GliderState glidingState = OFFG;
	private static final KeyBinding flyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Fly toggle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_K,
			"FlyHack v1.3 Binds"
	));
	private static final KeyBinding boatKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Boat Fly toggle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_B,
			"FlyHack v1.3 Binds"
	));
	final KeyBinding JETPACK_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"JetPack toggle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_J,
			"FlyHack v1.3 Binds"
	));
	private static final KeyBinding fallKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"No Fall Damage toggle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_N,
			"FlyHack v1.3 Binds"
	));
	private static final KeyBinding gliderKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Glider toggle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_G,
			"FlyHack v1.3 Binds"
	));
	private static final KeyBinding helpKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Help toggle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_H,
			"FlyHack v1.3 Binds"
	));

		// Register the key binding using KeyBindingHelper
		 public static final KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.examplemod.spook", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_E, // The keycode of the key
				"category.examplemod.test" // The translation key of the keybinding's category.
		));
	@Override
	public void onInitializeClient() {
		FabrientRegistries.registerFabrientStuff();
		ClientTickEvents.START_CLIENT_TICK.register(client -> {

			ClientPlayerEntity player = MinecraftClient.getInstance().player;

			if (JETPACK_KEY.wasPressed()) {
				jetPackState = jetPackState == ON ? NEUTRALJP : ON;
				if(!sentjet) {
					if(player != null) {
						player.sendMessage(Text.of("[JetPack ON]"));
						sentjet=true;
					}
				} else if (player != null) {
					player.sendMessage(Text.of("[JetPack OFF]"));
					sentjet=false;
				}

			}
			if (boatKey.wasPressed()) {
				boatState = boatState == ONB ? NEUTRALB : ONB;
				if(!sentboat) {
					if(player != null) {
						player.sendMessage(Text.of("[BoatFly ON]"));
						sentboat=true;
					}
				} else if (player != null) {
					player.sendMessage(Text.of("[BoatFly OFF]"));
					sentboat=false;
				}
			}
			if (flyKey.wasPressed()) {
				flyingState = flyingState == FLYING ? NEUTRAL : FLYING;
				if(!sentfly) {
					if(player != null) {
						player.sendMessage(Text.of("[CreativeFly ON]"));
						sentfly=true;
					}
				} else if (player != null) {
					player.sendMessage(Text.of("[CreativeFly OFF]"));
					sentfly=false;
				}
			}
			if (fallKey.wasPressed()) {
				fallState = fallState == NOF ? NEU : NOF;
				if(!sentfall) {
					if(player != null) {
						player.sendMessage(Text.of("[NoFall ON]"));
						sentfall=true;
					}
				} else if (player != null) {
					player.sendMessage(Text.of("[NoFall OFF]"));
					sentfall=false;
				}
			}
			if (gliderKey.wasPressed()) {
				glidingState = glidingState == ONG ? NEUTRALG : ONG;
				if(!sentglide) {
					if(player != null) {
						player.sendMessage(Text.of("[Glider ON]"));
						sentglide=true;
					}
				} else if (player != null) {
					player.sendMessage(Text.of("[Glider OFF]"));
					sentglide=false;
				}
			}
			if (helpKey.wasPressed()) {
				senthelp = false;
			}
			if(player != null) {
				if(!senthelp) {
					player.sendMessage((Text.of(" ")));
					player.sendMessage(Text.of("-----------------------HELP-----------------------"));
					player.sendMessage(Text.of("JetPack:J, Glider:G, BoatFly:B, CreativeFly:K, NoFallDamage:N"));
					player.sendMessage(Text.of("--------------------------------------------------"));
					player.sendMessage((Text.of(" ")));
					senthelp = true;
				}

				if(boatState == ONB) {
					BoatFly.onUpdate();
				}
				if(jetPackState == ON) {
					JetpackHack.updateJetpack();
				}
				if(fallState == NOF) {
					NoFallHack noFallHack = new NoFallHack();
					noFallHack.cancelFallDamage(player);
				}
				if(glidingState == ONG) {
					GliderHack.updateGlider();
				}
                player.getAbilities().flying = flyingState == FLYING;
			}
		});
	}
}