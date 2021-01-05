package com.mlaszyn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class BTree implements Serializable{
    private int d;
    private Node root;
    public int kolor = 1;


    public BTree(int d) {
        this.d = d;
        root = new Node(d);
        root.setD(d);
        root.setNumber(0);
        root.setLeaf(true);

    }

    public void insertIntoFile(int key, String value) {
        try {
            File file = new File("Values.txt");
            FileWriter fr = new FileWriter(file, true);
            fr.write(key+";"+value+"\n");
            fr.close();
        } catch (IOException io) {
            System.out.println("File error!!! Can't write value to file");
        }
    }

    private Node searchNodes(Node node, int key) {
        if (node == null)
            return node;
        int i;
        for (i = 0; i < node.getNumber(); i++) {
            if (key < node.key[i]) {
                break;
            }
            if (key == node.key[i]) {
                return node;
            }
        }
        if (node.getIsLeaf() == true) {
            return null;
        } else {
            return searchNodes(node.child[i], key);
        }
    }

    private void Split(Node node, int pos, Node node2, int key) {
        Node node3 = new Node(d);
        node3.setLeaf(node2.getIsLeaf());
        node3.setNumber(d);
        int temp[] = new int[2*d+1];
        boolean ins = false;
        for (int i = 0, j = 0; j < 2*d+1; j++) {
            if(i >= 2*d || (ins == false && node2.key[i] > key)) {
                temp[j] = key;
                ins = true;
            } else {
                temp[j] = node2.key[i];
                i++;
            }
        }
        for (int i = 0; i < d; i++)
            node3.key[i] = temp[d+i+1];
        for (int i = 0; i < d; i++)
            node2.key[i] = temp[i];

        node2.setNumber(d);
        node3.setNumber(d);
//        for (int i = 0; i < d; i++) {
//            node3.key[i] = node2.key[d+i];
//        }
        if (node2.getIsLeaf() == false) {
            for (int i = 0; i < d+1; i++) {
                node3.child[i] = node2.child[d+i];
           }
       }
        node2.setNumber(d);
        for (int i = node.getNumber(); i >= pos+1; i--) {
            node.child[i+1] = node.child[i];
        }
        node.child[pos+1] = node3;
        for (int i = node.getNumber() - 1; i >= pos; i--){
            node.key[i+1] = node.key[i];
        }
        node.key[pos] = temp[d];
        node.setNumber(node.getNumber()+1);
    }

    public int Compensate(Node node, int cIndex, int key) {
        if (cIndex > 0) {
            if (node.child[cIndex-1].getNumber() < 2*d) {
                //node.child[cIndex-1].key[node.child[cIndex-1].getNumber()] = node.key[cIndex];

                int temp[] = new int[4 * d + 1];
                int j = 0;
                for (int i = 0; i < node.child[cIndex - 1].getNumber(); i++) {
                    temp[j] = node.child[cIndex - 1].key[i];
                    j++;
                }
                temp[j] = node.key[cIndex - 1];
                j++;
                int k = 0;
                boolean isKey = false;
                for (int i = 0; i < 2*d + 1; i++) {
                    if (key < node.child[cIndex].key[k] && isKey == false) {
                        temp[j] = key;
                        j++;
                        isKey = true;
                    } else {
                        temp[j] = node.child[cIndex].key[k];
                        j++;
                        k++;
                    }
                }
                node.child[cIndex].setNumber(j/2);
                for (int i = (j / 2 - 1); i >= 0; i--){
                    node.child[cIndex].key[i] = temp[j - 1];
                    j--;
                }
                node.key[cIndex-1] = temp[j - 1];
                j--;
                node.child[cIndex-1].setNumber(j);
                for(int i = j-1; i >= 0; i--) {
                    node.child[cIndex-1].key[i] = temp[j-1];
                    j--;
                }
                return 1;
            }
            else{}
        }
        if (cIndex < 2*d+1) {
            if (node.child[cIndex+1] != null && node.child[cIndex+1].getNumber() < 2*d) {
                int temp[] = new int[4 * d + 1];
                int j = 0, k = 0;
                boolean isKey = false;
                for(int i = 0; i < 2*
                        d + 1; i++) {
                    if(key < node.child[cIndex].key[k] && isKey == false) {
                        temp[j] = key;
                        j++;
                        isKey = true;
                    } else {
                        temp[j] = node.child[cIndex].key[k];
                        j++;
                        k++;
                    }
                }
                temp[j] = node.key[cIndex];
                j++;
                for (int i = 0; i < node.child[cIndex + 1].getNumber(); i++) {
                    temp[j] = node.child[cIndex + 1].key[i];
                    j++;
                }
                node.child[cIndex+1].setNumber(j/2);
                for (int i = (j / 2 - 1); i >= 0; i--){
                    node.child[cIndex+1].key[i] = temp[j - 1];
                    j--;
                }
                node.key[cIndex] = temp[j - 1];
                j--;
                node.child[cIndex].setNumber(j);
                for(int i = j-1; i >= 0; i--) {
                    node.child[cIndex].key[i] = temp[j-1];
                }
                return 1;
            }
            else{}
        }
        return 0;
    }

    public void Insert(final int key, String v) {
        Node r = root;
        Node f = searchNodes(r, key);
        if(f != null) {
            System.out.println("Klucz juz istnieje");
            return;
        }
        insertIntoFile(key, v);
        if(r.getNumber() == 2*d) {
            Node newNode = new Node(d);
            root = newNode;
            newNode.setLeaf(false);
            newNode.setNumber(0);
            newNode.child[0] = r;
            Split(newNode,0, r, key);
            //insertValue(newNode, key);
        }
        else
            insertValue(r, key);
    }

    public void DeleteValue(final int key) {
        Node r = root;
        Node f = searchNodes(r, key);
        if (f == null) {
            System.out.println("Key: " + key + " not found.");
            return;
        }
        if(r.getIsLeaf() == true) {
     //       for (int i = 0; i < r.getNumber(); i++)
    //            if(key == r.key[i])

        }
    }

    private final void insertValue(Node node, int k) {
        if(node.getIsLeaf() == true) {
            int i = 0;
            for (i = node.getNumber() - 1; i >= 0 && k < node.key[i]; i--) {
                node.key[i+1] = node.key[i];
            }
            node.key[i+1] = k;
            node.setNumber(node.getNumber()+1);
        } else {
            int i = 0;
            for (i = node.getNumber() - 1; i >= 0 && k < node.key[i]; i--) {
            }
            i++;
            Node tmp = node.child[i];
            int comp = 0;



            if(tmp.getIsLeaf() == true && tmp.getNumber() == 2 * d) {
                comp = Compensate(node, i, k);
                if (comp == 0)
                    Split(node, i, tmp, k);
            }
            else if (tmp.getNumber() == 2 * d) {
                Split(node, i, tmp, k);
            }
            else
                insertValue(node.child[i], k);
        }

    }



    /*public void Insert(int key) {
        int num = root.getNumber();
        if (num == 0) {
            root.key[0] = key;
            root.setNumber(1);
            return;
        }
        else if (root.getIsLeaf() == true) {
            if (num < 2*d)
                insertValue(root, key);
            else
        }
    }

     */
    public void Show() {
        ShowNode(root, 1);
    }

    public void ShowNode(Node node, int c) {
        assert (node == null);
        for(int i = 0; i < node.getNumber(); i++) {
            System.out.print(node.key[i] + " : " + c + ":K:"+ c +  "\n");
        }
        System.out.println(" ");
        if(node.getIsLeaf() == false) {
            for (int i = 0; i < node.getNumber() + 1; i++) {
                ShowNode(node.child[i], c+1);
            }
        }
    }
}
