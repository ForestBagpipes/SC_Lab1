package P1;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MagicSquare {
    //设定矩阵阶数最大值N
    final static int N = 400;

    //主函数
    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 5; i++) {
            if (isLegalMagicSquare(i + ".txt")) {
                System.out.println(i + ".txt 中存的数据是幻方");
            } else {
                System.out.println("因此" + i + ".txt 中存的数据不是幻方");
            }
            System.out.println("");
        }
        int n;
        Scanner sr = new Scanner(System.in);
        n = sr.nextInt();
        boolean flag = generateMagicSquare(n);
        if(flag)
        {
            System.out.println("6.txt幻方写入成功");
        }
        else{
            System.out.println("6.txt幻方写入失败");
        }
    }

    //判断是否是幻方函数
    static boolean isLegalMagicSquare(String fileName) throws FileNotFoundException {
        File f = new File("src\\P1\\txt\\" + fileName);
        BufferedReader br = new BufferedReader(new FileReader(f));

        String[][] line = new String[N][N]; //用来存储读取的每一行

        int len = 0; //记录读取到的幻方行数

        int[] wid = new int[N]; //记录读取到的幻方每行的列数

        String str = null; //用来临时存储读取到的行

        String[][] square = new String[N][N];//创建二维字符数组装矩阵
        int[][] int_square = new int[N][N];//创建二维整型数组装矩阵

        try {
            while ((str = br.readLine()) != null) {
                line[len] = str.split("\t");
                wid[len] = line[len].length;
                square[len] = line[len];
                len++;
            }
        } catch (IOException e) {
            System.out.println("读取阻塞");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.out.println("文件关闭失败");
            }

        }

        //判断行列数是否满足方型矩阵定义
        int tmp = wid[0];
        for (int i = 1; i < len; i++) {
            if (tmp != wid[i]) {
                System.out.println("输入的数据每行的个数不相等，不是矩阵");
                return false;
            }
        }
        if (tmp != len) {
            System.out.println("输入的矩阵行列数不相等");
            return false;
        }

        //判断输入的每个数是否是正整数
        float z = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (!isNumber(square[i][j])) {
                    System.out.println("数字之间并非使用\\t 分割");
                    return false;
                }
                z = Float.parseFloat(square[i][j]);
                if (z <= 0.0) {
                    System.out.println("输入的数据含非正整数");
                    return false;
                } else if ((float) ((int) z) != z) {
                    System.out.println("输入的数据有小数");
                    return false;
                }
                int_square[i][j] = Integer.valueOf(square[i][j]);
            }
        }

        int add = 0; //保存第一行数字的和
        for (int i = 0; i < len; i++) {
            add += int_square[0][i];
        }

        //判断每行数之和是否相等
        tmp = 0; //保存各行的和
        for (int i = 1; i < len; i++) {
            for (int j = 0; j < len; j++) {
                tmp += int_square[i][j];
            }
            if (add != tmp) {
                System.out.println("存在某行的数字之和与其他不等");
                return false;
            }
            tmp = 0;
        }

        //判断每列数之和是否相等
        tmp = 0; //保存各列的和
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                tmp += int_square[j][i];
            }
            if (add != tmp) {
                System.out.println("存在某列的数字之和与其他不等");
                return false;
            }
            tmp = 0;
        }

        //判断主对角线之和是否相等
        tmp = 0; //保存主对角线的和
        for (int i = 0; i < len; i++) {
            tmp += int_square[i][i];
        }
        if (add != tmp) {
            System.out.println("主对角线的数字之和与其他不等");
            return false;
        }

        //判断次对角线之和是否相等
        tmp = 0; //保存次对角线的和
        for (int i = 0; i < len; i++) {
            tmp += int_square[i][len - i - 1];
        }
        if (add != tmp) {
            System.out.println("次对角线的数字之和与其他不等");
            return false;
        }
        return true;
    }

    //正则表达式判断字符串是否为数字
    public static boolean isNumber(String string) {
        if (string == null)
            return false;
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
        return pattern.matcher(string).matches();
    }

    public static boolean generateMagicSquare(int n) throws IOException {
        if(n <= 0)
        {
            System.out.println("输入的数字不是正整数，不合法");
            return false;
        }
        else if(n%2 == 0)
        {
            System.out.println("输入的数字不是奇数，不合法");
            return false;
        }
        int magic[][] = new int[n][n];  //初始化二维数组保存矩阵值
        int row = 0, col = n / 2, i, j, square = n * n;//从第一行的中央开始填数
        //连摆法构造幻方，循环填入n*n个数，填完后幻方构造完成
        for (i = 1; i <= square; i++) {
            magic[row][col] = i;
            if (i % n == 0) //数整除n时，下移一行
                row++;
            else {
                if (row == 0)//数填到了最上面第一行时，返回到最下面一行开始填数
                    row = n - 1;
                else //否则，上移一行
                    row--;
                if (col == (n - 1)) //数填到了最右边一列时，返回到最左边开始填数
                    col = 0;
                else//否则，右移一列
                    col++;
            }
        }
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++)
                System.out.print(magic[i][j] + "\t");
            System.out.println();
        }
        File f = new File("src\\P1\\txt\\6.txt"); //创建文件对象
        BufferedWriter bw = new BufferedWriter(new FileWriter(f)); //创建字符缓冲输出流
        //通过循环将矩阵写入文件
        for (i = 0; i < n; i++) {
            for ( j = 0; j < n; j++) {
                bw.write(magic[i][j] + "\t");
                bw.flush(); //清空缓冲区
            }
            bw.newLine(); //换行
        }
        bw.close();//关闭字符缓冲输出流
        return true;
    }
}
