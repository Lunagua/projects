package com.kkb.linkedList;

import java.util.Stack;

public class SingleLinkedListDemo {
    public static void main(String[] args) {
        HeroNode heroNode6 = new HeroNode(16, "宋江", "及时雨");
        HeroNode heroNode7 = new HeroNode(27, "卢俊义", "玉麒麟");
        HeroNode heroNode8 = new HeroNode(38, "吴用", "智多星");
        HeroNode heroNode9 = new HeroNode(49, "鲁智深", "花和尚");
        HeroNode heroNode10 = new HeroNode(50, "林冲", "豹子头");
        HeroNode heroNode1 = new HeroNode(1, "宋江", "及时雨");
        HeroNode heroNode2 = new HeroNode(2, "卢俊义", "玉麒麟");
        HeroNode heroNode3 = new HeroNode(3, "吴用", "智多星");
        HeroNode heroNode4 = new HeroNode(4, "鲁智深", "花和尚");
        HeroNode heroNode5 = new HeroNode(5, "林冲", "豹子头");

        SingleLinkedList singleLinkedList = new SingleLinkedList();
        SingleLinkedList singleLinkedList1 = new SingleLinkedList();

        singleLinkedList.addByOrder(heroNode1);
        singleLinkedList.addByOrder(heroNode2);
        singleLinkedList.addByOrder(heroNode3);
        singleLinkedList.addByOrder(heroNode4);
        singleLinkedList.addByOrder(heroNode5);
        singleLinkedList1.addByOrder(heroNode6);
        singleLinkedList1.addByOrder(heroNode7);
        singleLinkedList1.addByOrder(heroNode8);
        singleLinkedList1.addByOrder(heroNode9);
        singleLinkedList1.addByOrder(heroNode10);
        singleLinkedList.showList();
        System.out.println("========");
        singleLinkedList1.showList();
        System.out.println("========");
        System.out.println("合并");
        HeroNode res = merge(singleLinkedList1.getHead(),singleLinkedList.getHead());
        while (res != null){
            System.out.println(res);
            res =res.next;
        }

        System.out.println("========");
        singleLinkedList1.showList();

        System.out.println("========");
        singleLinkedList.showList();
    }

    public static HeroNode merge(HeroNode head1, HeroNode head2) {
        HeroNode sll = new HeroNode(0, "", "");
        HeroNode temp1 = head1.next;
        HeroNode temp2 = head2.next;
        HeroNode cur = sll;

        while (temp1 != null && temp2 != null) {
            if (temp1.no < temp2.no) {
                cur.next = temp1;
                temp1 = temp1.next;
            } else {
                cur.next = temp2;
                temp2 = temp2.next;
            }
            cur = cur.next;
        }
        cur.next = (temp1 != null) ? temp1 : temp2;

        return sll.next;
    }
}

//创建单向链表类
class SingleLinkedList {
    //创建头结点
    private HeroNode head = new HeroNode(0, "", "");

    //翻转链表
//    public void reverse(){
//        /**
//         * 思路:
//         * 1.创建一个新的reverseHead,用于存放翻转后的链表
//         * 2.创建辅助变量temp,用于衔接head和未被取走的链表
//         * 3.创建辅助变量ac用于存放从head链表取出来的结点,
//         * 4.遍历并依次取出head链表的每个结点,放入reverseHead链表的前面,也就是将reverseHead的next置为ac
//         * 5.将ac的next置为reverseHead的next
//         * 6.遍历完成后,用head取代reverseHead,
//         */
//        HeroNode reverseHead = new HeroNode(0,"","");   //新建reverseHead
//        HeroNode cur = head.next;  //辅助变量指向head的下一个结点
//        HeroNode next;    //辅助变量
//        while (true){
//            if (head.next == null){     //如果head链表已经取完,则退出循环
//                break;
//            }
//            /**
//             * 本过程完成了以下步骤:
//             * 1.从head取出next的结点,放入ac,
//             * 2.head 指向next的next
//             * 3.将ac放入reverseHead的next,ac的next被reverseHead原有的next取代(也就是把ac插入reverseHead和其链表之间)
//             *
//             */
//            next = cur;      //ac指向temp所在的位置
//            cur = next.next; //temp位置后移
//            next.next = reverseHead.next; //ac指向新链表的下一个结点
//            reverseHead.next = next;      //新链表的头指向ac
//            head.next = cur;           //head链表指向temp所在的位置
//        }
//        head = reverseHead;
//    }
    //翻转链表
    public void reverse() {
        if (head.next == null || head.next.next == null) {
            return;
        }
        HeroNode reverseHead = new HeroNode(0, "", "");
        HeroNode cur = head.next;
        HeroNode next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = reverseHead.next;
            reverseHead.next = cur;
            cur = next;
        }
        head = reverseHead;
    }

    //添加结点
    public void add(HeroNode heroNode) {
        //头结点不能动,创建辅助变量
        HeroNode temp = head;
        //遍历链表找到next==null的结点,并将新结点加入
        while (true) {
            if (temp.next == null) {
                break;
            }
            temp = temp.next;
        }
        temp.next = heroNode;   //找到next为null的结点后,将其next指向heroNode
    }

    //顺序添加结点
    public void addByOrder(HeroNode heroNode) {
        //头结点不能动,使用辅助变量temp
        HeroNode temp = head;
        //使用flag标记是否找到与传入的结点序号一致
        boolean flag = false;

        while (true) {
            if (temp.next == null) {  //next结点为null,代表到了链表末尾
                break;
            }
            if (temp.next.no > heroNode.no) { //如果下一个结点的序号大于插入的结点序号,则可以插入
                break;
            } else if (temp.next.no == heroNode.no) { //如果找到下一个结点的序号和传入的结点序号相同,修改flag为true,并退出循环
                flag = true;
                break;
            }
            temp = temp.next;   //指针后移
        }
        if (flag) {
            System.out.printf("需要插入的英雄: %s, 序号: %d 已经存在,无法插入\n", heroNode.name, heroNode.no);
        } else {     //完成插入动作
            heroNode.next = temp.next;
            temp.next = heroNode;
        }
    }

    //查询链表长度
    public int length() {
        int len = 0;
        HeroNode temp = head;
        while (temp.next != null) {
            len++;
            temp = temp.next;
        }
        return len;
    }

    //删除结点
    public void delete(int no) {
        HeroNode temp = head;
        boolean flag = false;   //使用flag判断是否找到待删除结点
        while (true) {
            if (temp.next == null) {
                break;
            }
            if (temp.next.no == no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            temp.next = temp.next.next;
        } else {
            System.out.printf("该结点: %d 不存在\n", no);
        }
    }

    //显示所有结点
    public void showList() {
        HeroNode temp = head;
        while (true) {
            if (temp.next == null) {
                break;
            }
            temp = temp.next;
            System.out.println(temp);
        }
    }

    //修改结点
    public void modify(HeroNode heroNode) {
        HeroNode temp = head;
        boolean flag = false;
        while (true) {
            if (temp.next == null) {
                break;
            }
            if (temp.next.no == heroNode.no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            temp.next.name = heroNode.name;
            temp.next.nickname = heroNode.nickname;
        } else {
            System.out.printf("待修改的结点: %d 不存在\n", heroNode.no);
        }
    }

    //查询结点
    public HeroNode search(int no) {
        HeroNode temp = head;
        boolean flag = false;
        while (true) {
            if (temp.next == null) {
                break;
            }
            if (temp.no == no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            return temp;
        } else {
            throw new RuntimeException("查找的结点:" + no + " 不存在");
        }
    }

    //倒序下标查找
    public HeroNode searchReverse(int index) {
        HeroNode temp = head;
        int len = length();
        int order = 0;
        if (len < index || index <= 0) {
            return null;
        }
        while (order <= len - index) {
            temp = temp.next;
            order++;
        }
        return temp;
    }

    //    //倒序打印,通过模拟栈的方式
//    public void showReverseList() {
//        HeroNode arr[] = new HeroNode[length()];
//        int front = 0;
//        HeroNode temp = head.next;
//        while (temp != null) {
//            arr[front] = temp;
//            front++;
//            temp = temp.next;
//        }
//        for (int i = length() - 1; i >= 0; i--) {
//            System.out.println(arr[i]);
//        }
//    }
    //倒序输出,栈的方法
    public void showReverseList() {
        Stack<HeroNode> stack = new Stack<>();
        HeroNode temp = head.next;
        while (temp != null) {
            stack.push(temp);
            temp = temp.next;
        }
        while (stack.size() > 0) {
            System.out.println(stack.pop());
        }
    }

    public HeroNode getHead() {
        return head;
    }

    public void merge(HeroNode head) {
        HeroNode temp = this.head.next;
        HeroNode cur = head.next;
        HeroNode next;

        while (cur != null) {
            next = cur.next;
            cur.next = null;
            addByOrder(cur);
            cur = next;
        }
    }

}

//创建英雄结点类
class HeroNode {
    public int no;
    public String name;
    public String nickname;
    public HeroNode next;   //指向下一个结点

    public HeroNode(int no, String name, String nickname) {
        this.no = no;
        this.name = name;
        this.nickname = nickname;
    }

    //重写toSting方法
    @Override
    public String toString() {
        return "HeroNode{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
