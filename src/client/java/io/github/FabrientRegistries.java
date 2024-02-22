package io.github;

import com.ibm.icu.text.RawCollationKey;
import io.github.densamisten.command.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class FabrientRegistries {

    public static void registerFabrientStuff() {
        registerCommands(); // commands
    }

public static void registerCommands() {
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> CaesarCipherCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> AtbashCipherCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> SubstitutionCipherCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> VigenereCipherCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> RailFenceCipherCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> BookCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> VerboseSayCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> CopyRawDataCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> Hex2ImgCommand.register(dispatcher)));
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> SocksCommand.register(dispatcher)));
    }
}
