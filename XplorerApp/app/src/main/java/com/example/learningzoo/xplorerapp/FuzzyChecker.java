package com.example.learningzoo.xplorerapp;

/**
 * Created by stanleynguyen on 13/11/16.
 */

import net.java.frej.Regex;

public class FuzzyChecker {
    public static String magic(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", "");
        input = input.replaceAll("[^a-zA-Z]", "");

        Regex mbsTest1 = new Regex("[marinabaysands]");
        Regex mbsTest2 = new Regex("[marinasands]");
        Regex mbsTest3 = new Regex("[mbs]");

        Regex flyerTest1 = new Regex("[singaporeflyer]");
        Regex flyerTest2 = new Regex("[flyersingapore]");
        Regex flyerTest3 = new Regex("[flyer]");

        Regex vivoTest1 = new Regex("[vivocity]");
        Regex vivoTest2 = new Regex("[cityvivo]");
        Regex vivoTest3 = new Regex("[vivo]");

        Regex sentosaTest1 = new Regex("[resortworldsentosa]");
        Regex sentosaTest2 = new Regex("[sentosa]");
        Regex sentosaTest3 = new Regex("[sentosaresort]");

        Regex templeTest1 = new Regex("[buddhatoothrelictemple]");
        Regex templeTest2 = new Regex("[toothrelictemple]");
        Regex templeTest3 = new Regex("[buddhatoothtemple]");

        Regex zooTest1 = new Regex("[singaporezoo]");
        Regex zooTest2 = new Regex("[zoosingapore]");
        Regex zooTest3 = new Regex("[zoo]");

        Regex ussTest1 = new Regex("[universalstudiosingapore]");
        Regex ussTest2 = new Regex("[universalstudio]");
        Regex ussTest3 = new Regex("[uss]");

        Regex safariTest1 = new Regex("[singaporenighsafari]");
        Regex safariTest2 = new Regex("[safarinight]");
        Regex safariTest3 = new Regex("[nightysafari]");

        Regex merlionTest1 = new Regex("[merlionpark]");
        Regex merlionTest2 = new Regex("[parkmerlion]");
        Regex merlionTest3 = new Regex("[merlion]");

        if (mbsTest1.match(input)|mbsTest2.match(input)|mbsTest3.match(input)) {
            return "MBS";
        } else if (flyerTest1.match(input)|flyerTest2.match(input)|flyerTest3.match(input)) {
            return "SF";
        } else if (vivoTest1.match(input)|vivoTest2.match(input)|vivoTest3.match(input)) {
            return "VC";
        } else if (sentosaTest1.match(input)|sentosaTest2.match(input)|sentosaTest3.match(input)) {
            return "RWS";
        } else if (templeTest1.match(input)|templeTest2.match(input)|templeTest3.match(input)) {
            return "BTRT";
        } else if (zooTest1.match(input)|zooTest2.match(input)|zooTest3.match(input)) {
            return "SZ";
        } else if (ussTest1.match(input)|ussTest2.match(input)|ussTest3.match(input)) {
            return "USS";
        } else if (safariTest1.match(input)|safariTest2.match(input)|safariTest3.match(input)) {
            return "NS";
        } else if (merlionTest1.match(input)|merlionTest2.match(input)|safariTest3.match(input)) {
            return "MP";
        } else {
            return "not found";
        }
    }
}
