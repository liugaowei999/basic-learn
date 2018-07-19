package com.cttic.liugw.ordinary.ADT.tree234;

import com.cttic.liugw.ordinary.ADT.btree.INode;

/**
 * 
 * @author liugaowei
 *
 */
public class Node implements INode {
    private static final int ORDER = 4;

    private int numItems; // 当前节点保存有几个数据项
    private Node parent; // 当前节点的父节点
    private Node[] childArray = new Node[ORDER];
    private DataItem[] itemArray = new DataItem[ORDER - 1];

    /**
     * 连接一个子节点 到 当前节点
     * @param childpos
     * @param child
     */
    public void connectChild(int childPos, Node child) {
        childArray[childPos] = child;
        if (child != null) {
            child.parent = this;
        }
    }

    /**
     * 断开一个节点的某个子节点， 并返回这个子节点
     * @param childPos
     * @return
     */
    public Node disconnectChild(int childPos) {
        Node temp = childArray[childPos];
        //        if (temp != null) {
        //            temp.parent = null;
        //        }
        childArray[childPos] = null;
        return temp;
    }

    public Node getChild(int childPos) {
        return childArray[childPos];
    }

    /**
     * 获取当前节点的父节点
     * @return
     */
    public Node getParent() {
        return this.parent;
    }

    /**
     * 是否是叶子节点
     * @return
     */
    public boolean isLeaf() {
        return childArray[0] == null ? true : false;
    }

    public int getNumItems() {
        return this.numItems;
    }

    public DataItem getItem(int index) {
        return itemArray[index];
    }

    /**
     * 当前节点是否已经填满了数据项
     * @return
     */
    public boolean isFull() {
        return this.numItems == this.ORDER - 1 ? true : false;
    }

    /**
     * 查找一个数据对象
     * @param dataItem
     * @return 返回数据项所在的数组下标位置
     */
    public int findItem(DataItem dataItem) {
        for (int i = 0; i < ORDER - 1; i++) {
            if (itemArray[i] == null) {
                break;
            } else if (itemArray[i].compareTo(dataItem) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在当前节点插入一个数据对象
     * @param newItem
     * @return 返回插入的位置下标
     */
    public int insertItem(DataItem newItem) {
        // 假设数据项未满为前提
        numItems++;

        if (numItems == 1) {
            itemArray[0] = newItem;
            return 0;
        }

        // 从右开始遍历， 2, 1, 0
        for (int j = ORDER - 2; j >= 0; j--) {
            if (itemArray[j] == null) {
                continue;
            } else {
                // 新加入的节点小
                if (newItem.compareTo(itemArray[j]) < 0) {
                    itemArray[j + 1] = itemArray[j]; // 将原来的节点往右移动
                } else {
                    // 新加入的节点大
                    itemArray[j + 1] = newItem;
                    return j + 1;
                }
            }
        }
        itemArray[0] = newItem;
        return 0;
    }

    /**
     * 取出最大的一个数据项（移除并返回最大的数据项）
     * @return
     */
    public DataItem removeItem() {
        DataItem temp = itemArray[numItems - 1];
        itemArray[numItems - 1] = null;
        numItems--;
        return temp;
    }

    public void displayNode() {
        for (int i = 0; i < numItems; i++) {
            itemArray[i].displayItem();
        }
        System.out.println("/");
    }

    @Override
    public void displayNodeValue() {
        System.out.print("|");
        for (int i = 0; i < ORDER - 1; i++) {
            if (itemArray[i] != null) {
                System.out.print(itemArray[i].dData + "|");
            } else {
                System.out.print("--|");
            }
            //itemArray[i].displayItem();
        }
    }
}
