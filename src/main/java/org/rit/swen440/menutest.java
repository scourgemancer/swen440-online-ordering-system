package org.rit.swen440;

import org.rit.swen440.presentation.menumgr;

import java.io.FileInputStream;
import java.io.IOException;

public class menutest
{
    public static void main(String[] args)
    {
        try {
            System.out.println("Welcome to Hippolyta.com!");
            menumgr mgr = new menumgr();
            int currentLevel = 0;
            boolean done = false;
            do {
                done = mgr.loadLevel(currentLevel);
            } while (!done);

            System.out.println("Thank you for shopping at Hippolyta.com!");

        } catch(Exception e) {
            System.err.println(e);
        }

    }
}
