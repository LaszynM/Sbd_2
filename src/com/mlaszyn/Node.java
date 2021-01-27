package com.mlaszyn;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Node implements Serializable {
    private int number;
    private boolean isLeaf;
    int size;
    private int nodeId;
    int key[];
    //Node child[];
    //Node parent;
    int childOffset[];
    private int parentOffset;
    int valuesOffset[];


    public Node(int d, int id, int parentOffset) {

        size = 2*d;
        key = new int[size];
        //child = new Node[2*d+1];
        childOffset = new int[2*d+1];
        isLeaf = true;
        nodeId = id;
        this.parentOffset = parentOffset;
        //this.parent = parent;
        valuesOffset = new int[2*d];
    }

    public int getNumber() { return(number); }
    public int getNodeId() { return(nodeId); }
    public boolean getIsLeaf() { return(isLeaf); }
    public int getParentOffset() { return(parentOffset); }

    public void setNumber(int number) { this.number = number; }
    public void setParentOffset(int offs) { this.parentOffset = offs; }
    public void setLeaf(boolean isLeaf) { this.isLeaf = isLeaf; }
    public void setNodeId(int number) { this.nodeId = number; }


    public int FindKey(int k) {
        for (int i = 0; i < number; i++) {
            if(key[i] == k)
                return i;
        }
        return -1;
    };
}