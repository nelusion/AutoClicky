package com.breelock.autoclicky;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.fabricmc.loader.api.FabricLoader;

public class ModConfig {

    private static final File CONFIG_FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("autoclicky.json")
                    .toFile();


    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();



    public static Config DATA = new Config();



    public static void load() {

        if (!CONFIG_FILE.exists()) {

            save();
            apply();

            return;
        }


        try (FileReader reader = new FileReader(CONFIG_FILE)) {

            DATA = GSON.fromJson(reader, Config.class);

            if (DATA == null)
                DATA = new Config();

        }

        catch (IOException e) {

            e.printStackTrace();

            DATA = new Config();
        }


        apply();
    }



    public static void save() {

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {

            GSON.toJson(DATA, writer);

        }

        catch (IOException e) {

            e.printStackTrace();
        }
    }



    private static void apply() {

        AutoClicky.minCPS = DATA.minCPS;
        AutoClicky.maxCPS = DATA.maxCPS;
    }



    public static class Config {


        /*
         * Zufällige CPS-Spanne
         *
         * Beispiel:
         *
         * minCPS = 8
         * maxCPS = 14
         *
         * ergibt wechselnde Klickgeschwindigkeit.
         */
        public int minCPS = 8;

        public int maxCPS = 14;



        /*
         * Zeigt Aktivierungsnachrichten
         */
        public boolean showMessages = true;
    }
}
