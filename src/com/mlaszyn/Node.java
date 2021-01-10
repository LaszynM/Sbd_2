package com.mlaszyn;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Node implements Serializable {
    private int d;
    private int number;
    private String fileName;
    private boolean isLeaf;
    int size;
    int key[];
    Node child[];
    Node parent;


    public Node(int d, int id, Node parent) {
        fileName = "Node"+id+".txt";
        try {
            File old = new File(fileName);
            old.delete();
            File file = new File(fileName);
            file.createNewFile();
        } catch (IOException io) {
            System.out.println("Error creating node file");
        }
        this.d = d;
        size = 2*d;
        key = new int[size];
        child = new Node[2*d+1];
        isLeaf = true;
        this.parent = parent;
    }

    public int getNumber() { return(number); }
    public String getFileName() { return(fileName); }
    public boolean getIsLeaf() { return(isLeaf); }

    public void setD(int d) { this.d = d; }
    public void setNumber(int number) { this.number = number; }
    //public void setValue(String value) { this.value = value; }
    public void setLeaf(boolean isLeaf) { this.isLeaf = isLeaf; }


    public int FindKey(int k) {
        for (int i = 0; i < number; i++) {
            if(key[i] == k)
                return i;
        }
        return -1;
    };
}