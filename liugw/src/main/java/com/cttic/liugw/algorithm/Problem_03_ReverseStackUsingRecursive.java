package com.cttic.liugw.algorithm;

import java.util.Stack;

/**
 * 只用递归函数和栈操作倒置一个栈， 不能使用额外的数据结构
 * 两个递归
 *        ：倒置栈（：返回并移除栈底的元素）
 *
 */
public class Problem_03_ReverseStackUsingRecursive {

	public static void reverse(Stack<Integer> stack) {
		if (stack.isEmpty()) {
			return;
		}
		int i = getAndRemoveLastElement(stack);
		reverse(stack);
		stack.push(i);
	}

	public static int getAndRemoveLastElement(Stack<Integer> stack) {
		int result = stack.pop();
		if (stack.isEmpty()) {
			return result;
		} else {
			int last = getAndRemoveLastElement(stack);
			stack.push(result);
			return last;
		}
	}

	public static void main(String[] args) {
		Stack<Integer> test = new Stack<Integer>();
		test.push(1);
		test.push(2);
		test.push(3);
		test.push(4);
		test.push(5);
		Stack clone = (Stack)test.clone();
		while (!clone.isEmpty()) {
			System.out.print(clone.pop() + ",");
		}
		System.out.println("\n-------------------------------");
		reverse(test);
		clone = (Stack)test.clone();
		while (!clone.isEmpty()) {
			System.out.print(clone.pop() + ",");
		}

	}

}
