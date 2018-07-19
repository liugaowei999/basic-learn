package com.cttic.liugw.ordinary.ADT.btree;

import com.cttic.liugw.ordinary.ADT.tree234.DataItem;
import com.cttic.liugw.ordinary.ADT.tree234.Node;

public interface INode {
    public void connectChild(int childPos, Node child);

    public Node disconnectChild(int childPos);

    public Node getChild(int childPos);

    public Node getParent();

    public boolean isLeaf();

    public int getNumItems();

    public DataItem getItem(int index);

    public boolean isFull();

    public int findItem(DataItem dataItem);

    public int insertItem(DataItem newItem);

    public DataItem removeItem();

    public void displayNode();

    public void displayNodeValue();
}
