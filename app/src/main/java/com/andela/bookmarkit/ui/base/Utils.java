package com.andela.bookmarkit.ui.base;


import com.andela.bookmarkit.R;

import java.util.Random;

public class Utils {
    private static int[] colors = {
            R.color.colorPrimary,
            R.color.colorAccent,
            R.color.colorPrimaryLight,
            R.color.colorPrimaryDark
    };

    public static int getRandomColor() {
        // Obtain a number between 0 - 3
        Random rand = new Random();
        int number = rand.nextInt(4);
        return colors[number];
    }
}
