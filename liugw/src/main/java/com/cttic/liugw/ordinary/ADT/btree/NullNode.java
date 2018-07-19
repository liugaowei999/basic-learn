package com.cttic.liugw.ordinary.ADT.btree;

import com.cttic.liugw.ordinary.ADT.tree234.DataItem;
import com.cttic.liugw.ordinary.ADT.tree234.Node;

public class NullNode implements INode {

    @Override
    public void connectChild(int childPos, Node child) {
        // TODO Auto-generated method stub

    }

    @Override
    public Node disconnectChild(int childPos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node getChild(int childPos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isLeaf() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getNumItems() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public DataItem getItem(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isFull() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int findItem(DataItem dataItem) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertItem(DataItem newItem) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public DataItem removeItem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void displayNode() {
        // TODO Auto-generated method stub

    }

    @Override
    public void displayNodeValue() {
        // TODO Auto-generated method stub

    }

}
