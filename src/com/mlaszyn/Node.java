package com.mlaszyn;

public class Node{
    private int d;
    private int number;
    private String value;
    private boolean isLeaf;
    int size;
    int key[];
    Node child[];


    public Node(int d) {
        this.d = d;
        size = 2*d;
        key = new int[size];
        child = new Node[2*d+1];
        isLeaf = true;
    }

    public int getNumber() { return(number); }
    public String getValue() { return(value); }
    public boolean getIsLeaf() { return(isLeaf); }

    public void setD(int d) { this.d = d; }
    public void setNumber(int number) { this.number = number; }
    public void setValue(String value) { this.value = value; }
    public void setLeaf(boolean isLeaf) { this.isLeaf = isLeaf; }


    public int FindKey(int k) {
        for (int i = 0; i < number; i++) {
            if(key[i] == k)
                return i;
        }
        return -1;
    };
}