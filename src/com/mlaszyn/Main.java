package com.mlaszyn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //System.out.println("test");
        BTree b = new BTree(2);
        b.Insert(8, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(9, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(10, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(11, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(15, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(4, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(31, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(7, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(23, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(13, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(22, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(12, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(20, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(17, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(14, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(13, "asdsf");
        b.Show();
        System.out.println("-------------------------------");
        b.Insert(18, "asdsf");

        b.Show();
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");

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
