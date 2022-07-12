//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.aybici;

public class ArgsCounter {
    public ArgsCounter() {
    }

    public static int[] countArgs(String args) {
        int normal = 0;
        int possible = 0;
        char[] var3 = args.toCharArray();
        int num = var3.length;

        int i;
        for(i = 0; i < num; ++i) {
            char c = var3[i];
            if (c == '<') {
                ++normal;
            } else if (c == '[') {
                ++possible;
            }
        }

        int[] allPossibilities = new int[possible + 1];
        num = normal;

        for(i = 0; i <= possible; ++i) {
            allPossibilities[i] = num++;
        }

        return allPossibilities;
    }
}
