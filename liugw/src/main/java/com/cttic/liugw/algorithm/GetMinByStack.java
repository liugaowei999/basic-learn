package com.cttic.liugw.algorithm;


import java.util.Stack;

public class GetMinByStack {
    private Stack<Integer> stackData;
    private Stack<Integer> stackMin;

    public GetMinByStack() {
        stackData = new Stack<>();
        stackMin  = new Stack<>();
    }

    public void push(int newNum) {
        if (this.stackMin.isEmpty()) {
            stackMin.push(newNum);
        } else if (newNum <= getMin()) {
            stackMin.push(newNum);
        }
        stackData.push(newNum);
    }

    public int pop() {
        if (stackData.isEmpty()) {
            throw new RuntimeException("Your stack is empty");
        }
        int value = stackData.pop();
        if (value == getMin()) {
            stackMin.pop();
        }
        return value;
    }

    private int getMin() {
        if (stackMin.isEmpty()) {
            throw new RuntimeException("Your stack is empty");
        }
        return stackMin.peek();
    }

    public static void main(String[] args) {
        GetMinByStack stack1 = new GetMinByStack();
        stack1.push(3);
        System.out.println(stack1.getMin());
        stack1.push(4);
        System.out.println(stack1.getMin());
        stack1.push(1);
        System.out.println(stack1.getMin());
        System.out.println(stack1.pop());
        System.out.println(stack1.getMin());
    }
}
