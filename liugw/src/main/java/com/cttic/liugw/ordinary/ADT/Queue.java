package com.cttic.liugw.ordinary.ADT;

public class Queue<T> {

    private int maxSize;
    private int front;
    private int rear;
    private int nItems;
    private Object[] queueArray;

    public Queue(int s) {
        maxSize = s;
        front = 0;
        rear = -1;
        nItems = 0;
        queueArray = new Object[s];
    }

    public boolean insert(T t) {
        if (isFull()) {
            return false;
        }
        if (rear == maxSize - 1) {
            rear = -1;
        }
        queueArray[++rear] = t;
        nItems++;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T remove() {
        if (isEmpty()) {
            return null;
        }
        Object temp = queueArray[front++];
        if (front == maxSize) {
            front = 0;
        }
        nItems--;
        return (T) temp;
    }

    @SuppressWarnings("unchecked")
    public T peekFront() {
        return (T) queueArray[front];
    }

    public boolean isEmpty() {
        return nItems == 0;
    }

    public boolean isFull() {
        return nItems == maxSize;
    }

    public int size() {
        return nItems;
    }

}
