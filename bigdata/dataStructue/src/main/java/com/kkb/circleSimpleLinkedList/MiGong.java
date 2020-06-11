package com.kkb.circleSimpleLinkedList;

public class MiGong {
    public static void main(String[] args) {
        int[][] map = new int[8][8];
        for (int i = 0; i < 8; i++) {
            map[0][i] = 1;
            map[7][i] = 1;
        }
        for (int i = 0; i < 8; i++) {
            map[i][0] = 1;
            map[i][7] = 1;
        }
        map[3][1] = 1;
        map[3][2] = 1;
        map[5][4] = 1;
        map[6][4] = 1;

        int [][] map1 = map.clone();

        System.out.println("初始化地图");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(map[i][j] + "\t");
            }
            System.out.println();
        }
        setWayDRUL(map, 1, 1);
        System.out.println("找路后的地图");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(map[i][j] + "\t");
            }
            System.out.println();
        }
    }


    public static boolean setWayDRUL(int[][] map, int i, int j) {
        if (map[2][2] == 2) {
            return true;
        } else {
            if (map[i][j] == 0) {
                map[i][j] = 2;
                if (setWayDRUL(map, i + 1, j)) {
                    return true;
                } else if (setWayDRUL(map, i, j + 1)) {
                    return true;
                } else if (setWayDRUL(map, i - 1, j)) {
                    return true;
                } else if (setWayDRUL(map, i, j - 1)) {
                    return true;
                } else {
                    map[i][j] = 3;
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean setWayURDL(int[][] map, int i, int j) {
        if (map[6][6] == 2) {
            return true;
        } else {
            if (map[i][j] == 0) {
                map[i][j] = 2;
                if (setWayURDL(map, i - 1, j)) {
                    return true;
                } else if (setWayURDL(map, i, j + 1)) {
                    return true;
                } else if (setWayURDL(map, i + 1, j)) {
                    return true;
                } else if (setWayURDL(map, i, j - 1)) {
                    return true;
                } else {
                    map[i][j] = 3;
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean setWayDLUR(int[][] map, int i, int j) {
        if (map[6][6] == 2) {
            return true;
        } else {
            if (map[i][j] == 0) {
                map[i][j] = 2;
                if (setWayDLUR(map, i +1, j)) {
                    return true;
                } else if (setWayDLUR(map, i, j -1)) {
                    return true;
                } else if (setWayDLUR(map, i + 1, j)) {
                    return true;
                } else if (setWayDLUR(map, i, j + 1)) {
                    return true;
                } else {
                    map[i][j] = 3;
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean setWayULDR(int[][] map, int i, int j) {
        if (map[6][6] == 2) {
            return true;
        } else {
            if (map[i][j] == 0) {
                map[i][j] = 2;
                if (setWayULDR(map, i - 1, j)) {
                    return true;
                } else if (setWayULDR(map, i, j - 1)) {
                    return true;
                } else if (setWayULDR(map, i + 1, j)) {
                    return true;
                } else if (setWayULDR(map, i, j + 1)) {
                    return true;
                } else {
                    map[i][j] = 3;
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
