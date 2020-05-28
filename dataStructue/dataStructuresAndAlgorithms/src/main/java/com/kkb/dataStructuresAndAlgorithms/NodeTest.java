package com.kkb.dataStructuresAndAlgorithms;

public class NodeTest<T> {
    private class Node{
        private T data;
        private Node next;

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Node() {
        }
    }

}
