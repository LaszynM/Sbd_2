package com.mlaszyn;

import java.io.*;

public class BTree implements Serializable{
    private int d;
    public int nodeNumber = 0;
    private Node root;
    public int kolor = 1;
    public int readCount;
    public int writeCount;


    public BTree(int d, int start) {
        this.d = d;
        nodeNumber = start + 1;
        root = new Node(d, nodeNumber);
        root.setD(d);
        root.setNumber(0);
        root.setLeaf(true);
        readCount = 0;
        writeCount = 0;
    }

    public void reorganizeFiles(Node node1, Node node2, Node node3, String valueToInsert, int keyToInsert) {
        try {
            File temp1 = new File("tmp1.txt");
            File temp2 = new File("tmp2.txt");
            File temp3 = new File("tmp3.txt");
            temp1.createNewFile();
            temp2.createNewFile();
            temp3.createNewFile();
            readCount++;

            String currentLine;
            String sub;
            int foundKey;
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(temp1));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(temp2));
            BufferedWriter bw3 = new BufferedWriter(new FileWriter(temp3));

            File node1File = new File(node1.getFileName());
            BufferedReader br1 = new BufferedReader(new FileReader(node1File));
            while((currentLine = br1.readLine()) != null) {
                sub = currentLine.substring(0, currentLine.indexOf(":"));
                foundKey = Integer.parseInt(sub);
                for (int i = 0; i < 2*d; i++) {
                    if (node1.getNumber() > i) {
                        if (node1.key[i] == foundKey) {
                            bw1.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                    if (node2.getNumber() > i) {
                        if (node2.key[i] == foundKey) {
                            bw2.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                    if (node3.getNumber() > i) {
                        if (node3.key[i] == foundKey) {
                            bw3.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                }
            }
            br1.close();

            File node2File = new File(node2.getFileName());
            BufferedReader br2 = new BufferedReader(new FileReader(node2File));
            while((currentLine = br2.readLine()) != null) {
                sub = currentLine.substring(0, currentLine.indexOf(":"));
                foundKey = Integer.parseInt(sub);
                for (int i = 0; i < 2*d; i++) {
                    if (node1.getNumber() > i) {
                        if (node1.key[i] == foundKey) {
                            bw1.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                    if (node2.getNumber() > i) {
                        if (node2.key[i] == foundKey) {
                            bw2.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                    if (node3.getNumber() > i) {
                        if (node3.key[i] == foundKey) {
                            bw3.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                }
            }
            br2.close();

            File node3File = new File(node3.getFileName());
            BufferedReader br3 = new BufferedReader(new FileReader(node3File));
            while((currentLine = br3.readLine()) != null) {
                sub = currentLine.substring(0, currentLine.indexOf(":"));
                foundKey = Integer.parseInt(sub);
                for (int i = 0; i < 2*d; i++) {
                    if (node1.getNumber() > i) {
                        if (node1.key[i] == foundKey) {
                            bw1.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                    if (node2.getNumber() > i) {
                        if (node2.key[i] == foundKey) {
                            bw2.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                    if (node3.getNumber() > i) {
                        if (node3.key[i] == foundKey) {
                            bw3.write(currentLine + System.getProperty("line.separator"));
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < 2*d; i++) {
                if (node1.getNumber() > i) {
                    if (node1.key[i] == keyToInsert) {
                        bw1.write(keyToInsert + ":" + valueToInsert + System.getProperty("line.separator"));
                        break;
                    }
                }
                if (node2.getNumber() > i) {
                    if (node2.key[i] == keyToInsert) {
                        bw2.write(keyToInsert + ":" + valueToInsert + System.getProperty("line.separator"));
                        break;
                    }
                }
                if (node3.getNumber() > i) {
                    if (node3.key[i] == keyToInsert) {
                        bw3.write(keyToInsert + ":" + valueToInsert + System.getProperty("line.separator"));
                        break;
                    }
                }
            }

            br3.close();

            bw1.close();
            writeCount++;
            bw2.close();
            writeCount++;
            bw3.close();
            writeCount++;

            node1File.delete();
            boolean success = temp1.renameTo(node1File);
            node2File.delete();
            success = temp2.renameTo(node2File);
            node3File.delete();
            success = temp3.renameTo(node3File);

        } catch (IOException io) {
            System.out.println("Something's wrong");
        }
    }

    public void insertIntoFile(int key, String value, String fileName) {
        try {
            writeCount++;
            File file = new File(fileName);
            FileWriter fr = new FileWriter(file, true);
            fr.write(key+":"+value+"\n");
            fr.close();
        } catch (IOException io) {
            System.out.println("File error!!! Can't write value to file");
        }
    }

    public String readValue(int key, String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine;
            while((currentLine = br.readLine()) != null) {
                if(currentLine.startsWith(key+":")) {
                    String val = currentLine.substring(currentLine.indexOf(":")+1);
                    br.close();
                    return val;
                }
            }
            br.close();
            return null;
        } catch (IOException io) {
            System.out.println("Can't find file");
        }
        return null;
    }

    public void readAllValues(String fileName, String[] tab) {

    }

    public void removeFromFile(int key, String fileName) {
        try {
            File file = new File(fileName);
            File filetmp = new File("tmp.txt");
            //file.createNewFile();
            filetmp.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(filetmp));
            String currentLine;
            while((currentLine = br.readLine()) != null) {
                if(currentLine.startsWith(key+":"))
                    continue;
                bw.write(currentLine + System.getProperty("line.separator"));

            }
            bw.close();
            br.close();
            file.delete();
            boolean success = filetmp.renameTo(file);
        } catch (IOException io) {
            System.out.println("File error!!! Can't write value to file");
        }
    }

    private Node searchNodes(Node node, int key) {
        if (node == null)
            return node;
        int i;
        readCount++;
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

    private void Split(Node node, int pos, Node node2, int key, String value) {
        nodeNumber++;
        Node node3 = new Node(d, nodeNumber);
        node3.setLeaf(node2.getIsLeaf());
        node3.setNumber(d);
        int temp[] = new int[2*d+1];
        boolean ins = false;
        for (int i = 0, j = 0; j < 2*d+1; j++) {
            if(i >= 2*d || (ins == false && node2.key[i] > key)) {
                temp[j] = key;
                ins = true;
            } else
        {
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
        reorganizeFiles(node, node2, node3, value, key);
    }

    public int Compensate(Node node, int cIndex, int key, String value) {
        if (cIndex > 0) {
            if (node.child[cIndex-1].getNumber() < 2*d) {

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
                reorganizeFiles(node, node.child[cIndex-1], node.child[cIndex], value, key);
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
                reorganizeFiles(node, node.child[cIndex], node.child[cIndex+1], value, key);
                return 1;
            }
            else{}
        }
        return 0;
    }

    public void Insert(final int key, String v) {
        readCount = 0;
        writeCount = 0;
        Node r = root;
        Node f = searchNodes(r, key);
        if(f != null) {
            System.out.println("Klucz juz istnieje");
            return;
        }
        if(r.getNumber() == 2*d) {
            nodeNumber++;
            Node newNode = new Node(d, nodeNumber);
            root = newNode;
            newNode.setLeaf(false);
            newNode.setNumber(0);
            newNode.child[0] = r;
            Split(newNode,0, r, key, v);
            //insertValue(newNode, key);
        }
        else
            insertValue(r, key, v);
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

    public void UpdateValue(int key, String value) {
        readCount = 0;
        writeCount = 0;
        Node r = root;
        Node f = searchNodes(r, key);
        if (f != null) {
            removeFromFile(key, f.getFileName());
            insertIntoFile(key, value, f.getFileName());
            return;
        }else
            System.out.println("Key not found");

    }

    private final void insertValue(Node node, int k, String v) {
        if(node.getIsLeaf() == true) {
            int i = 0;
            for (i = node.getNumber() - 1; i >= 0 && k < node.key[i]; i--) {
                node.key[i+1] = node.key[i];
            }
            node.key[i+1] = k;
            node.setNumber(node.getNumber()+1);
            insertIntoFile(k, v, node.getFileName());
        } else {
            int i = 0;
            for (i = node.getNumber() - 1; i >= 0 && k < node.key[i]; i--) {
            }
            i++;
            Node tmp = node.child[i];
            int comp = 0;
            if(tmp.getIsLeaf() == true && tmp.getNumber() == 2 * d) {
                comp = Compensate(node, i, k, v);
                if (comp == 0)
                    Split(node, i, tmp, k, v);
            }
            else if (tmp.getNumber() == 2 * d) {
                Split(node, i, tmp, k, v);
            }
            else
                insertValue(node.child[i], k, v);
        }

    }

    public void Show() {
        ShowNode(root, 1);
        System.out.println("Last operation read count: "+ readCount);
        System.out.println("Last operation write count: "+ writeCount);
        System.out.println("|------------------------------------------|");
    }

    public void ShowNode(Node node, int c) {
        assert (node == null);

        System.out.println("Node file: "+node.getFileName());
        for(int i = 0; i < node.getNumber(); i++) {
            System.out.print("Key: " + node.key[i] + " " + "Value:" + readValue(node.key[i], node.getFileName()) +   "\n");
        }
        System.out.println("Height from root:"+c);
        if (node.getIsLeaf() != true) {
            System.out.print("Children: ");
            for (int i = 0; i < node.getNumber() + 1; i++) {
                System.out.print(node.child[i].getFileName() + " ");
            }
        }
        else
            System.out.print("Leaf");
        System.out.println("\n");
        if(node.getIsLeaf() == false) {
            for (int i = 0; i < node.getNumber() + 1; i++) {
                ShowNode(node.child[i], c+1);
            }
        }
    }
}
