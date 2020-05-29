package com.kkb.linkedList;

public class DoubleLinkedListDemo {
    public static void main(String[] args) {
        HeroNode2 heroNode1 = new HeroNode2(1, "宋江", "及时雨");
        HeroNode2 heroNode2 = new HeroNode2(2, "卢俊义", "玉麒麟");
        HeroNode2 heroNode3 = new HeroNode2(3, "吴用", "智多星");
        HeroNode2 heroNode4 = new HeroNode2(4, "鲁智深", "花和尚");
        HeroNode2 heroNode5 = new HeroNode2(5, "林冲", "豹子头");

        DoubleLinkedList linkedList = new DoubleLinkedList();
        linkedList.addByOrder(heroNode5);
        linkedList.addByOrder(heroNode1);
        linkedList.addByOrder(heroNode4);
        linkedList.addByOrder(heroNode2);
        linkedList.addByOrder(heroNode3);
        linkedList.showList();
        linkedList.update(new HeroNode2(5, "林冲1", "豹子头1"));
        System.out.println("修改后的链表");
        linkedList.showList();
        linkedList.delete(3);
        System.out.println("删除后的链表");
        linkedList.showList();

    }

}

class DoubleLinkedList {
    HeroNode2 head = new HeroNode2(0, "", "");


    //末尾新增
    public void add(HeroNode2 hero) {
        HeroNode2 temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = hero;
        hero.previous = temp;

    }

    //顺序添加
    public void addByOrder(HeroNode2 hero) {
        HeroNode2 temp = head;

        while (temp.next != null) {
            if (temp.no == hero.no) {
                System.out.printf("序号为 %d 的英雄 (%s) 已经存在,无法添加\n", temp.no, temp.name);
                return;
            } else if (temp.no > hero.no) {
                break;
            }
            temp = temp.next;
        }

        if (temp.next == null && temp.no < hero.no) {
            temp.next = hero;
            hero.previous = temp;
        } else {
            temp.previous.next = hero;
            hero.previous = temp.previous;
            hero.next = temp;
            temp.previous = hero;
        }
    }

    //修改
    public void update(HeroNode2 hero) {
        HeroNode2 temp = head.next;
        boolean flag = false;
        while (temp != null) {
            if (temp.no == hero.no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            temp.name = hero.name;
            temp.nikename = hero.nikename;
        } else {
            System.out.printf("没有序号为:%d 的英雄\n", hero.no);
        }
    }

    //删除
    public void delete(int no) {
        HeroNode2 temp = head.next;
        boolean flag = false;
        while (temp != null) {
            if (temp.no == no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            temp.previous.next = temp.next;
            if (temp.next != null) {
                temp.next.previous = temp.previous;
            }
        } else {
            System.out.printf("没有找到序号为: %d 的英雄\n", no);
        }
    }

    //查看链表
    public void showList() {
        HeroNode2 temp = head.next;
        while (temp != null) {
            System.out.println(temp);
            temp = temp.next;
        }
    }

}


class HeroNode2 {
    public int no;
    public String name;
    public String nikename;
    public HeroNode2 next;
    public HeroNode2 previous;

    public HeroNode2(int no, String name, String nikename) {
        this.no = no;
        this.name = name;
        this.nikename = nikename;

    }

    @Override
    public String toString() {
        return "HeroNode2{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nikename='" + nikename + '\'' +
                '}';
    }

}