package com.cttic.liugw.utils;

/**
 * 注意：不支持多线程操作
 * 
 * @author liugaowei
 *
 * @param <T>
 */
public class Stack<T> {
    private int top = -1;
    private Object[] stack;
    private float factor = 2;

    public Stack(int capacity) throws RuntimeException {
        if (capacity < 0)
            throw new RuntimeException("Illegal capacity:" + capacity);
        stack = new Object[capacity];
        System.out.println(stack.length);
    }

    public void push(T obj) {
        if (top == stack.length - 1) {
            System.out.println("Stack is full! Start to expend .......");
            Object[] newStack = new Object[(int) (stack.length * factor)];
            System.arraycopy(stack, 0, newStack, 0, stack.length);
            stack = newStack;
            System.out.println("Stack is full! The size expended to " + stack.length);
        }
        // throw new Exception("Stack is full!");
        stack[++top] = obj;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (top == -1)
            throw new RuntimeException("Stack is empty!");
        return (T) stack[top--];
    }

    public void dispaly() {
        System.out.print("bottom -> top: | ");
        for (int i = 0; i <= top; i++) {
            System.out.print(stack[i] + " | ");
        }
        System.out.print("\n");
    }

    public boolean isEmpty() {
        return top == -1 ? true : false;
    }

    public static void main(String[] args) throws Exception {
        Stack<Integer> s = new Stack<>(3);
        s.push(1);
        s.push(2);
        s.dispaly();
        System.out.println("=========================================1");
        System.out.println(s.pop());
        s.dispaly();
        System.out.println("=========================================2");
        s.push(99);
        s.dispaly();
        System.out.println("=========================================3");
        s.push(99);
        s.push(10);
        s.push(11);
        s.push(12);
        s.push(13);
        s.dispaly();
        System.out.println("=========================================");
    }
}