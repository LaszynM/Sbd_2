package com.mlaszyn;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        Menu menu = new Menu();
        //menu.startMenu();
        menu.testing();
        /*
        try {
            FileOutputStream f =  new FileOutputStream(new File("Dzewo.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(b);
            o.close();
            f.close();

            FileInputStream fi = new FileInputStream(new File("Dzewo.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            BTree b2 = (BTree) oi.readObject();

            b2.Show();
            oi.close();
            fi.close();


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {

        }

         */
    }


}
