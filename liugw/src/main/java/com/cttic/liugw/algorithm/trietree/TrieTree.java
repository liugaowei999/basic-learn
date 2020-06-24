package com.cttic.liugw.algorithm.trietree;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TrieTree {
    private static final Integer MAX_SIZE = 26;
    private TrieTreeNode root;

    public TrieTree() {
        root = new TrieTreeNode();
    }

    // 向树插入新单词
    void insert(String word){
        if (word.length() == 0) {  return; }
        insert(root, word.toCharArray());
    }

    private void insert(TrieTreeNode p, char[] word){
        for (char aWord : word) {
            int pos = aWord - 'a';
            if (p.child[pos] == null) {
                p.child[pos] = new TrieTreeNode();
                p.child[pos].ch = aWord;
                p.child[pos].parent = p;
            }
            p.child[pos].PrefixCount++;
            p = p.child[pos];
        }
        p.WordCount++;
    }

    //给定某个单词，返回其在文本中出现的次数
    int findCount(String word){
        if (word.length() == 0){ return -1; }
        return findCount(root, word.toCharArray());
    }

    private int findCount(TrieTreeNode p, char[] word){
        for (char aWord : word) {
            int pos = aWord - 'a';
            if (p.child[pos] == null) {
                return 0;
            }
            p = p.child[pos];
        }
        return p.WordCount;
    }

    //给定某个前缀，返回其在文本中出现的次数
    int findPrefix(String prefix){
        if (prefix.length() == 0) {
            return -1;
        }
        return findPrefix(root, prefix.toCharArray());
    }

    private int findPrefix(TrieTreeNode p, char[] prefix){
        for (char aPrefix : prefix) {
            int pos = aPrefix - 'a';
            if (p.child[pos] == null) {
                return 0;
            }
            p = p.child[pos];
        }
        return p.PrefixCount;
    }

    //统计文本中出现的所有单词及出现的次数
    Map<String, Integer> WordFrequency(){
        Map<String, Integer> tank = new HashMap<>();
        countFrequency(root, tank);
        return tank;
    }

    private void countFrequency(TrieTreeNode p, Map<String, Integer> tank){
        if (p == null) {
            return;
        }
        if (p.WordCount > 0) {
            tank.putAll(getWordCountAtNode(p));
        }
        for (int i = 0; i < MAX_SIZE; ++i){
            countFrequency(p.child[i], tank);
        }
    }

    private Map<String, Integer> getWordCountAtNode(TrieTreeNode p){
        int count = p.WordCount;
        String word = "";
        Stack<Character> S = new Stack<>();
        do{
            S.push(p.ch);
            p = p.parent;
        } while (p.parent != null);
        while (!S.empty()){
            word += String.valueOf(S.pop());
            S.pop();
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put(word, count);
        return resultMap;
    }


    public static void main(String[] args) {

        List<String> words = Lists.newArrayList("hello", "he", "he", "huhe", "hehe");
        TrieTree tree = new TrieTree();
        for (String word : words) {
            tree.insert(word);
        }

        int count=tree.findCount("he");
        System.out.printf("he: %d\n",count);
        count=tree.findCount("he");
        System.out.printf("he: %d\n",count);

        int prefixCount=tree.findPrefix("he");
        System.out.printf("prefix \"he\": %d\n",prefixCount);
    }
}
