package com.kkb.sparseArray;

import java.io.*;

public class SparseArrayTest {
    public static void main(String[] args) throws IOException {
        //创建原始二维数组
        int chressArray[][] = new int[11][11];
        chressArray[1][2] = 1;
        chressArray[2][3] = 2;
        chressArray[3][4] = 1;
        chressArray[4][5] = 2;
        chressArray[5][2] = 1;
        chressArray[5][3] = 2;
        //遍历原始数组,获取有效数据数量
        int num = 0;
        for (int[] rows : chressArray) {
            for (int data : rows) {
                if (data != 0) {
                    num++;
                }
            }
        }

        //创建稀疏数组,并将原始数组的行列和有效数据计入第一行
        int sparseArray[][] = new int[num + 1][3];
        sparseArray[0][1] = 11;
        sparseArray[0][0] = 11;
        sparseArray[0][2] = num;

        //遍历原始数组,将有效数据信息写入稀疏数组
        int count = 0;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (chressArray[i][j] != 0) {
                    count++;
                    sparseArray[count][0] = i;
                    sparseArray[count][1] = j;
                    sparseArray[count][2] = chressArray[i][j];
                }
            }
        }
        //控制台输出稀疏数组
        System.out.println("=============完成后的稀疏数组=============");
        for (int[] rows : sparseArray) {
            for (int data : rows) {
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }

        //将稀疏数组写入data文件
        FileOutputStream fos = new FileOutputStream("data");
        //将稀疏数组转化为StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int[] rows : sparseArray) {
            for (int i = 0; i < 3; i++) {
                sb.append(rows[i]);
                if (i != 2) {
                    sb.append("\t");
                }
            }
            sb.append("\n");
        }
        byte[] bytes = sb.toString().getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        int res = -1;
        while ((res = bais.read()) != -1) {
            fos.write(res);
        }
        fos.close();
        bais.close();

        //读取文件中的数据,还原成稀疏数组
        FileInputStream fis = new FileInputStream("data");
        byte[] bytes1 = new byte[1024];
        int len = -1;
        String result = "";
        while ((len = fis.read(bytes1)) != -1) {
            result = new String(bytes1, 0, len);
        }
        System.out.println("还原后的字符串====");
        System.out.println(result);
        //将从文件中读取的字符串转换为稀疏数组
        String[] rows = result.split("\n");
        int sparseArray1[][] = new int[rows.length][3];
        for (int i = 0; i < rows.length; i++) {
            sparseArray1[i][0] = Integer.parseInt(rows[i].split("\t")[0]);
            sparseArray1[i][1] = Integer.parseInt(rows[i].split("\t")[1]);
            sparseArray1[i][2] = Integer.parseInt(rows[i].split("\t")[2]);
        }

        System.out.println("还原后的稀疏数组=======");
        for(int[] rows1:sparseArray1 ){
            for(int data:rows1){
                System.out.printf("%d\t",data);
            }
            System.out.println();
        }

        //将稀疏数组还原成 原始数组
        int chessArray1[][] = new int[sparseArray1[0][0]][sparseArray1[0][1]];
        for(int i=1;i<sparseArray1.length;i++){
            chessArray1[sparseArray1[i][0]][sparseArray1[i][1]] = sparseArray1[i][2];
        }
        System.out.println("还原后的原始数组========");
        for(int[] oo:chessArray1){
            for(int data:oo){
                System.out.printf("%d\t",data);
            }
            System.out.println();
        }


    }

}
