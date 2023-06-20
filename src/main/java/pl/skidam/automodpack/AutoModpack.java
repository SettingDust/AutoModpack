/*
 * This file is part of the AutoModpack project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023 Skidam and contributors
 *
 * AutoModpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AutoModpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AutoModpack.  If not, see <https://www.gnu.org/licenses/>.
 */

package pl.skidam.automodpack;

import pl.skidam.automodpack.client.audio.AudioManager;
import pl.skidam.automodpack.loaders.Loader;
import pl.skidam.automodpack.modpack.Commands;
import pl.skidam.automodpack.modpack.Modpack;
import pl.skidam.automodpack.networking.ModPackets;
import pl.skidam.automodpack.utils.MinecraftUserName;

import static pl.skidam.automodpack.StaticVariables.*;

//#if FORGE
//$$ import net.minecraftforge.common.MinecraftForge;
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//$$ import net.minecraftforge.eventbus.api.IEventBus;
//$$ import net.minecraftforge.fml.common.Mod;
//$$ @Mod(MOD_ID)
//#endif
public class AutoModpack {

//#if FORGE
//$$     public AutoModpack() {
//$$        MinecraftForge.EVENT_BUS.register(this);
//$$        Preload.onPreInitialize();
//#elseif FABRICLIKE
    public static void onInitialize() {
//#endif

        preload = false;

        String loader =  Loader.getPlatformType().toString().toLowerCase();

        if (Loader.getEnvironmentType().equals("SERVER")) {
            LOGGER.info("Server environment detected, on " + loader + " platform");
            if (serverConfig.generateModpackOnStart) {
                LOGGER.info("Generating modpack...");
                Modpack.generate();
            }
            ModPackets.registerS2CPackets();
        } else {
            LOGGER.info("Client environment detected, on " + loader + " platform");
            MinecraftUserName.get(); // To save the username` to variable in MinecraftUserName class for later use


//#if FABRICLIKE
            // TODO fix for forge
            new AudioManager();
//#endif

            ModPackets.registerC2SPackets();
        }

        Commands.register();
    }
}