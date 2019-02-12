package com.pelletier.czml.util;

import java.awt.*;
import java.util.Random;

public class ColorGenerator {

    private static String[] colors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };

    public static Color getRandomColor() {
        String color = "";
        // Randomly select a fact
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(colors.length);

        color = colors[randomNumber];
        return Color.decode(color);
    }

}
