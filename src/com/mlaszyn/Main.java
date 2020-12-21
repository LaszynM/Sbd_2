package com.mlaszyn;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //System.out.println("test");
        BTree b = new BTree(2);
        b.Insert(8);
        b.Show();
        System.out.println();
        b.Insert(9);
        b.Show();
        System.out.println();
        b.Insert(10);
        b.Show();
        System.out.println();
        b.Insert(11);
        b.Show();
        System.out.println();
        b.Insert(15);
        b.Show();
        System.out.println();
        b.Insert(20);
        b.Show();
        System.out.println();
        b.Insert(17);
        b.Show();
        System.out.println();
        b.Insert(14);
        b.Show();
        System.out.println();
        b.Insert(18);

        b.Show();

    }
}
