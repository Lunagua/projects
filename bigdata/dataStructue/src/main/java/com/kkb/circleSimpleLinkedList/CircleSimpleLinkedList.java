package com.kkb.circleSimpleLinkedList;

public class CircleSimpleLinkedList {
    public static void main(String[] args) {
        HeroNode heroNode1 = new HeroNode(1, "1");
        CircleLinkedList list = new CircleLinkedList(heroNode1);
//        list.addMany(5);
//        list.showList();
        list.josephIssue(10,20,125);
    }
}


class CircleLinkedList {
    private HeroNode first;

    public CircleLinkedList(HeroNode first) {
        this.first = first;
        this.first.setNext(first);
    }

    /**
     * 出圈顺序
     *
     * @param startNo  开始数的位置
     * @param countNum 一次数几个
     * @param sum      总共多少个结点
     */

    public void josephIssue(int startNo, int countNum, int sum) {
        if (startNo < 1 || countNum < 1 || startNo > sum) {
            System.out.println("输入的参数有误,请重新输入");
            return;
        }
        addMany(sum - 1);
        HeroNode temp = first;
        for (int i = 1; i < startNo; i++) {
            first = first.getNext();
        }
        while (temp.getNext() != first) {
            temp = temp.getNext();
        }

        while (temp != first) {
            for (int i = 1; i < countNum; i++) {
                first = first.getNext();
                temp = temp.getNext();
            }
            System.out.printf("%s 出圈\n",first);
            first = first.getNext();
            temp.setNext(first);
        }
        System.out.printf("%s 出圈\n",first);

    }

    /**
     * 一次添加多个结点
     *
     * @param num 添加结点的个数
     */
    private void addMany(int num) {
        HeroNode cur;
        HeroNode temp = first;
        for (int i = first.getNo() + 1; i <= first.getNo() + num; i++) {
            cur = new HeroNode(i, "" + i);
            temp.setNext(cur);
            cur.setNext(first);
            temp = temp.getNext();
        }
    }

    /**
     * 遍历并打印链表的所有元素
     */
    public void showList() {
        HeroNode temp = first;
        while (true) {
            System.out.println(temp);
            temp = temp.getNext();
            if (temp == first) {
                break;
            }
        }
    }


}

class HeroNode {
    private int no;
    private String name;
    private HeroNode next;

    public HeroNode(int no, String name) {
        this.no = no;
        this.name = name;
    }

    @Override
    public String toString() {
        return "HeroNode{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HeroNode getNext() {
        return next;
    }

    public void setNext(HeroNode next) {
        this.next = next;
    }
}
