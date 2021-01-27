package com.mlaszyn;

import java.io.*;
import java.util.Random;

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

    public void Insertt(int key, String value) {
        BTree b = new BTree(2, 0);

        //b.UpdateValue(15, "pppppppppppppppppp");
        b.Insert(key, value);
    }

    public void testing() {
       /* BTree b = new BTree(2, 0);

        //b.UpdateValue(15, "pppppppppppppppppp");
        b.Insert(8, "asdsfalksfhsohaggudhguanvjmnso");
        //b.Show();
        b.Insert(9, "ldsugh9ushugodsgninfdb");
        //b.Show();
        b.Insert(10, "pwoqihjtouhrerwniuqfngvuinv");
        //b.Show();
        b.Insert(11, "tqyufhbdhxvnojnivbmisdgv");
        //b.Show();
        b.Insert(15, "qwretyreiuyijptrkhocxvds");
        //b.Show();

        b.Insert(76, "asdgdshahafjj");
        //b.Show();
        b.Insert(26, "foienrjgnjhbnhb ");
        //b.Show();
        b.Insert(98, "oiewhtunreingugvrfeg");
        //b.Show();
        b.Insert(16, "fdsonbjdfbnjsnjbnm");
        //b.Show();
        b.Insert(30, "sihgnfidsnvjnjdfbbf");
        //b.Show();

        b.Insert(28, "AAKSDHBGHBAJGBHKA");
        //b.Show();
        b.Insert(19, "AGJJFDONBIOMNBEWKLTNWOE");
        //b.Show();
        b.Insert(45, "POJRWOUIENGIUIERGBER");
        //b.Show();
        b.Insert(17, "NHUIEWBYHFBHIWENGOJNOPWM");
        //b.Show();
        b.Insert(14, "PAOEJIONUJNGBNRFGBYDGSBYUYEWBEY");
        //b.Show();

        b.Insert(13, "yeghtwbhgbnjnrfgbmkrd");
        //b.Show();
        b.Insert(18, "jkBKBHJBHJVBJbjnklnlkm");
        //b.Show();
        b.Insert(4, "jnjbnsjabfubaisbubnONAAAA");
        //b.Show();
        b.Insert(31, "AAAAAAAAAAAAAAAAAAAAAAAAA");
       //b.Show();
        b.Insert(7, "BBBBBBBBBBBBBBBBBBBBBB");

        //b.Show();
        b.Insert(23, "cccccccccccccccccc");
        //b.Show();
        b.Insert(13, "dddddddddddddddddddddd");
        //b.Show();
        b.Insert(22, "eeeeeeeeeeeeeeeeeeeeeee");
        //b.Show();
        b.Insert(12, "rrrrrrrrrrrrrrrrrrrrrrrrrrr");
        //b.Show();
        b.Insert(20, "tttttttttttttttttttttttt");
        //b.Show();

        b.Insert(82, "yyyyyyyyyyyyyyyyyyyyyyy");
        //b.Show();
        b.Insert(55, "uuuuuuuuuuuuuuuuuuuu");
        //b.Show();
        b.Insert(39, "nnnnnnnnnnnnnnnnnnnnnnn");
       // b.Show();
        b.Insert(69, "bbbbbbbbbbbbbbbb");

        BTree bt = new BTree(2, 0);
        bt.Show();

        //b.Show();
        saveTree(b, "Btreee.txt");



        b.UpdateValue(13, "pouiyuyuhkdsgbhjsbghkasnu");

        b.Show();
*/
        Random rand = new Random(); //instance of random class

        int upperbound = 9000;
        for (int i = 0; i < 80; i ++) {
            int int_random = rand.nextInt(upperbound);
            Insertt(int_random, "asdsfalksfhsohaggudhguanvjmnso");

        }
        BTree b = new BTree(2, 0);
        b.Show();
        //b.UpdateValue(15, "pppppppppppppppppp");
        //b.Insert(8, "asdsfalksfhsohaggudhguanvjmnso");
        //b.Show();
        System.out.println("Write all: "+b.writee);
        System.out.println("Read all: "+b.readd);



/*
        try {
            FileOutputStream f = new FileOutputStream(new File("BTree3.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(b);
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }

 */
    }

    public void readTreeFromFile() {
        //String tName = "BTree2.txt";

        while(true) {
            System.out.println("Read tree from file");
            System.out.println("Choose option:");
            System.out.println("1: Add key and value");
            System.out.println("2: Update value at target key");
            System.out.println("3: Delete key");
            System.out.println("4: Show tree");
            System.out.println("5: Back");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String name = reader.readLine();
                if (name.equals("1")) {
                    System.out.println("Please enter key:");
                    String keyy = reader.readLine();
                    int key = Integer.parseInt(keyy);
                    System.out.println("Please enter value:");
                    String val = reader.readLine();
                    BTree b = new BTree(2, 0);
                    b.Insert(key, val);
                    //saveTree(b, tName);

                } else if (name.equals("2")) {
                    System.out.println("Please enter key:");
                    String keyy = reader.readLine();
                    int key = Integer.parseInt(keyy);
                    System.out.println("Please enter value:");
                    String val = reader.readLine();
                    BTree b = new BTree(2, 0);
                    b.UpdateValue(key, val);
                }
                else if (name.equals("3")) {
                    System.out.println("Work in progress");
                }
                else if (name.equals("4")) {
                    BTree bt = new BTree(2, 0);
                    bt.Show();
                }
                else if (name.equals("5")) {
                    //saveTree(b, tName);
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

        //String tName = "BTree.txt";
        while(true) {
            System.out.println("Read tree from file");
            System.out.println("Choose option:");
            System.out.println("1: Add key and value");
            System.out.println("2: Update value at target key");
            System.out.println("3: Delete key");
            System.out.println("4: Show tree");
            System.out.println("5: Back");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String name = reader.readLine();
                if (name.equals("1")) {
                    System.out.println("Please enter key:");
                    String keyy = reader.readLine();
                    int key = Integer.parseInt(keyy);
                    System.out.println("Please enter value:");
                    String val = reader.readLine();
                    BTree b = new BTree(2, 0);
                    b.Insert(key, val);
                    //saveTree(b, tName);

                } else if (name.equals("2")) {
                    System.out.println("Please enter key:");
                    String keyy = reader.readLine();
                    int key = Integer.parseInt(keyy);
                    System.out.println("Please enter value:");
                    String val = reader.readLine();
                    BTree b = new BTree(2, 0);
                    b.UpdateValue(key, val);
                }
                else if (name.equals("3")) {
                    System.out.println("Work in progress");
                }
                else if (name.equals("4")) {
                    BTree bt = new BTree(2, 0);
                    bt.Show();
                    //b.Show();
                }
                else if (name.equals("5")) {
                    //saveTree(b, tName);
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
