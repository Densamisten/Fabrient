package io.github.densamisten;

import io.github.FabrientRegistries;
import io.github.densamisten.mixin.util.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.densamisten.mixin.util.BoatState.*;
import static io.github.densamisten.mixin.util.FlyingState.*;
import static io.github.densamisten.mixin.util.GliderState.*;
import static io.github.densamisten.mixin.util.JetPackState.*;
import static io.github.densamisten.mixin.util.NoFallState.*;

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
		List<String> languageCountryCodes = new ArrayList<>();
		Collections.addAll(languageCountryCodes,
				"af_za", "ara_sa", "ast_es", "aze_az", "bak_ru", "bar_de", "bel_by", "bul_bg", "bre_fr", "qbr_nl",
				"bos_ba", "cat_es", "ces_cz", "cym_gb", "dan_dk", "bar_at", "gsw_ch", "deu_de", "ell_gr", "eng_au",
				"eng_ca", "eng_gb", "eng_nz", "qpe", "eng_qabs_gb", "en_ud", "epo", "spa_ar", "spa_cl", "spa_ec",
				"spa_es", "spa_mx", "spa_uy", "spa_ve", "spa_es_an", "est_ee", "eus_es", "fas_ir", "fin_fi", "fil_ph",
				"fao_fo", "fra_ca", "fra_fr", "fra_de", "fur_it", "fry_nl", "gle_ie", "gla_gb", "glg_es", "haw_us",
				"heb_il", "hin_in", "hrv_hr", "hun_hu", "hye_am", "id_id", "ibo_ng", "is_is", "qis", "ita_it", "jpn_jp",
				"jbo", "kat_ge", "kaz_kz", "kan_in", "kor_kr", "ksh_de", "kw_gb", "lat_va", "ltz_lu", "lim_nl", "lmo_it",
				"lao_la", "qll", "lit_lt", "lav_lv", "lzh", "mkd_mk", "mon_mn", "zlm_my", "mlt_mt", "nhe_mx", "vmf_de",
				"msa_my", "mlg_mg", "mar_in", "nld_be", "nld_nl", "nno_no", "nob_no", "quc_latn_gt", "pol_pl", "por_br",
				"por_pt", "qya", "ro_ro", "qpr", "rus_ru", "srp_latn_rs", "srp_cyrl_rs", "swe_se", "slk_sk", "slv_si",
				"som_so", "sqi_al", "ckb_ir", "sr_sp", "swa_ke", "swh_tz", "tam_in", "tha_th", "tgk_tj", "uig_cn",
				"ukr_ua", "urd_pk", "uzb_uz", "ven_za", "cymr", "xho_za", "yor_ng", "yue_hk", "zho_hans_cn", "zho_hant_hk",
				"zho_hant_tw");

		// Shuffle the list
		Collections.shuffle(languageCountryCodes);

		// Print one code at a time
		for (String code : languageCountryCodes) {
			LanguageManager languageManager = new LanguageManager(code);
			languageManager.reload(getResourceManager());
			MinecraftClient.getInstance().getLanguageManager().setLanguage("sv_se");
			System.out.println(MinecraftClient.getInstance().getLanguageManager().getLanguage());
			System.out.println(code);
			break; // Exit the loop after printing one code
		}
	}

	ResourceManager resourceManager;

	public ResourceManager getResourceManager() {
		return resourceManager;
	}
}