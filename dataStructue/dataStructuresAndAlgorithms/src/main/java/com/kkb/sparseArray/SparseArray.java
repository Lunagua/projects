package com.kkb.sparseArray;

import java.io.*;

public class SparseArray {
    public static void main(String[] args) throws IOException {
        //创建原始的二维数组 11 * 11
        // 0: 表示没有棋子, 1表示黑子,2表示篮子
        int sum = 0;
        int chessArr1[][] = new int[11][11];
        chessArr1[1][2] = 1;
        chessArr1[2][3] = 2;
        chessArr1[3][4] = 2;
        chessArr1[2][5] = 2;
        chessArr1[2][6] = 2;
        //输出原始的二维数组
//        System.out.println("原始的二维数组");
        for (int[] row : chessArr1) {
            for (int data : row) {
//                System.out.printf("%d\t", data);
                if (data != 0) {
                    sum++;
                }
            }
//            System.out.println();
        }
        System.out.println(sum);    //输出有效记录数量

        //创建稀疏数组,并写入原始数组行\列数,和有效记录数量
        int sparseArr[][] = new int[sum + 1][3];
        sparseArr[0][0] = 11;
        sparseArr[0][1] = 11;
        sparseArr[0][2] = sum;

        //将有效记录写入稀疏数组中
        int count = 1;  //稀疏数组中的记录行号
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (chessArr1[i][j] != 0) {
                    sparseArr[count][0] = i;
                    sparseArr[count][1] = j;
                    sparseArr[count][2] = chessArr1[i][j];
                    count++;
                }
            }
        }


        //创建StringBuilder,将稀疏数组转化为StringBuilder,数字以制表符分隔,行以换行符分隔
        StringBuilder allBuilder = new StringBuilder();
        for (int[] row : sparseArr) {
            for (int data : row) {
                allBuilder.append(data + "\t");
            }
            allBuilder.append("\n");
        }

        //通过FileOutputStream流,将allBuilder写入map.data文件
        FileOutputStream fileOutputStream = new FileOutputStream("map.data");
        byte[] bytes = allBuilder.toString().getBytes();    //将StringBuilder转化为byte数组
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);    //通过ByteArrayInputStream将byte数组转化为IO流
        int len = -1;
        while ((len = byteArrayInputStream.read()) != -1) {     //通过fileOutputStream将IO流数据写入文件
            fileOutputStream.write(len);
        }

        //通过FileInputStream 读取map.data文件内容,并转化为输入流
        FileInputStream fileInputStream = new FileInputStream("map.data");
        byte[] bytes1 = new byte[1024];
        len = 0;
        //将输入流读取结果写入字符串对象
        String result = "";
        while ((len = fileInputStream.read(bytes)) != -1) {
            result = new String(bytes, 0, len);
        }

        //通过换行符切割result字符串,将其转化为一维数组
        String rows[] = result.split("\n");
        //创建稀疏数组容器,行数由rows长度确定,列数为3
        int sparseArr2[][] = new int[rows.length][3];
        //将稀疏数组循环遍历,并给相应的位置赋值
        for (int i = 0; i < rows.length; i++) {
            for (int k = 0; k < 3; k++) {
                //通过制表符切割每一行的数据,并强制转化为int类型
                sparseArr2[i][k] = Integer.parseInt(rows[i].split("\t")[k]);
            }
        }

        //将稀疏数组还原成原始数组
        //创建原始数组容器
        int chressArr2[][] = new int[sparseArr2[0][0]][sparseArr2[0][1]];
        //给原始数组的有效数字部分进行赋值
        for (int i = 1; i <= sparseArr2[0][2]; i++) {
            chressArr2[sparseArr2[i][0]][sparseArr2[i][1]] = sparseArr2[i][2];
        }
        for (int[] row : chressArr2) {
            for (int date : row) {
                System.out.printf("%d\t", date);
            }
            System.out.println();
        }
    }
}
