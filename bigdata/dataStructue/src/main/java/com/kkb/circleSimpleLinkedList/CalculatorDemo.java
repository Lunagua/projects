package com.kkb.circleSimpleLinkedList;

public class CalculatorDemo {
    public static void main(String[] args) {
        String expression = "1900*20+20*60-20-30*10+30/2";
        ArrayStack numStack = new ArrayStack(10);
        ArrayStack operStack = new ArrayStack(10);

        int index = 0;
        int num1 = 0;
        int num2 = 0;
        int oper = 0;
        int res = 0;
        char ch = ' ';
        int start = 0;
        while (true) {
            ch = expression.substring(index, index + 1).charAt(0);
            if (operStack.isOper(ch)) {   //如果是运算符
                numStack.push(Integer.parseInt(expression.substring(start, index)));
                start = index + 1;
                if (!operStack.isEmpty()) {   //判断是否为空
                    if (operStack.priority(ch) <= operStack.priority(operStack.getTop())) { //判断运算符优先级
                        num1 = numStack.pop();
                        num2 = numStack.pop();
                        oper = operStack.pop();
                        res = numStack.cal(num1, num2, oper);
                        numStack.push(res);
                        operStack.push(ch);
                    } else {
                        operStack.push(ch);
                    }
                } else {//入栈
                    operStack.push(ch);
                }
            }
            index++;
            if (index >= expression.length()) {
                numStack.push(Integer.parseInt(expression.substring(start, index)));
                break;
            }
        }
        while (true) {
            if (operStack.isEmpty()) {
                break;
            } else {
                num1 = numStack.pop();
                num2 = numStack.pop();
                oper = operStack.pop();
                res = numStack.cal(num1, num2, oper);
                numStack.push(res);
            }
        }
        System.out.printf("表达式 %s = %d", expression, numStack.pop());
    }
}


class ArrayStack {
    private int maxSize;
    private int[] array;
    private int top = -1;

    public ArrayStack(int maxSize) {
        this.maxSize = maxSize;
        array = new int[this.maxSize];
    }

    public boolean isFull() {
        return top == maxSize - 1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public void push(int num) {
        if (isFull()) {
            System.out.println("栈满");
            return;
        }
        top++;
        array[top] = num;
    }

    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("栈空");
        }
        int value = array[top];
        top--;
        return value;
    }

    public void list() {
        if (isEmpty()) {
            System.out.println("栈空");
            return;
        }
        for (int i = top; i >= 0; i--) {
            System.out.printf("Stack[%d] = %d\n", i, array[i]);
        }
    }

    public int priority(int oper) {
        if (oper == '*' || oper == '/') {
            return 1;
        } else if (oper == '+' || oper == '-') {
            return 0;
        } else {
            return -1;
        }
    }

    public boolean isOper(int oper) {
        return oper == '+' || oper == '-' || oper == '*' || oper == '/';
    }

    public int cal(int num1, int num2, int oper) {
        int res = 0;
        switch (oper) {
            case '+':
                res = num1 + num2;
                break;
            case '-':
                res = num2 - num1;
                break;
            case '*':
                res = num1 * num2;
                break;
            case '/':
                res = num2 / num1;
                break;
            default:
                break;
        }
        return res;
    }

    public int getTop() {
        return array[top];
    }
}
