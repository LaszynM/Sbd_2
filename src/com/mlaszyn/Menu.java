package com.mlaszyn;

import java.io.*;

public class Menu {
    public Menu(){}
    public void startMenu() {
        while(true) {
            System.out.println("Menu");
            System.out.println("Choose option:");
            System.out.println("1: Read from file");
            System.out.println("2: Start from scratch");
            System.out.println("3: Exit program");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String name = reader.readLine();
                if (name.equals("1")) {
                    readTreeFromFile();
                } else if (name.equals("2"))
                    startTreeFromScratch();
                else if (name.equals("3"))
                    return;
                else
                    System.out.println("Wrong command, please try again");
            } catch (IOException io) {
                System.out.println("Input error");
            }
        }
    }

    public void saveTree(BTree b, String name) {
        try {
            FileOutputStream f =  new FileOutputStream(new File(name));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(b);
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    public BTree readTree(String name) {
        try {
            FileInputStream fi = new FileInputStream(new File(name));
            ObjectInputStream oi = new ObjectInputStream(fi);
            BTree b2 = (BTree) oi.readObject();

            b2.Show();
            oi.close();
            fi.close();

            return b2;

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            System.out.println("File not found");
        }
        return null;
    }

    public void testing() {
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

        b.UpdateValue(13, "pouiyuyuhu");
    }

    public void readTreeFromFile() {
        String tName = "BTree2.txt";
        BTree b = readTree(tName);

        while(true) {
            System.out.println("Read tree from file");
            System.out.println("Choose option:");
            System.out.println("1: Add key and value");
            System.out.println("2: Update value at target key");
            System.out.println("3: Delete key");
            System.out.println("4: Show tree");
            System.out.println("5: Exit program");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String name = reader.readLine();
                if (name.equals("1")) {
                    System.out.println("Please enter key:");
                    String keyy = reader.readLine();
                    int key = Integer.parseInt(keyy);
                    System.out.println("Please enter value:");
                    String val = reader.readLine();

                    b.Insert(key, val);
                    saveTree(b, tName);

                } else if (name.equals("2")) {
                    System.out.println("Please enter key:");
                    String keyy = reader.readLine();
                    int key = Integer.parseInt(keyy);
                    System.out.println("Please enter value:");
                    String val = reader.readLine();
                    b.UpdateValue(key, val);
                }
                else if (name.equals("3")) {
                    System.out.println("Work in progress");
                }
                else if (name.equals("4")) {
                    b.Show();
                }
                else if (name.equals("5")) {
                    return;
                }
                else
                    System.out.println("Wrong command, please try again");
            } catch (IOException io) {
                System.out.println("Input error");
            }
        }
    }

    public void startTreeFromScratch() {
        BTree b = new BTree(2);
        String tName = "BTree.txt";
        while(true) {
            System.out.println("Read tree from file");
            System.out.println("Choose option:");
            System.out.println("1: Add key and value");
            System.out.println("2: Update value at target key");
            System.out.println("3: Delete key");
            System.out.println("4: Show tree");
            System.out.println("5: Exit program");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String name = reader.readLine();
                if (name.equals("1")) {
                    System.out.println("Please enter key:");
                    String keyy = reader.readLine();
                    int key = Integer.parseInt(keyy);
                    System.out.println("Please enter value:");
                    String val = reader.readLine();

                    b.Insert(key, val);
                    saveTree(b, tName);

                } else if (name.equals("2")) {
                    System.out.println("Please enter key:");
                    String keyy = reader.readLine();
                    int key = Integer.parseInt(keyy);
                    System.out.println("Please enter value:");
                    String val = reader.readLine();

                    b.UpdateValue(key, val);
                }
                else if (name.equals("3")) {
                    System.out.println("Work in progress");
                }
                else if (name.equals("4")) {
                    System.out.println("Work in progress");
                }
                else if (name.equals("5")) {
                    return;
                }
                else
                    System.out.println("Wrong command, please try again");
            } catch (IOException io) {
                System.out.println("Input error");
            }
        }
    }

}
