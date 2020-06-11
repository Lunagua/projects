package com.kkb.circleSimpleLinkedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class PolandNotation {
    public static void main(String[] args) {
        //(3+4)*5 + (6-8)*3 = 35 - 6 = 29
        String polandExpression = "3 4 + 5 * 6 8 - 3 * +";
        List<String> list = polandList(polandExpression);
        int num = cal(list);
        System.out.println(num);
//        System.out.println("+-*/".contains("/"));
        List<String> list1 = houzuiExpression("( 3+ 4  ) * 5 + ( 6 - 8 ) * 3");
        System.out.println(list1);
        System.out.println(cal(list1));
    }

    static List<String> polandList(String expression) {
        List<String> list = new ArrayList<>();
        String[] items = expression.split("\\s+");
        for (String item : items) {
            list.add(item);
        }
        return list;
    }

    static int cal(List<String> list) {
        Stack<String> stack = new Stack<>();
        for (String item : list) {
            if (item.matches("\\d+")) {
                stack.push(item);
            } else {
                int num2 = Integer.parseInt(stack.pop());
                int num1 = Integer.parseInt(stack.pop());
                int res = 0;
                if (item.equals("+")) {
                    res = num1 + num2;
                } else if (item.equals("-")) {
                    res = num1 - num2;
                } else if (item.equals("*")) {
                    res = num1 * num2;
                } else if (item.equals("/")) {
                    res = num1 / num2;
                } else {
                    throw new RuntimeException("运算符错误");
                }
                stack.push(res + "");
            }
        }
        return Integer.parseInt(stack.pop());
    }

    /**
     * 1,如果是数字,直接入栈S2
     * 2,如果是左括号,直接入栈S1
     * 3,如果是运算符,比较与栈顶元素的优先级:
     * -3.1如果S1为空,或者栈顶元素为左括号,直接入栈S1
     * -3.2否则,如果优先级比栈顶元素的优先级高,直接入栈S1
     * -3.3否则,弹出S1栈顶元素,入栈S2,继续转回3.1进一步判断
     * 4,遇到括号:
     * -4.1遇到左括号直接入栈S1
     * -4.2右括号,则依次弹出S1栈顶的运算符,压入S2,直到遇到左括号为止,丢弃一对括号
     */
    static List<String> houzuiExpression(String expression) {
        List<String> items = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.substring(i, i + 1).matches("\\s")) {
                start = i + 1;
                continue;
            }
            if (!expression.substring(i, i + 1).matches("\\d")) {
                items.add(expression.substring(i, i + 1));
                start = i + 1;
            } else {
                items.add(expression.substring(start, i + 1));
            }
        }
        List<String> list = new ArrayList<>();
        Stack<String> stack1 = new Stack<>();
        Stack<String> stack2 = new Stack<>();
        for (String item : items) {
            if (item.matches("\\d+")) {
                stack2.push(item);
            } else if (item.equals("(")) {
                stack1.push(item);
            } else if ("+-*/".contains(item)) {
                while (true) {
                    if (stack1.empty() || stack1.peek().equals("(")) {
                        stack1.push(item);
                        break;
                    } else if ("*/".contains(item)) {
                        if ("+-".contains(stack1.peek())) {
                            stack1.push(item);
                            break;
                        } else {
                            stack2.push(stack1.pop());
                        }
                    } else if ("+-".contains(item)) {
                        stack2.push(stack1.pop());
                    }
                }
            } else if (item.equals(")")) {
                String str = "";
                while (true) {
                    if (!(str = stack1.pop()).equals("(")) {
                        stack2.push(str);
                    } else {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i <= stack1.size(); i++) {
            stack2.push(stack1.pop());
        }

        for (int i = 0; i < stack2.size(); i += 0) {
            System.out.println(stack2.peek());
            list.add(stack2.pop());
        }
        System.out.println(list);
        Collections.reverse(list);
        return list;
    }
}
