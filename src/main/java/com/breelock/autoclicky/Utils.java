package com.breelock.autoclicky;

import net.minecraft.util.Util;

import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {


    /**
     * Gibt eine zufällige Ganzzahl zwischen min und max zurück.
     */
    public static int randint(int min, int max) {

        if (min > max) {

            int temp = min;
            min = max;
            max = temp;
        }


        return ThreadLocalRandom.current()
                .nextInt(min, max + 1);
    }




    /**
     * Berechnet die Anzahl Minecraft-Ticks
     * bis zum nächsten Klick.
     *
     * Minecraft:
     * 20 Ticks = 1 Sekunde
     *
     * Beispiel:
     * 10 CPS = ungefähr alle 2 Ticks
     */
    public static int cpsToDelayTicks(int cps) {

        if (cps <= 0)
            return 20;


        return Math.max(
                1,
                20 / cps
        );
    }




    /**
     * Berechnet eine zufällige Verzögerung
     * zwischen minimaler und maximaler CPS.
     */
    public static int randomCpsDelay(
            int minCps,
            int maxCps
    ) {

        int cps = randint(
                minCps,
                maxCps
        );


        return cpsToDelayTicks(cps);
    }




    /**
     * Öffnet einen Link im Standardbrowser.
     */
    public static void openLink(String url) {

        try {

            URI uri = new URI(url);

            Util.getOperatingSystem()
                    .open(uri);

        }

        catch (Exception e) {

            AutoClicky.LOGGER.error(
                    "AutoClicky konnte Link nicht öffnen: {}",
                    url,
                    e
            );
        }
    }
}
