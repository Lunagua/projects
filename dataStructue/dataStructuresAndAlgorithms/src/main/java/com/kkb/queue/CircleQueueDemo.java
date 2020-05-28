package com.kkb.queue;

import java.util.Scanner;

public class CircleQueueDemo {
    public static void main(String[] args) {
        CircleQueue circleQueue = new CircleQueue(3);
        Scanner scanner = new Scanner(System.in);
        boolean loop = true;
        char key;

        while (loop) {
            System.out.println("a(add),添加数据到队列");
            System.out.println("e(exit),退出程序");
            System.out.println("s(show),查看队列所以数据");
            System.out.println("h(head),查看队列头部数据");
            System.out.println("g(get),从队列提取数据");

            key = scanner.next().charAt(0);
            switch (key) {
                case 'a':
                    System.out.print("输入数字:");
                    circleQueue.addData(scanner.nextInt());
                    break;
                case 'h':
                    try {
                        System.out.printf("队列头部的数据为: %d\n", circleQueue.headQueue());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'e':
                    loop = false;
                    break;
                case 'g':
                    try {
                        System.out.printf("队列中提取的数据为: %d\n", circleQueue.getData());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 's':
                    circleQueue.showList();
                    break;
                default:
                    break;
            }
        }
        System.out.println("退出程序!");

    }
}


class CircleQueue {
    private int rear;   //指向队列尾部的后一个位置
    private int front;  //指向队列头部位置
    private int maxSize;    //队列最大容量
    private int arr[];  //队列所在数组

    //构造器
    public CircleQueue(int maxSize) {
        this.maxSize = maxSize + 1;
        arr = new int[this.maxSize];
    }

    public boolean isFull() {
        //判断队列满的条件为(rear + 1) % maxSize == front
        return (rear + 1) % maxSize == front;
    }

    public boolean isEmpty() {
        //判断是否为空的条件front == rear
        return front == rear;
    }

    public void addData(int n) {
        //插入数据,先判断是否满
        if (isFull()) {
            System.out.println("队列已满,无法插入...");
            return;
        }
        arr[rear] = n;
        rear = (rear + 1) % maxSize;
    }

    public int getData() {
        //提取数据,先判断是否队列空
        if (isEmpty()) {
            throw new RuntimeException("队列为空,无法提取数据...");
        }
        int value = arr[front];
        front = (front + 1) % maxSize;
        return value;
    }

    public void showList() {
        if (isEmpty()) {
            System.out.println("队列为空...");
            return;
        }
        for (int i = front; i < front + size(); i++) {
            System.out.printf("arr[%d] = %d\n", i % maxSize, arr[i % maxSize]);
        }
    }

    public int size() {
        return (rear - front + maxSize) % maxSize;
    }

    public int headQueue() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空...");
        }
        return arr[front];
    }
}