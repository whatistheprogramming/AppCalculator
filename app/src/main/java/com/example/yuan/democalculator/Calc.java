package com.example.yuan.democalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 实现计算器算法功能
 */
public class Calc {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
//        String exp = "-3*-(3+6/6-3)*1";
        while(in.hasNext())
        {
            String exp = in.next();
            System.out.println(calcBracket(exp));
        }
    }

    /**
     * 带括弧的四则运算
     * @param exp 带括弧的表达式
     * @return
     */
    public static double calcBracket(String exp){
        //从最里面的括号开始运算,exp中最后出现的(最内层的括号
        int lastIndexOf = exp.lastIndexOf('(');
        //两种结果
        if (lastIndexOf != -1) {
            //有
            //1.计算最内层括号表达式的值,找对应的右括号
            int rightIndexOf = exp.indexOf(')', lastIndexOf);
            double d = calc(exp.substring(lastIndexOf+1,rightIndexOf));
            //2.把运算结果拼接
            exp = exp.substring(0, lastIndexOf)+d+exp.substring(rightIndexOf+1);
            return calcBracket(exp);
        }else {
            //没有
            //没有括号的表达式
            return calc(exp);
        }
    }

    /**
     *
     * @param exp 不带括号的四则表达式
     * @return
     */
    public static double calc(String exp){
        //1.把表示负数的-号换成@号
        exp = fu2At(exp);
        //2.数字的分类
        List<Double> numbers = splitNumExp(exp);
        //3.运算符的分离
        List<Character> ops = splitOpfromExp(exp);
        //4.先乘除
        for (int i = 0; i < ops.size(); i++) {
            //判断,运算符是否是乘除
            char op = ops.get(i);
            //是,取出,运算
            if (op == '×' || op == '÷') {
                //取出来,运算
                ops.remove(i);//后面的数据往前顺序移动
                //运算
                //从数字容器中取出对应运算符的两个数字
                double d1 = numbers.remove(i);
                double d2 = numbers.remove(i);

                if (op == '×') {
                    d1 *= d2;
                }else {
                    d1 /= d2;
                }

                //把运算结果放入数字容器中i的位置
                numbers.add(i, d1);//原来i位置(包括)后面的数据依次往后顺移
                i--;
            }
        }
        //5.后加减
        while (!ops.isEmpty() ) {
            char op = ops.remove(0);
            double d1 = numbers.remove(0);
            double d2 = numbers.remove(0);
            //运算
            if (op == '+') {
                d1 += d2;
            } else {
                d1 -= d2;
            }
            //把运算结果插入到数字容器中0的位置
            numbers.add(0, d1);
        }
        //6.容器中的第一个数据就是结果
        return numbers.get(0);
    }

    /**
     * 从表达式中分离表达式和运算符
     * @param exp
     * @return
     */
    private static List<Character> splitOpfromExp(String exp) {
        List<Character> ops = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(exp, "1234567890.@");
        while (st.hasMoreTokens()) {
            char c = st.nextElement().toString().trim().charAt(0);
            ops.add(c);
        }
        return ops;
    }

    /**
     * 分离出数字
     * @param exp
     * @return
     */
    private static List<Double> splitNumExp(String exp) {
        List<Double> numbers = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(exp, "+-×÷");
        while (st.hasMoreTokens()) {
            String numStr = st.nextElement().toString().trim();
            if (numStr.charAt(0) == '@') {
                numStr = "-" + numStr.substring(1);
            }
            numbers.add(Double.parseDouble(numStr));
        }
        return numbers;
    }

    /**
     * 把-号换成@号
     * @param exp
     * @return
     */
    private static String fu2At(String exp) {
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c == '-') {
                //判断是否是负数
                if (i == 0) {
                    //第一个位置肯定是负数
                    exp = "@"+exp.substring(1);
                }else {
                    //不是第一个位置
                    //判断前一个位置是否是运算符
                    char cprev = exp.charAt(i - 1);
                    if (cprev == '+' || cprev == '-' || cprev == '×' || cprev == '÷') {
                        exp = exp.substring(0, i)+"@"+exp.substring(i+1);
                    }
                }
            }
        }
        return exp;
    }
}