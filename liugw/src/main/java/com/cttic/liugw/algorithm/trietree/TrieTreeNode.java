package com.cttic.liugw.algorithm.trietree;

public class TrieTreeNode {
    // 字符集的大小，这里默认字符都是小写英文字母
    private static final Integer MAX_SIZE = 26;
    int WordCount;          //用于记录以该节点结尾的单词出现的次数
    int PrefixCount;        //用于记录以该节点结尾的前缀出现的次数
    char ch;                //该节点的字符值
    TrieTreeNode parent;   //指向的父节点指针，一般来说不需要，但为了后面高效的遍历树并统计词频所增加的
    TrieTreeNode[] child;   //指向孩子的指针数组

    TrieTreeNode(){
        WordCount = 0;
        PrefixCount = 0;
        child = new TrieTreeNode[MAX_SIZE];
        parent = null;
        for (int i = 0; i < MAX_SIZE; ++i) {
            child[i] = null;
        }
    }
}
