package com.cttic.liugw.ordinary.ADT.tree234;

import java.util.Queue;

import com.cttic.liugw.ordinary.ADT.btree.INode;
import com.cttic.liugw.ordinary.ADT.btree.NullNode;

/**
 * 效率和红黑树相当
 * 
 * 但存储利用率比红黑树低
 * @author liugaowei
 *
 */
public class Tree234 {
    private Node root = new Node();

    /**
     * 如果找到，返回下标
     * @param dataItem
     * @return
     */
    public int find(DataItem dataItem) {
        Node current = root;
        int childPos;

        while (true) {
            if ((childPos = current.findItem(dataItem)) >= 0) {
                // 当前节点找到了这个数据项，返回所在的数组下标
                return childPos;
            } else if (current.isLeaf()) {
                // 当前节点没有找到这个数据项，并且是叶子节点，查找失败
                return -1;
            } else {
                current = getNextChildNode(current, dataItem);
            }
        }

    }

    private Node getNextChildNode(Node current, DataItem dataItem) {
        int childPos;
        int itemCount = current.getNumItems();
        for (childPos = 0; childPos < itemCount; childPos++) {
            if (dataItem.compareTo(current.getItem(childPos)) < 0) {
                // 在当前节点中找到比dataItem大的一个数据项， 那么说明就应该到对应的子节点中去找
                return current.getChild(childPos); // 从左子节点中去找
            }
        }
        return current.getChild(childPos);// 如果比最后一个数据项还大，则返回右子节点
    }

    /**
     * 插入一个数据项
     * 
     * @param dataItem
     */
    public void insert(DataItem dataItem) {
        Node curNode = root;
        while (true) {

            if (curNode.isFull()) {
                // 当前节点如果数据项已满， 则首先分裂当前节点
                split(curNode);
                curNode = curNode.getParent();

                // 从分离后的新的父节点开始，重新查找可以插入的位置节点
                curNode = getNextChildNode(curNode, dataItem);
            } else if (curNode.isLeaf()) {
                // 如果当前节点时叶子节点， 则在这个节点插入数据
                break;
            } else {
                // 当前节点数据项未满，且不是叶子节点，继续往下找合适的节点
                curNode = getNextChildNode(curNode, dataItem);
            }
        }
        curNode.insertItem(dataItem);
    }

    /**
     * 分裂满节点
     * @param curNode
     */
    private void split(Node curNode) {

        DataItem itemB, itemC;
        Node parent, child2, child3;
        int itemIndex;

        // 先将当前节点进行拆分，然后将拆分项分别放到新建的节点或父节点中去
        itemC = curNode.removeItem();
        itemB = curNode.removeItem();
        parent = curNode.getParent();
        child2 = curNode.disconnectChild(2);
        child3 = curNode.disconnectChild(3);
        Node newRightNode = new Node();

        // 判断当前要分裂的节点是不是根节点
        if (curNode == root) {
            // 根节点分裂： 分裂成3个： 新建1个新的根节点存储B， 新建1个右子节点存储C， A保留在原来的节点中
            Node newRoot = new Node();
            newRoot.connectChild(0, curNode);
            root = newRoot;
            parent = root;
        } else {
            parent = curNode.getParent();
        }

        // 这里开始，不管当前节点原来是不是根节点， 现在它都已经变成了普通节点， 后面的处理逻辑都一样了
        // 将B数据项上移保存到父节点中去
        itemIndex = parent.insertItem(itemB);
        int itemCount = parent.getNumItems();

        // itemIndex为新建的右节点挂接到父节点的位置，挂接之前应该先为其腾出地方。 这部分逻辑放到Node中去实现更好
        for (int j = itemCount - 1; j > itemIndex; j--) {
            Node disconnectChild = parent.disconnectChild(j);
            parent.connectChild(j + 1, disconnectChild);
        }
        parent.connectChild(itemIndex + 1, newRightNode);

        // 处理右节点
        newRightNode.insertItem(itemC); // 插入数据项C
        newRightNode.connectChild(0, child2);
        newRightNode.connectChild(1, child3);
    }

    public void displayTree() {
        recDisplayTree(root, 0, 0);
        recDisplayTree1(root);
    }

    private void recDisplayTree(Node curNode, int level, int childPos) {
        System.out.print("level=" + level + ",childPositon=" + childPos + " ");
        curNode.displayNode();

        int numItems = curNode.getNumItems();
        for (int i = 0; i < numItems + 1; i++) {
            Node nextNode = curNode.getChild(i);
            if (nextNode != null) {
                recDisplayTree(nextNode, level + 1, i);
            } else {
                return;
            }
        }
    }

    private void recDisplayTree1(Node curNode) {
        int level = 0;
        Queue<INode> totalQueue = new java.util.ArrayDeque<>();
        Queue<INode> queueTemp = new java.util.ArrayDeque<>();

        queueTemp.add(curNode);
        queueTemp.add(new NullNode());
        INode temp;

        // 将所有节点 按层次按顺序 放入 Totalqueue
        while ((!queueTemp.isEmpty()) && !(queueTemp.size() == 1 && (queueTemp.peek() instanceof NullNode))) {
            temp = queueTemp.poll();
            totalQueue.add(temp);
            if (temp instanceof NullNode) {
                // 说明当前层次的所有元素已经遍历完
                queueTemp.add(new NullNode()); // 加入分层标志，开启新的一层遍历
                level++;
                System.out.println("@@ level=" + level);
                continue;
            }

            int numItems = temp.getNumItems();
            for (int i = 0; i <= numItems; i++) {
                Node nextNode = temp.getChild(i);
                if (nextNode != null) {
                    queueTemp.add(nextNode);
                } else {
                    break;
                }
            }
        }
        System.out.println("========================== level=" + level);
        // dipaly
        while (!totalQueue.isEmpty()) {
            INode poll = totalQueue.poll();
            if (poll instanceof NullNode) {
                System.out.println();
                level--;
            } else {
                for (int i = 0; i < level * 12 / 2 + 1; i++) {
                    System.out.print("    ");
                }
                poll.displayNodeValue();
            }
        }

    }
}
