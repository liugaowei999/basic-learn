package com.cttic.liugw.ordinary.ADT.btree;

public class Data {

    public int iData;
    public double dData;

    public Data(int iData, double dDdata) {
        this.iData = iData;
        this.dData = dDdata;
    }

    public void display() {
        System.out.println("{" + iData + " , " + dData + "}");
    }

    @Override
    public String toString() {
        return "{" + iData + " , " + dData + "}";
    }

    public int compareTo(Data key) {
        if (key == this || this.iData == key.iData) {
            return 0;
        }
        if (this.iData >= key.iData) {
            return 1;
        } else {
            return -1;
        }
    }

}
