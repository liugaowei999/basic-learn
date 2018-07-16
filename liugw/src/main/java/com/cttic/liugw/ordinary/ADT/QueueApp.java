package com.cttic.liugw.ordinary.ADT;

import java.util.Random;

public class QueueApp {
    private static final int SIZE = 10;

    public static Queue getQueue() {
        Queue<Integer> queue = new Queue<>(SIZE);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < SIZE * 2; i++) {
            queue.insert(random.nextInt(SIZE * 100));
        }
        //        array.display();
        return queue;
    }

    public static void main(String[] args) {
        Queue<Integer> queue = getQueue();

        while (!queue.isEmpty()) {
            System.out.print(queue.remove() + " , ");
        }
    }

}
