package com.mlaszyn;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BTree implements Serializable{
    private int d;
    public int keyAmount;
    public int nodeNumber;
    private int bufferedNodes;

    private Node root;
    public int kolor = 1;
    public int readCount;
    private int rootId;
    public int writeCount;
    public int readd, writee;
    private int valueLength = 30;
    File file;
    Node[] nodeList;
    private int nodeSize = 68;
    int nodeBuffer = 40;


    public BTree(int d, int start) {
        this.d = d;
        bufferedNodes = 0;
        rootId = readRootOffset();
        nodeList = new Node[nodeBuffer];
        if (rootId < 0) {
            nodeList[0] = new Node(d, 0, -1);
            rootId = nodeList[0].getNodeId();
            root = nodeList[0];
            nodeNumber = 1;
            keyAmount = 0;
            bufferedNodes++;
        } else {
            nodeList[0] = new Node(d, 0, -1);
            readNode(rootId, nodeList[0]);
            root = nodeList[0];
            nodeNumber = readNodeAmount();
            keyAmount = readKeyAmount();
            bufferedNodes++;
        }
        //nodeNumber = start+1;
        //root = new Node(d, nodeNumber, null);
//        root.setNumber(0);
//        root.setLeaf(true);
        readCount = 0;
        writeCount = 0;
        readd = 0;
        writee = 0;
        file = new File("BTree.bin");
    }


    public int readRootOffset() {

        try {
            RandomAccessFile raf = new RandomAccessFile("BTree.bin", "r");
            raf.seek(0);
            byte[] bytes = new byte[4];
            raf.read(bytes);
            String value = new String(bytes);
            try {
                int num = Integer.parseInt(value);
                return (num);
            }catch(NumberFormatException nfe) {
                return(-1);
            }
        } catch (IOException io) {
            return(-1);
        }
    }


    public int readNodeAmount() {

        try {
            RandomAccessFile raf = new RandomAccessFile("BTree.bin", "r");
            raf.seek(4);
            byte[] bytes = new byte[4];
            raf.read(bytes);
            String value = new String(bytes);
            try {
                int num = Integer.parseInt(value);
                return (num);
            }catch(NumberFormatException nfe) {
                return(-1);
            }
        } catch (IOException io) {
            return(-1);
        }
    }


    public int readKeyAmount() {

        try {
            RandomAccessFile raf = new RandomAccessFile("BTree.bin", "r");
            raf.seek(8);
            byte[] bytes = new byte[4];
            raf.read(bytes);
            String value = new String(bytes);
            try {
                int num = Integer.parseInt(value);
                return (num);
            }catch(NumberFormatException nfe) {
                return(-1);
            }
        } catch (IOException io) {
            return(-1);
        }
    }


    public void writeTreeData() {
        try {
            RandomAccessFile raf = new RandomAccessFile("BTree.bin", "rw");
            raf.seek(0);
            String buffStr;
            buffStr = String.format("%04d", rootId);
            buffStr = buffStr + String.format("%04d", nodeNumber);
            buffStr = buffStr + String.format("%04d", keyAmount);
            raf.write(buffStr.getBytes());
            raf.close();
        } catch (IOException io) {
            System.out.println("error writing tree properties");
        }
    }


    public String readValue(int pos) {
        int jump = pos*valueLength;
        try {

            RandomAccessFile raf = new RandomAccessFile("Values.bin", "r");
            raf.seek(jump);
            byte[] bytes = new byte[valueLength];
            raf.read(bytes);
            String value = new String(bytes);
            if(value.contains("_")){
                String valueCut = value.substring(0, value.indexOf("_"));
                return valueCut;
            }else
                return value;
        } catch (IOException io) {
            return("File read error");
        }
    }


    public int writeValue(String str) {
        try {

            RandomAccessFile raf = new RandomAccessFile("Values.bin", "rw");
            raf.seek(raf.length());
            int pos = (int)raf.length()/valueLength;
            if(str.length() > valueLength) {
                String valueCut = str.substring(0, valueLength);
                raf.write(valueCut.getBytes());
            }
            else if(str.length() < valueLength) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                for (int i = str.length(); i < valueLength; i++) {
                    sb.append("_");
                }
                raf.write(sb.toString().getBytes());
            }
            else
                raf.write(str.getBytes());
            return pos;
        } catch (IOException io) {
            return(-1);
        }
    }


    public int writeValue(String str, int pos) {
        int jump = pos*valueLength;
        try {

            RandomAccessFile raf = new RandomAccessFile("Values.bin", "rw");
            raf.seek(jump);
            if(str.length() > valueLength) {
                String valueCut = str.substring(0, valueLength);
                raf.write(valueCut.getBytes());
            }
            else if(str.length() < valueLength) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                for (int i = str.length(); i < valueLength; i++) {
                    sb.append("_");
                }
                raf.write(sb.toString().getBytes());
            }
            else
                raf.write(str.getBytes());
            return pos;
        } catch (IOException io) {
            return(-1);
        }
    }


    public void readNode(int offset, Node node) {
        int jump = offset*nodeSize + 12;
        try {
            RandomAccessFile raf = new RandomAccessFile("BTree.bin", "r");
            raf.seek(jump);
            byte[] buffer = new byte[nodeSize];
            raf.read(buffer);
            String buffStr = new String(buffer);
            node.setNodeId(Integer.parseInt(buffStr.substring(0, 4)));
            node.setNumber(Integer.parseInt(buffStr.substring(4, 6)));
            node.setLeaf(Boolean.parseBoolean(buffStr.substring(6, 10)));
            node.size = Integer.parseInt(buffStr.substring(10, 12));
            for(int i = 0; i < 2*d; i++) {
                node.key[i] = Integer.parseInt(buffStr.substring(12+i*4, 16+i*4));
                node.childOffset[i] = Integer.parseInt(buffStr.substring(28+i*4, 32+i*4));
                node.valuesOffset[i] = Integer.parseInt(buffStr.substring(52+i*4, 56+i*4));
            }
            node.childOffset[2*d] = Integer.parseInt(buffStr.substring(44, 48));
            node.setParentOffset(Integer.parseInt(buffStr.substring(48, 52)));
            raf.close();
            /*
            File filetmp = new File("BTree.bin");
            List<String> line;
            BufferedReader reader = new BufferedReader(new FileReader(filetmp));
            line = reader.lines().skip(jump).limit(nodeSize).collect(Collectors.toList());

            node.setNumber(Integer.parseInt(line.get(0)));
            node.setLeaf(Boolean.parseBoolean(line.get(1)));
            node.size = Integer.parseInt(line.get(2));
            String keys = line.get(3);
            node.key = Arrays.stream(keys.substring(1, keys.length()-1).split(","))
                    .map(String::trim).mapToInt(Integer::parseInt).toArray();
            String children = line.get(4);
            node.childOffset = Arrays.stream(children.substring(1, children.length()-1).split(","))
                    .map(String::trim).mapToInt(Integer::parseInt).toArray();
            node.setParentOffset(Integer.parseInt(line.get(5)));
            String valOffs = line.get(6);
            node.valuesOffset = Arrays.stream(valOffs.substring(1, valOffs.length()-1).split(","))
                    .map(String::trim).mapToInt(Integer::parseInt).toArray();

             */
            readCount++;
        } catch (IOException io) {
            System.out.println("File error!!! Can't write value to file");
        }
    }


    public void writeNode(int offset, Node node) {
        int jump = offset * nodeSize + 12;
        try {
            RandomAccessFile raf = new RandomAccessFile("BTree.bin", "rw");
            raf.seek(jump);
            String buffStr;
            buffStr = String.format("%04d", node.getNodeId());
            buffStr = buffStr + String.format("%02d", node.getNumber());
            if(node.getIsLeaf() == false) {
                buffStr = buffStr + "fals";
            } else
                buffStr = buffStr + "true";
            buffStr = buffStr+String.format("%02d", node.size);
            for(int i = 0; i < 2*d; i++)
                buffStr = buffStr+String.format("%04d", node.key[i]);
            for(int i = 0; i < 5; i++)
                buffStr = buffStr+String.format("%04d", node.childOffset[i]);
            buffStr = buffStr+String.format("%04d", node.getParentOffset());
            for(int i = 0; i < 4; i++)
                buffStr = buffStr+String.format("%04d", node.valuesOffset[i]);
            raf.write(buffStr.getBytes());
            raf.close();
            writeCount++;
        } catch (IOException io) {
            System.out.println("File error!!! Can't write value to file");
        }

    }


   /* public String reorganizeTwoFiles(Node node1, Node node2, int keyToInsert, String valueToInsert, int keyToExtract) {
        try {
            File temp1 = new File("tmp1.txt");
            File temp2 = new File("tmp2.txt");
            temp1.createNewFile();
            temp2.createNewFile();
            String currentLine;
            String sub;
            String valueFound = null;
            int foundKey;
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(temp1));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(temp2));

            File node1File = new File(node1.getFileName());
            BufferedReader br1 = new BufferedReader(new FileReader(node1File));

            while((currentLine = br1.readLine()) != null) {
                readCount++;
                sub = currentLine.substring(0, currentLine.indexOf(":"));
                foundKey = Integer.parseInt(sub);
                if(foundKey == keyToExtract) {
                    valueFound = currentLine.substring(currentLine.indexOf(":")+1);
                    continue;
                }
                for (int i = 0; i < 2*d; i++) {
                    if (node1.getNumber() > i) {
                        if (node1.key[i] == foundKey) {
                            bw1.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                    if (node2.getNumber() > i) {
                        if (node2.key[i] == foundKey) {
                            bw2.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }

                }
            }
            br1.close();

            File node2File = new File(node2.getFileName());
            BufferedReader br2 = new BufferedReader(new FileReader(node2File));
            while((currentLine = br2.readLine()) != null) {
                readCount++;
                sub = currentLine.substring(0, currentLine.indexOf(":"));
                foundKey = Integer.parseInt(sub);
                if(foundKey == keyToExtract) {
                    valueFound = currentLine.substring(currentLine.indexOf(":")+1);
                    continue;
                }
                for (int i = 0; i < 2*d; i++) {
                    if (node1.getNumber() > i) {
                        if (node1.key[i] == foundKey) {
                            bw1.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                    if (node2.getNumber() > i) {
                        if (node2.key[i] == foundKey) {
                            bw2.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                }
            }
            br2.close();
            for (int i = 0; i < 2*d; i++) {
                if(keyToInsert == keyToExtract) {
                    valueFound = valueToInsert;
                    break;
                }
                if (node1.getNumber() > i) {
                    if (node1.key[i] == keyToInsert) {
                        bw1.write(keyToInsert + ":" + valueToInsert + System.getProperty("line.separator"));
                        writeCount++;
                        break;
                    }
                }
                if (node2.getNumber() > i) {
                    if (node2.key[i] == keyToInsert) {
                        bw2.write(keyToInsert + ":" + valueToInsert + System.getProperty("line.separator"));
                        writeCount++;
                        break;
                    }
                }
            }
            bw1.close();
            //writeCount++;
            bw2.close();
            //writeCount++;

            node1File.delete();
            boolean success = temp1.renameTo(node1File);
            node2File.delete();
            success = temp2.renameTo(node2File);

            return valueFound;
        } catch (IOException io) {
            System.out.println("Something's wrong");
            return null;
        }
    }


    public void reorganizeFiles(Node node1, Node node2, Node node3, String valueToInsert, int keyToInsert) {
        try {
            File temp1 = new File("tmp1.txt");
            File temp2 = new File("tmp2.txt");
            File temp3 = new File("tmp3.txt");
            temp1.createNewFile();
            temp2.createNewFile();
            temp3.createNewFile();
            //readCount++;

            String currentLine;
            String sub;
            int foundKey;
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(temp1));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(temp2));
            BufferedWriter bw3 = new BufferedWriter(new FileWriter(temp3));

            File node1File = new File(node1.getFileName());
            BufferedReader br1 = new BufferedReader(new FileReader(node1File));
            while((currentLine = br1.readLine()) != null) {
                readCount++;
                sub = currentLine.substring(0, currentLine.indexOf(":"));
                foundKey = Integer.parseInt(sub);
                for (int i = 0; i < 2*d; i++) {
                    if (node1.getNumber() > i) {
                        if (node1.key[i] == foundKey) {
                            bw1.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                    if (node2.getNumber() > i) {
                        if (node2.key[i] == foundKey) {
                            bw2.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                    if (node3.getNumber() > i) {
                        if (node3.key[i] == foundKey) {
                            bw3.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                }
            }
            br1.close();

            File node2File = new File(node2.getFileName());
            BufferedReader br2 = new BufferedReader(new FileReader(node2File));
            while((currentLine = br2.readLine()) != null) {
                readCount++;
                sub = currentLine.substring(0, currentLine.indexOf(":"));
                foundKey = Integer.parseInt(sub);
                for (int i = 0; i < 2*d; i++) {
                    if (node1.getNumber() > i) {
                        if (node1.key[i] == foundKey) {
                            bw1.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                    if (node2.getNumber() > i) {
                        if (node2.key[i] == foundKey) {
                            bw2.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                    if (node3.getNumber() > i) {
                        if (node3.key[i] == foundKey) {
                            bw3.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                }
            }
            br2.close();

            File node3File = new File(node3.getFileName());
            BufferedReader br3 = new BufferedReader(new FileReader(node3File));
            while((currentLine = br3.readLine()) != null) {
                readCount++;
                sub = currentLine.substring(0, currentLine.indexOf(":"));
                foundKey = Integer.parseInt(sub);
                for (int i = 0; i < 2*d; i++) {
                    if (node1.getNumber() > i) {
                        if (node1.key[i] == foundKey) {
                            bw1.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                    if (node2.getNumber() > i) {
                        if (node2.key[i] == foundKey) {
                            bw2.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                    if (node3.getNumber() > i) {
                        if (node3.key[i] == foundKey) {
                            bw3.write(currentLine + System.getProperty("line.separator"));
                            writeCount++;
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < 2*d; i++) {
                if (node1.getNumber() > i) {
                    if (node1.key[i] == keyToInsert) {
                        bw1.write(keyToInsert + ":" + valueToInsert + System.getProperty("line.separator"));
                        writeCount++;
                        break;
                    }
                }
                if (node2.getNumber() > i) {
                    if (node2.key[i] == keyToInsert) {
                        bw2.write(keyToInsert + ":" + valueToInsert + System.getProperty("line.separator"));
                        writeCount++;
                        break;
                    }
                }
                if (node3.getNumber() > i) {
                    if (node3.key[i] == keyToInsert) {
                        bw3.write(keyToInsert + ":" + valueToInsert + System.getProperty("line.separator"));
                        writeCount++;
                        break;
                    }
                }
            }

            br3.close();

            bw1.close();
            //writeCount++;
            bw2.close();
            //writeCount++;
            bw3.close();
            //writeCount++;

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
*/
    public String readValue(int key, String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine;
            while((currentLine = br.readLine()) != null) {
                //readCount++;
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
                readCount++;
                if(currentLine.startsWith(key+":"))
                    continue;
                bw.write(currentLine + System.getProperty("line.separator"));
                writeCount++;

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
        //readCount++;
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
            nodeList[bufferedNodes] = new Node(d, 0, -1);
            readNode(node.childOffset[i], nodeList[bufferedNodes]);
            bufferedNodes++;
            return searchNodes(nodeList[bufferedNodes-1], key);
        }
    }
/*
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

 */
    public void Split2(Node node, int key, String value) {

        nodeList[bufferedNodes] = new Node(d, nodeNumber, node.getParentOffset());
        Node newNode = nodeList[bufferedNodes];
        bufferedNodes++;
        nodeNumber++;
        //Node newNode = new Node(d, nodeNumber, node.parent);
        newNode.setLeaf(true);
        newNode.setNumber(d);
        boolean ins = false;
        int temp[] = new int[2 * d + 1];
        int tempValueOffset[] = new int [2 * d + 1];
        for(int i = 0, j = 0; j < 2*d+1; j++) {
            if(i >= 2*d || (ins == false && node.key[i] > key)) {
                temp[j] = key;
                tempValueOffset[j] = writeValue(value);
                ins = true;
            } else
            {
                temp[j] = node.key[i];
                tempValueOffset[j] = node.valuesOffset[i];
                i++;
            }
        }
        for (int i = 0; i < d; i++) {
            node.key[i] = temp[i];
            node.valuesOffset[i] = tempValueOffset[i];
            newNode.key[i] = temp[d+i+1];
            newNode.valuesOffset[i] = tempValueOffset[d+i+1];
        }
        node.setNumber(d);
        writeNode(node.getNodeId(), node);
        writeNode(newNode.getNodeId(), newNode);
        //String v = reorganizeTwoFiles(node, newNode, key, value, temp[d]);
        SplitParent((bufferedNodes-3), temp[d], newNode.getNodeId(), tempValueOffset[d]);
    }


    public void SplitParent(int nodeIndex, int key, int newChildId, int valueOffset) {
        Node node = nodeList[nodeIndex];
        if(node.getNumber() < 2*d) {
            int j;
            for(j = node.getNumber(); j > 0; j--) {
                if(key > node.key[j-1]) {
                    break;
                    //node.key[j] = key;
                    //node.setNumber(node.getNumber()+1);
                    //node.child[j+1] = newChild;
                } else {
                    node.key[j] = node.key[j-1];
                    node.valuesOffset[j] = node.valuesOffset[j-1];
                    node.childOffset[j+1] = node.childOffset[j];
                }
            }
            node.key[j] = key;
            node.valuesOffset[j] = valueOffset;
            node.setNumber(node.getNumber()+1);
            node.childOffset[j+1] = newChildId;
            //writeTreeData();
            writeNode(node.getNodeId(), node);
            //insertIntoFile(key, value, node.getFileName());
            //reorganizeFiles(node, node.child[j], node.child[j+1], value, key);
        } else if (node.getParentOffset() < 0) {
            //nowy root
            nodeList[bufferedNodes] = new Node(d, nodeNumber, -1);
            Node newR = nodeList[bufferedNodes];
            bufferedNodes++;
            nodeNumber++;
            //Node newR = new Node(d, nodeNumber, null);
            newR.setLeaf(false);
            node.setParentOffset(newR.getNodeId());
            root = newR;
            newR.childOffset[0] = node.getNodeId();
            newR.setNumber(1);
            //nowy somsiad
            nodeList[bufferedNodes] = new Node(d, nodeNumber, newR.getNodeId());
            Node newSib = nodeList[bufferedNodes];
            bufferedNodes++;
            nodeNumber++;
            //Node newSib = new Node(d, nodeNumber, newR);
            newR.childOffset[1] = newSib.getNodeId();
            newSib.setLeaf(false);

            int temp[] = new int[2*d+1];
            int tempValuesOffset[] = new int[2*d+1];
            int tempNodeOffset[] = new int[2*d+2];
            boolean ins = false;
            tempNodeOffset[0] = node.childOffset[0];

            for(int i = 0, j = 0; j < 2*d+1; j++) {
                if(i >= 2*d || (ins == false && node.key[i] > key)) {
                    temp[j] = key;
                    tempValuesOffset[j] = valueOffset;
                    tempNodeOffset[j+1] = newChildId;
                    ins = true;
                } else
                {
                    temp[j] = node.key[i];
                    tempValuesOffset[j] = node.valuesOffset[i];
                    tempNodeOffset[j+1] = node.childOffset[i+1];
                    i++;
                }
            }

            for (int i = 0; i < d; i++) {
                node.key[i] = temp[i];
                node.valuesOffset[i] = tempValuesOffset[i];
                newSib.key[i] = temp[d+i+1];
                newSib.valuesOffset[i] = tempValuesOffset[d+i+1];
            }
            newR.key[0] = temp[d];
            newR.valuesOffset[0] = tempValuesOffset[d];
            for (int i = 0; i <=d; i++) {
                node.childOffset[i] = tempNodeOffset[i];
                newSib.childOffset[i] = tempNodeOffset[i+d+1];

                nodeList[bufferedNodes] = new Node(d, 0, -1);
                readNode(newSib.childOffset[i], nodeList[bufferedNodes]);
                nodeList[bufferedNodes].setParentOffset(newSib.getNodeId());
                writeNode(nodeList[bufferedNodes].getNodeId(), nodeList[bufferedNodes]);

                //bufferedNodes++;
                //newSib.child[i].parent = newSib;
            }
            newSib.setLeaf(false);
            node.setNumber(d);
            newSib.setNumber(d);
            //writeTreeData();
            rootId = newR.getNodeId();
            root = newR;
            writeNode(node.getNodeId(), node);
            writeNode(newR.getNodeId(), newR);
            writeNode(newSib.getNodeId(), newSib);
            //for(int i = 0; i < bufferedNodes; i++) {
            //    writeNode(nodeList[i].getNodeId(), nodeList[i]);
            //}
            //reorganizeFiles(newR, node, newSib, value, key);


        } else {
            nodeList[bufferedNodes] = new Node(d, nodeNumber, node.getParentOffset());
            Node newSib = nodeList[bufferedNodes];
            bufferedNodes++;
            nodeNumber++;
            int temp[] = new int[2*d+1];
            int tempValuesOffset[] = new int[2*d+1];
            int tempNodeOffset[] = new int[2*d+2];
            boolean ins = false;

            tempNodeOffset[0] = node.childOffset[0];
            for(int i = 0, j = 0; j < 2*d+1; j++) {
                if(i >= 2*d || (ins == false && node.key[i] > key)) {
                    temp[j] = key;
                    tempValuesOffset[j] = valueOffset;
                    tempNodeOffset[j+1] = newChildId;
                    ins = true;
                } else
                {
                    temp[j] = node.key[i];
                    tempValuesOffset[j] = node.valuesOffset[i];
                    tempNodeOffset[j+1] = node.childOffset[i+1];
                    i++;
                }
            }

            for (int i = 0; i < d; i++) {
                node.key[i] = temp[i];
                node.valuesOffset[i] = tempValuesOffset[i];
                newSib.key[i] = temp[d+i+1];
                newSib.valuesOffset[i] = tempValuesOffset[d+i+1];
            }
            //newR.key[0] = temp[d];
            for (int i = 0; i <=d; i++) {
                node.childOffset[i] = tempNodeOffset[i];
                newSib.childOffset[i] = tempNodeOffset[i+d+1];
                nodeList[bufferedNodes] = new Node(d, 0, -1);
                readNode(newSib.childOffset[i], nodeList[bufferedNodes]);
                nodeList[bufferedNodes].setParentOffset(newSib.getNodeId());
                writeNode(nodeList[bufferedNodes].getNodeId(), nodeList[bufferedNodes]);
                //newSib.child[i].parent = newSib;
            }
            newSib.setLeaf(false);
            node.setNumber(d);
            newSib.setNumber(d);//--------------------------------------------------------------------------------------------------------------------\\
            writeNode(node.getNodeId(), node);
            writeNode(newSib.getNodeId(), newSib);
            //String v = reorganizeTwoFiles(node, newSib, key, value, temp[d]);
            SplitParent((nodeIndex-1), temp[d], newSib.getNodeId(), tempValuesOffset[d]);
        }
    }


    public void SplitRoot(Node node, int key, String value) {

        nodeList[bufferedNodes] = new Node(d, nodeNumber, -1);
        Node newR = nodeList[bufferedNodes];
        nodeNumber++;
        bufferedNodes++;
        newR.setNumber(1);
        newR.childOffset[0] = node.getNodeId();
        newR.setLeaf(false);
        node.setParentOffset(newR.getNodeId());
        root = newR;

        nodeList[bufferedNodes] = new Node(d, nodeNumber, newR.getNodeId());
        Node newSib = nodeList[bufferedNodes];
        nodeNumber++;
        bufferedNodes++;
        newSib.setNumber(d);
        node.setNumber(d);
        newR.childOffset[1] = newSib.getNodeId();
        int temp[] = new int[2*d+1];
        int tempValueOffset[] = new int [2*d+1];
        boolean ins = false;

        for(int i = 0, j = 0; j < 2*d+1; j++) {
            if(i >= 2*d || (ins == false && node.key[i] > key)) {
                temp[j] = key;
                tempValueOffset[j] = writeValue(value);
                keyAmount++;
                ins = true;
            } else
            {
                temp[j] = node.key[i];
                tempValueOffset[j] = node.valuesOffset[i];
                i++;
            }
        }

        for (int i = 0; i < d; i++) {
            node.key[i] = temp[i];
            node.valuesOffset[i] = tempValueOffset[i];
            newSib.key[i] = temp[d+i+1];
            newSib.valuesOffset[i] = tempValueOffset[d+i+1];
        }
        newR.key[0] = temp[d];
        newR.valuesOffset[0] = tempValueOffset[d];
        rootId = newR.getNodeId();
        //writeTreeData();
        for(int i = 0; i < bufferedNodes; i++) {
            writeNode(nodeList[i].getNodeId(), nodeList[i]);
        }
        //reorganizeFiles(newR, newSib, node, value, key);
    }


    public int Compensate(Node node, int key, String value) {
        int cIndex;
        for(cIndex = 0; cIndex < node.getNumber(); cIndex++) {
            if (key < node.key[cIndex]) {
                break;
            }
        }
        Node child = nodeList[bufferedNodes - 1];
        if (cIndex > 0) {
            nodeList[bufferedNodes] = new Node(d, 0, -1);
            readNode(node.childOffset[cIndex-1], nodeList[bufferedNodes]);
            Node leftSib = nodeList[bufferedNodes];
            bufferedNodes++;
            if (leftSib.getNumber() < 2*d) {
                int temp[] = new int[4 * d + 1];
                int tempValueOffset[] = new int[4 * d + 1];
                int j = 0;
                for (int i = 0; i < leftSib.getNumber(); i++) {
                    temp[j] = leftSib.key[i];
                    tempValueOffset[j] = leftSib.valuesOffset[i];
                    j++;
                }
                temp[j] = node.key[cIndex - 1];
                tempValueOffset[j] = node.valuesOffset[cIndex - 1];
                j++;
                int k = 0;
                boolean isKey = false;
                for (int i = 0; i < 2*d + 1; i++) {
                    if(k == 2*d && isKey == false) {
                       temp[j] = key;
                       tempValueOffset[j] = writeValue(value);
                       keyAmount++;
                        j++;
                        isKey = true;
                    }else if (key < child.key[k] && isKey == false) {
                        temp[j] = key;
                        tempValueOffset[j] = writeValue(value);
                        keyAmount++;
                        j++;
                        isKey = true;
                    } else {
                        temp[j] = child.key[k];
                        tempValueOffset[j] = child.valuesOffset[k];
                        j++;
                        k++;
                    }
                }
                child.setNumber(j/2);
                for (int i = (j / 2 - 1); i >= 0; i--){
                    child.key[i] = temp[j - 1];
                    child.valuesOffset[i] = tempValueOffset[j - 1];
                    j--;
                }
                node.key[cIndex-1] = temp[j - 1];
                node.valuesOffset[cIndex-1] = tempValueOffset[j-1];
                j--;
                leftSib.setNumber(j);
                for(int i = j-1; i >= 0; i--) {
                    leftSib.key[i] = temp[j-1];
                    leftSib.valuesOffset[i] = tempValueOffset[j-1];
                    j--;
                }
                writeNode(node.getNodeId(), node);
                writeNode(child.getNodeId(), child);
                writeNode(leftSib.getNodeId(), leftSib);
                //reorganizeFiles(node, node.child[cIndex-1], node.child[cIndex], value, key);
                return 1;
            }
            else{bufferedNodes--;}
        }
        if (cIndex < 2*d && cIndex < node.getNumber()) {
            nodeList[bufferedNodes] = new Node(d, 0, -1);
            readNode(node.childOffset[cIndex+1], nodeList[bufferedNodes]);
            Node rightSib = nodeList[bufferedNodes];
            bufferedNodes++;
            if (rightSib.getNumber() < 2*d) {
                int temp[] = new int[4 * d + 1];
                int tempValueOffset[] = new int[4 * d + 1];
                int j = 0, k = 0;
                boolean isKey = false;
                for(int i = 0; i < 2*d + 1; i++) {
                    if(k == 2*d && isKey == false) {
                        temp[j] = key;
                        tempValueOffset[j] = writeValue(value);
                        keyAmount++;
                        j++;
                        isKey = true;
                    }else if(key < child.key[k] && isKey == false) {
                        temp[j] = key;
                        tempValueOffset[j] = writeValue(value);
                        keyAmount++;
                        j++;
                        isKey = true;
                    } else {
                        temp[j] = child.key[k];
                        tempValueOffset[j] = child.valuesOffset[k];
                        j++;
                        k++;
                    }
                }
                temp[j] = node.key[cIndex];
                tempValueOffset[j] = node.valuesOffset[cIndex];
                j++;
                for (int i = 0; i < rightSib.getNumber(); i++) {
                    temp[j] = rightSib.key[i];
                    tempValueOffset[j] = rightSib.valuesOffset[i];
                    j++;
                }
                rightSib.setNumber(j/2);
                for (int i = (j / 2 - 1); i >= 0; i--){
                    rightSib.key[i] = temp[j - 1];
                    rightSib.valuesOffset[i] = tempValueOffset[j - 1];
                    j--;
                }
                node.key[cIndex] = temp[j - 1];
                node.valuesOffset[cIndex] = tempValueOffset[j - 1];
                j--;
                child.setNumber(j);
                for(int i = j-1; i >= 0; i--) {
                    child.key[i] = temp[j-1];
                    child.valuesOffset[i] = tempValueOffset[j-1];
                    j--;
                }
                writeNode(node.getNodeId(), node);
                writeNode(child.getNodeId(), child);
                writeNode(rightSib.getNodeId(), rightSib);
                //reorganizeFiles(node, node.child[cIndex], node.child[cIndex+1], value, key);
                return 1;
            }
            else{bufferedNodes--;}
        }
        return 0;
    }


    public void Insert(final int key, String v) {
        readCount = 1;
        writeCount = 0;
        System.out.println("Inserting: "+key);
        Node f = searchNodes(root, key);
        if(f != null) {
            System.out.println("Key already exists");
            return;
        }
        if(root.getNumber() == 2*d && root.getIsLeaf() == true)
            SplitRoot(nodeList[0], key, v);
        /*
        if(r.getNumber() == 2*d) {

            nodeNumber++;
            Node newNode = new Node(d, nodeNumber);
            root = newNode;
            newNode.setLeaf(false);
            newNode.setNumber(0);
            newNode.child[0] = r;
            Split(newNode,0, r, key, v);
            insertValue(newNode, key);
        }
        */
        else
            insertValue(nodeList[bufferedNodes-1], key, v);
        writeTreeData();
        bufferedNodes = 1;
        System.out.println("Read count: " + readCount + ", write count: " + writeCount);
    }
/*
    int Compensate2(Node node, int key) {
        Node parent = node.parent;
        int w;
        for (w  = 0; w < parent.getNumber()+1; w++) {
            if(parent.child[w] == node)
                break;
        }
        if(w > 0) {
            if (parent.child[w-1].getNumber() > d) {
                int temp[] = new int[3 * d];
                int j = 0;
                for (int i = 0; i < node.child[w - 1].getNumber(); i++) {
                    temp[j] = node.child[w - 1].key[i];
                    j++;
                }
                temp[j] = node.key[w - 1];
                j++;
                int k = 0;
                boolean isKey = false;
                for (int i = 0; i < 2*d + 1; i++) {
                    if(k == 2*d && isKey == false) {
                        temp[j] = key;
                        j++;
                        isKey = true;
                    }else if (key < node.child[cIndex].key[k] && isKey == false) {
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
                //reorganizeFiles(node, node.child[cIndex-1], node.child[cIndex], value, key);
                return 1;
            }
        }
        if (w < 2*d) {
            if(parent.child[w+1].getNumber() > d) {
                return 1;
            }
        }
        return 0;
    }


 */
    /*
    public void DeleteValue(final int key) {
        Node r = root;
        Node f = searchNodes(r, key);
        if (f == null) {
            System.out.println("Key: " + key + " not found.");
            return;
        }
        if(f.getIsLeaf() == true) {
            if(f.getNumber() > d) {
                int i;
                boolean found = false;
                for(i = 0; i <f.getNumber(); i++) {
                    if(key == f.key[i]) {
                        found = true;
                        f.setNumber(f.getNumber()-1);
                    }else if (found == true) {
                        f.key[i-1] = f.key[i];
                    }
                }
                removeFromFile(key, f.getFileName());
            } else
                System.out.println("Compensate here");
                //if (Compensate2(f, key) == 0) {}
                System.out.println("Merge here");

        }
    }

     */

    public void UpdateValue(int key, String value) {
        readCount = 0;
        writeCount = 0;
        Node r = root;
        Node f = searchNodes(r, key);
        if (f != null) {
            for(int i = 0; i < f.getNumber(); i++)
                if (key == f.key[i])
                    writeValue(value, f.valuesOffset[i]);
        }else
            System.out.println("Key not found");

    }


    private final void insertValue(Node node, int k, String v) {
        /*if(node.getIsLeaf() == false) {
            int i = 0;
            for (i = node.getNumber() - 1; i >= 0 && k < node.key[i]; i--) {
            }
            i++;
            Node tmp = node.child[i];
            int comp = 0;
            if(tmp.getIsLeaf() == true && tmp.getNumber() == 2 * d) {
                comp = Compensate(node, i, k, v);
                if (comp == 0)
                    Split2(tmp, k, v);
            }
            else
                insertValue(node.child[i], k, v);
        }
        else if(node.getIsLeaf() == true) {
            int i = 0;
            for (i = node.getNumber() - 1; i >= 0 && k < node.key[i]; i--) {
                node.key[i+1] = node.key[i];
                node.valuesOffset[i+1] = node.valuesOffset[i];
            }
            node.key[i+1] = k;
            node.valuesOffset[i+1] = writeValue(v);
            keyAmount++;
            node.setNumber(node.getNumber()+1);
        }

         */
        /*else if (node.getIsLeaf() == true && node.getNumber() >= 2*d) {

        }

        else {
            int i = 0;
            for (i = node.getNumber() - 1; i >= 0 && k < node.key[i]; i--) {
            }
            i++;
            Node tmp = node.child[i];
            int comp = 0;
            if(tmp.getIsLeaf() == true && tmp.getNumber() == 2 * d) {
                comp = Compensate(node, i, k, v);
                if (comp == 0)
                    System.out.print(" ");
  //                  Split(node, i, tmp, k, v);
            }
            else if (tmp.getNumber() == 2 * d) {
 //               Split(node, i, tmp, k, v);
            }
            else
                insertValue(node.child[i], k, v);
        }*/
        if(node.getIsLeaf() == true && node.getNumber() < 2*d) {
            int i = 0;
            for (i = node.getNumber() - 1; i >= 0 && k < node.key[i]; i--) {
                node.key[i+1] = node.key[i];
                node.valuesOffset[i+1] = node.valuesOffset[i];
            }
            node.key[i+1] = k;
            node.valuesOffset[i+1] = writeValue(v);
            keyAmount++;
            node.setNumber(node.getNumber()+1);
            writeNode(node.getNodeId(), node);
        } else {
            int comp = 0;
            Node parr = nodeList[bufferedNodes-2];
            comp = Compensate(parr, k, v);
            if(comp == 0) {
                Split2(node, k, v);
            }

        }


    }

    public void Show() {
        readNode(rootId, nodeList[0]);
        bufferedNodes++;
        readd = readCount;
        writee = writeCount;

        ShowNode(root, 1);
        bufferedNodes = 1;
        //System.out.println("Last operation read count: "+ readd);
        //System.out.println("Last operation write count: "+ writee);
        //readd = readd+readCount;
        //writee = writee + writeCount;
        System.out.println("|------------------------------------------|");
    }

    public void ShowNode(Node node, int c) {
        assert (node == null);

        System.out.println("Node id: "+node.getNodeId());
        for(int i = 0; i < node.getNumber(); i++) {
            System.out.print("Key: " + node.key[i] + ", Value:" + readValue(node.valuesOffset[i]) +   "\n");
        }
        System.out.println("Height:"+c);

        if(node.getParentOffset() > 0)
            System.out.println("Parent: "+node.getParentOffset());
        else
            System.out.println("Root");

        if (node.getIsLeaf() != true) {
            System.out.print("Children: ");
            for (int i = 0; i < node.getNumber() + 1; i++) {
                System.out.print(node.childOffset[i] + " ");
            }
        }
        else
            System.out.print("Leaf");
        System.out.println("\n");
        if(node.getIsLeaf() == false) {
            for (int i = 0; i < node.getNumber() + 1; i++) {
                nodeList[bufferedNodes] = new Node(d, 0, -1);
                readNode(node.childOffset[i], nodeList[bufferedNodes]);
                bufferedNodes++;
                ShowNode(nodeList[bufferedNodes-1], c+1);
            }
        }


    }
}
