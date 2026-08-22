package com.breelock.autoclicky;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;

public class AutoClicky implements ClientModInitializer {

    public static final String MOD_ID = "autoclicky";

    private static KeyBinding leftClickBind;
    private static KeyBinding rightClickBind;
    private static KeyBinding settingsBind;

    private static boolean leftEnabled = false;
    private static boolean rightEnabled = false;

    private static int clickDelayTicks = 0;

    /*
     * Später aus ModConfig laden
     *
     * Beispiel:
     * minCPS = 8
     * maxCPS = 14
     */
    public static int minCPS = 8;
    public static int maxCPS = 14;


    @Override
    public void onInitializeClient() {

        ModConfig.load();


        leftClickBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.autoclicky.left",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "key.categories.autoclicky"
                )
        );


        rightClickBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.autoclicky.right",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "key.categories.autoclicky"
                )
        );


        settingsBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.autoclicky.settings",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "key.categories.autoclicky"
                )
        );


        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null)
                return;


            if (leftClickBind.wasPressed()) {

                leftEnabled = !leftEnabled;
                rightEnabled = false;

                sendToggleMessage(
                        client,
                        "Linksklick",
                        leftEnabled
                );
            }


            if (rightClickBind.wasPressed()) {

                rightEnabled = !rightEnabled;
                leftEnabled = false;

                sendToggleMessage(
                        client,
                        "Rechtsklick",
                        rightEnabled
                );
            }


            if (settingsBind.wasPressed()) {

                /*
                 * Wird später ersetzt:
                 * client.setScreen(new OldCombatScreen());
                 */
            }


            if (client.currentScreen != null)
                return;


            if (clickDelayTicks > 0) {
                clickDelayTicks--;
                return;
            }


            if (leftEnabled) {

                PlayerMethods.attack(client);

                clickDelayTicks = calculateDelay();
            }


            else if (rightEnabled) {

                PlayerMethods.interact(client);

                clickDelayTicks = calculateDelay();
            }

        });
    }



    private static int calculateDelay() {

        int cps = ThreadLocalRandom.current()
                .nextInt(minCPS, maxCPS + 1);


        /*
         * Minecraft läuft mit 20 Ticks pro Sekunde.
         *
         * Beispiel:
         * 10 CPS = ungefähr alle 2 Ticks
         */
        return Math.max(
                1,
                20 / cps
        );
    }



    private static void sendToggleMessage(
            MinecraftClient client,
            String button,
            boolean enabled
    ) {

        if (client.player == null)
            return;


        client.player.sendMessage(
                Text.literal(
                        button +
                        (enabled ? " aktiviert" : " deaktiviert")
                ),
                true
        );
    }
}
