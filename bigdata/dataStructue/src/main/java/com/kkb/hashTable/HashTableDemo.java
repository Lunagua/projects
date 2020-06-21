package com.kkb.hashTable;

public class HashTableDemo {
    public static void main(String[] args) {
        Employee zhangsan = new Employee(1, "zhangsan", "北京");
        Employee lisi = new Employee(2, "lisi", "北京");
        Employee wangwu = new Employee(3, "wangwu", "北京");
        Employee zhaoliu = new Employee(4, "zhaoliu", "北京");
        Employee tangqi = new Employee(16, "zhaoliu", "北京");
        Employee muba = new Employee(24, "zhaoliu", "北京");
        Employee zhaojiu = new Employee(9, "zhaoliu", "北京");
//        EmployeeLinkedList linkedList = new EmployeeLinkedList();
//        linkedList.add(zhangsan);
//        linkedList.add(lisi);
//        linkedList.add(wangwu);
//        linkedList.add(zhaoliu);
//        linkedList.list();
//        linkedList.delete(3);
//        linkedList.delete(1);
//        linkedList.delete(2);
//        linkedList.delete(4);
//        linkedList.list();
        EmployeeHashTable hashTable = new EmployeeHashTable(7);
        hashTable.add(zhangsan);
        hashTable.add(lisi);
        hashTable.add(tangqi);
        hashTable.add(muba);
        hashTable.add(zhaojiu);
        hashTable.add(wangwu);
        hashTable.add(zhaoliu);
        hashTable.list();
        hashTable.find(24);
        hashTable.find(22);
    }
}

class EmployeeHashTable {
    private EmployeeLinkedList[] table;
    private int size;

    public EmployeeHashTable(int rowNum) {
        table = new EmployeeLinkedList[rowNum];
        size = rowNum;
        for (int i = 0; i < size; i++) {
            table[i] = new EmployeeLinkedList();
        }
    }

    public void add(Employee employee) {
        table[hash(employee.getId())].add(employee);
    }

    public void delete(int id) {
        table[hash(id)].delete(id);
    }

    public void list() {
        for (int i = 0; i < size; i++) {
            table[i].list(i + 1);
        }
    }

    public void find(int id) {
        table[hash(id)].find(id);
    }

    public int hash(int id) {
        return id % size;
    }
}

class EmployeeLinkedList {
    private Employee head = new Employee(0, "", "");

    public void add(Employee employee) {
        Employee temp = head;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }
        temp.setNext(employee);
    }

    public void find(int id) {
        Employee temp = head;
        boolean flag = false;
        while (temp.getNext() != null) {
            if (temp.getNext().getId() == id) {
                flag = true;
                break;
            }
            temp = temp.getNext();
        }
        if (flag) {
            System.out.println(temp.getNext().toString());
        } else {
            System.out.println("id为 " + id + " 的雇员不存在");
        }
    }

    public void delete(int id) {
        Employee temp = head;
        boolean flag = false;
        while (temp.getNext() != null) {
            if (temp.getNext().getId() == id) {
                flag = true;
                break;
            }
            temp = temp.getNext();
        }
        if (flag) {
            temp.setNext(temp.getNext().getNext());
        } else {
            System.out.println("id为 " + id + " 的雇员不存在");
        }
    }

    public void list() {
        if (head.getNext() == null) {
            System.out.println("链表为空");
            return;
        }
        Employee temp = head.getNext();
        while (temp != null) {
            System.out.println(temp.toString());
            temp = temp.getNext();
        }
    }

    public void list(int num) {
        if (head.getNext() == null) {
            System.out.printf("第 %d 条链表为空\n", num);
            return;
        }
        Employee temp = head.getNext();
        System.out.printf("第 %d 条链表:", num);
        while (temp != null) {
            System.out.printf("  =>  %s", temp.toString());
            temp = temp.getNext();
        }
        System.out.println();
    }
}


class Employee {
    private int id;
    private String name;
    private String address;
    private Employee next;

    public Employee(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Employee getNext() {
        return next;
    }

    public void setNext(Employee next) {
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}