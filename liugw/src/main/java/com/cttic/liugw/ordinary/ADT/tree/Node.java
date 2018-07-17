package com.cttic.liugw.ordinary.ADT.tree;

public class Node {

    private Data tData;
    public Node leftChild;
    public Node rightChild;

    public Node(Data data) {
        this.tData = data;
    }

    public Node(int iData, double dDdata) {
        this.tData = new Data(iData, dDdata);
    }

    public void display() {
        tData.display();
    }

    public int compareTo(Data key) {
        return tData.compareTo(key);
    }

    public int getKey() {
        return tData.iData;
    }
}
