package com.kkb.circleSimpleLinkedList;

import java.util.Scanner;
import java.util.Stack;

public class linkedListStack {
    public static void main(String[] args) {
        LinkedList list = new LinkedList(4);
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        String key = "";
        while (loop) {
            System.out.println("s:show, a:push, p:pop, e:exit");
            key = scanner.next();
            switch (key) {
                case "s":
                    list.list();
                    break;
                case "a":
                    System.out.println("请输入数字");
                    list.push(scanner.nextInt());
                    break;
                case "p":
                    try {
                        System.out.println("取出的数字是:" + list.pop());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "e":
                    loop = false;
                    scanner.close();
                    break;
                default:
                    break;
            }
        }
        System.out.println("程序结束");
    }
}

class LinkedList {
    private LinkedNode head = new LinkedNode(0);
    private int maxSize;
    private int top = -1;

    public LinkedList(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isFull() {
        return top == maxSize - 1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public void push(int num) {
        if (isFull()) {
            System.out.println("栈已满");
            return;
        }
        LinkedNode node = new LinkedNode(num);
        LinkedNode temp = head;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }
        temp.setNext(node);
        top++;
    }

    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("栈空");
        }
        LinkedNode temp = head;
        LinkedNode cur = temp.getNext();
        while (cur.getNext() != null) {
            temp = temp.getNext();
            cur = temp.getNext();
        }
        temp.setNext(null);
        top--;
        return cur.getNum();
    }

    public void list() {
        if (isEmpty()) {
            System.out.println("栈空");
            return;
        }
        LinkedNode temp = head.getNext();
        Stack<LinkedNode> stack = new Stack<>();
        while (temp != null) {
            stack.add(temp);
            temp = temp.getNext();
        }
        for (int i = top; i >= 0; i--) {
            System.out.printf("Stack[%d] = %d\n", i, stack.pop().getNum());
        }
    }
}


class LinkedNode {
    private int num;
    private LinkedNode next = null;

    public LinkedNode(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }


    public LinkedNode getNext() {
        return next;
    }

    public void setNext(LinkedNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "LinkedNode{" +
                "num=" + num +
                '}';
    }
}
