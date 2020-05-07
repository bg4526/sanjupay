package com.shanjupay.merchant;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Lambad表达式的使用
 * 1.举例： (o1, o2) -> Integer.compare(o1, o2);
 *
 * 2.格式：
 * ->：Lambad操作符 或 箭头操作符
 * ->左边：Lambda形参列表（其实就是接口中的抽象方法的形参列表）
 * ->右边：Lambda体（其实就是重写的抽象方法的方法体）
 *
 * 3.Lambda表达式的使用: (分为6种情况介绍)
 *  总结：
 *  ->左边：Lambda形参列表的参数类型可以省略（类型推断），如果Lambda形参只有一个参数，其一对（）也可以省略
 *  ->右边：Lambda体应该使用一堆{}包裹，如果lambda只有一条执行语句（可能是return语句），可以省略一对{}和return关键字
 *
 * 4.Lambda表达式的本质: (作为函数式接口 的实例，具体实现)
 *
 * 5.如果一个接口中，只声明了一个抽象方法，则此接口称为函数式接口
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LambdaTest {
    //语法格式一：无参，无返回值
    @Test
    public void test01() {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("张三");
            }
        };
        r1.run();
        System.out.println("***************");
        Runnable r2 = () -> System.out.println("李四");
        r2.run();
    }

    //语法格式二：需要一个参数，无返回值
    @Test
    public void test02() {
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("张三");
        System.out.println("**********");
        Consumer<String> consumer1 = (String s) -> System.out.println(s);
        consumer1.accept("李四");
    }

    //格式三：数据类型可以省略，因为可以由编译器推断得出，但是没有返回值
    @Test
    public void test03() {
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("张三");
        System.out.println("**********");
        Consumer<String> consumer1 = (s) -> System.out.println(s);
        consumer1.accept("李四");
    }

    //格式四：Lambda若只需要一个参数时，参数的小括号也可以省
    @Test
    public void test04() {
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("张三");
        System.out.println("**********");
        Consumer<String> consumer1 = s -> System.out.println(s);
        consumer1.accept("李四");
    }

    //格式五：Lambda需要两个或以上的参数，多余的执行语句，并且可以有返回值
    @Test
    public void test05() {
       Comparator<Integer> comparator = new Comparator<Integer>() {
           @Override
           public int compare(Integer o1, Integer o2) {
               System.out.println(o1);
               System.out.println(o2);
               return o1.compareTo(o2);
           }
       };
        System.out.println(comparator.compare(12, 23));
        System.out.println("**********");
        Comparator<Integer> comparator1 = (o1, o2) -> {
            return o1.compareTo(o2);
        };
        System.out.println(comparator1.compare(12, 10));
    }

    //格式六：Lambda体只有一条语句时，return与大括号若有，都可以省略
    @Test
    public void test06() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                System.out.println(o1);
                System.out.println(o2);
                return o1.compareTo(o2);
            }
        };
        System.out.println(comparator.compare(12, 23));
        System.out.println("**********");
        Comparator<Integer> comparator1 = (o1, o2) -> o1.compareTo(o2);
        System.out.println(comparator1.compare(12, 10));
    }


}

