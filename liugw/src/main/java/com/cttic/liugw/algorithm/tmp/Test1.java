package com.cttic.liugw.algorithm.tmp;

import java.util.*;

/**
 * @author liugw
 * @Package com.cttic.liugw.algorithm.tmp
 * @Description: ${TODO}
 * @date 2020/6/15 12:42
 */
public class Test1 {
    public static void main(String[] args) {
        Test1 test1 = new Test1();
        Solution solution = new Solution();
        System.out.println(solution.letterMap.get(2));
        System.out.println(solution.letterCombinations("234"));
    }
    static class Solution {
        Map<Integer, List<String>> letterMap = new HashMap<>();
        private Solution() {
            letterMap.put(2, new ArrayList<String>(Arrays.asList("a","b","c")));
            letterMap.put(3, new ArrayList<String>(Arrays.asList("d","e","f")));
            letterMap.put(4, new ArrayList<String>(Arrays.asList("g","h","i")));
            letterMap.put(5, new ArrayList<String>(Arrays.asList("j","k","l")));
            letterMap.put(6, new ArrayList<String>(Arrays.asList("m","n","o")));
            letterMap.put(7, new ArrayList<String>(Arrays.asList("p","q","r","s")));
            letterMap.put(8, new ArrayList<String>(Arrays.asList("t","u","v")));
            letterMap.put(9, new ArrayList<String>(Arrays.asList("w","x","y","z")));
        }

        List<String> result = new ArrayList<>();

        private List<String> letterCombinations(String digits) {
            if (digits.length() <=0) {
                return result;
            }
            result = getLetters(digits);
            return result;
        }

        private List<String> getLetters(String digits) {
            List<List<String>> allLists = new ArrayList<>();
            int len = digits.length();
            for (int i=0; i<len; i++) {
                Integer c = Integer.valueOf(digits.substring(i,i+1));
                List<String> oneNum = letterMap.get(c);
                allLists.add(oneNum);
            }

            getwordList(allLists, 0, "");
            return result;
        }

        private void getwordList(List<List<String>> numList, int idxList, String word) {
            int size = numList.size();
            if (idxList >= size) {
                result.add(word);
                return;
            }

            List<String> numStr = numList.get(idxList);
            for (String aNumStr : numStr) {
                String word1 = word + aNumStr;
                getwordList(numList, idxList + 1, word1);
            }
        }


    }
}
