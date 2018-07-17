package com.cttic.liugw.ordinary.ADT.tree;

import java.util.Stack;

/**
 * 树的条件： 从根到一个节点的路径有且只有一条 
 * 
 * @author liugaowei
 *
 */
// 二叉搜索树
public class Tree {
    private Node root;

    public Tree() {
        root = null;
    }

    /**
     * 查重
     * @param key
     * @return
     */
    public Node find(Data key) {
        //        if (root == null) {
        //            return null;
        //        }

        Node current = root;
        while (current.compareTo(key) != 0) {
            if (current.compareTo(key) > 0) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    /**
     * 添加数据
     * 
     * @param data
     */
    public void insert(Data data) {
        Node newNode = new Node(data);
        Node current = root;
        Node parent = current;

        if (root == null) {
            root = newNode;
            return;
        }

        while (true) {
            parent = current;
            // 当前节点值 比要插入的值大， 新节点应在左子树中插入
            if (current.compareTo(data) > 0) {
                current = current.leftChild;
                if (current == null) {
                    parent.leftChild = newNode;
                    return;
                }
            } else {
                current = current.rightChild;
                if (current == null) {
                    parent.rightChild = newNode;
                    return;
                }
            }
        }
    }

    /**
     * 删除数据
     * 
     * @param data
     * @return
     */
    public boolean delete(Data data) {
        Node current = root;
        Node parent = root;
        boolean isLeftChild = true;

        // 不相等继续找到要删除的节点
        while (current.compareTo(data) != 0) {
            parent = current; // 记录父节点
            if (current.compareTo(data) > 0) {
                current = current.leftChild;
                isLeftChild = true;
            } else {
                current = current.rightChild;
                isLeftChild = false;
            }
            // 如果没有找到节点， 则认为删除失败
            if (current == null) {
                return false;
            }
        }

        // 找到节点current是要删除的节点， parent是current的直接父节点
        if (current.leftChild == null && current.rightChild == null) {
            // 要删除的节点 是叶子节点， 直接删除
            if (current == root)
                root = null;
            else if (isLeftChild)
                parent.leftChild = null;
            else
                parent.rightChild = null;
        } else if (current.rightChild == null) {
            // 如果没有右子树， 直接将current节点替换为左子树
            if (current == root) {
                root = current.leftChild;
            } else if (isLeftChild)
                parent.leftChild = current.leftChild;
            else
                parent.rightChild = current.leftChild;
        } else if (current.leftChild == null) {
            // 如果没有左子树， 直接将current节点替换为右子树
            if (current == root) {
                root = current.rightChild;
            } else if (isLeftChild)
                parent.leftChild = current.rightChild;
            else {
                parent.rightChild = current.rightChild;
            }
        } else {
            // 两课子树都有， current的位置替换为 current 的中序后继节点
            // 1. 先处理并获取当前要删除节点的后继节点
            Node sucessor = ProcessSucessor(current);

            // 2. 将后继节点替换掉当前节点， 即：将后继节点，连接到当前要删除节点的父节点上
            if (current == root) {
                root = sucessor;
            } else if (isLeftChild)
                parent.leftChild = sucessor;
            else {
                parent.rightChild = sucessor;
            }

            // 3. 将当前要删除节点的左子树连接到 后继节点的左边
            sucessor.leftChild = current.leftChild;
        }
        return true;
    }

    /**
     * 处理要删除节点的后继节点
     * 
     * @param delNode
     * @return
     */
    private Node ProcessSucessor(Node delNode) {
        Node sucessor = delNode;
        Node sucessorParent = delNode;
        Node current = delNode.rightChild; // 选中要删除节点的右子树， 后继节点就在这里面

        while (current != null) {
            // 当到达最有一个没有左孩子的节点时， sucessor就是后继节点， sucessorParent 是后继节点的父节点
            sucessorParent = sucessor;
            sucessor = current;
            current = current.leftChild;
        }

        if (sucessor != delNode.rightChild) {
            // 后继节点不是当前节点的右孩子， 说明 右边是课子树
            // 腾空后继节点的右孩子位置， 将后继节点的右子树挂到（后继节点挪走后， 其父节点的左孩子正好空了）
            sucessorParent.leftChild = sucessor.rightChild;

            // 将当前要删除节点的右子树 ， 挂在后继几点的右边
            sucessor.rightChild = delNode.rightChild;
        }

        return sucessor;
    }

    /**
     * 按照中序遍历，获取后继节点
     * 当前节点的后继节点为 右子树的左孩子
     * @param current
     * @return
     */
    private Node getSucessor(Node current) {

        //        Node successor = current;
        Node parent = current;
        Node successor = current.rightChild;

        while (successor != null) {
            parent = successor;
            successor = successor.leftChild;
        }

        return parent;
    }

    /**
     * 遍历二叉搜索树
     * @param traverseType
     * 1：前序遍历
     * 2：中序遍历
     * 3：后续遍历
     */
    public void traverse(int traverseType) {
        switch (traverseType) {
        case 1:
            System.out.println("前序遍历");
            preOrder(root);
            break;
        case 2:
            System.out.println("中序遍历");
            inOrder(root);
            break;
        case 3:
            System.out.println("后序遍历");
            postOrder(root);
            break;
        }
    }

    private void postOrder(Node localRoot) {
        if (localRoot != null) {
            postOrder(localRoot.leftChild);
            postOrder(localRoot.rightChild);
            System.out.println(localRoot.getKey() + " , ");
        }
    }

    private void inOrder(Node localRoot) {
        if (localRoot != null) {
            postOrder(localRoot.leftChild);
            System.out.println(localRoot.getKey() + " , ");
            postOrder(localRoot.rightChild);
        }
    }

    private void preOrder(Node localRoot) {
        if (localRoot != null) {
            System.out.println(localRoot.getKey() + " , ");
            postOrder(localRoot.leftChild);
            postOrder(localRoot.rightChild);
        }
    }

    /**
     * ========================================================================================
     */
    public void displayTree() {
        Stack<Node> globalStack = new Stack<>();
        globalStack.push(root);
        int nBlanks = 64; // 2*2*2*2*2 --> 最大支持5层的打印

        boolean isRowEmpty = false;
        System.out.println("-----------------------------------------------------------------");

        while (!isRowEmpty) {
            Stack<Node> localStack = new Stack<>();
            isRowEmpty = true;
            for (int i = 0; i < nBlanks; i++) {
                System.out.print(" ");
            }

            while (!globalStack.isEmpty()) {
                Node temp = globalStack.pop();
                if (temp != null) {
                    System.out.print(temp.getKey());
                    localStack.push(temp.leftChild);
                    localStack.push(temp.rightChild);

                    if (temp.leftChild != null || temp.rightChild != null) {
                        isRowEmpty = false;
                    }
                } else {
                    // 当前节点为null， 打印--代表节点为空
                    System.out.print("--");

                    // 当前节点为空节点，其左右孩子节点也一定为空， 加入两个null代表空孩子
                    localStack.push(null);
                    localStack.push(null);
                }

                for (int i = 0; i < nBlanks * 2 - 2; i++) {
                    System.out.print(" ");
                }
            } // end 【while globalStack is not empty】
            System.out.println();
            nBlanks /= 2;
            while (!localStack.isEmpty()) {
                globalStack.push(localStack.pop());
            }
        } // end [while isRow not Empty]
        System.out.println("-----------------------------------------------------------------");
    }// end displayTree

}
