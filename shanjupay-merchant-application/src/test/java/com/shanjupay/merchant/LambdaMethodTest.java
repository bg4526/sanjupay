package com.shanjupay.merchant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方法引用的使用
 * 1.使用情景：当传递给lambda体的操作，已经有实现的方法了，可以使用方法引用
 * 2.方法引用，恩知就是lambda表达式，而lambda表达式作为函数式接口的实例，所以方法引用，也是函数式接口的实例
 * 3.使用格式： 类(或对象)::方法名
 * 4.具体分为如下三种情况
 * 情况1 对象::非静态方法
 * 情况2 类::静态方法
 * 情况3 类::非静态方法
 * 5.方法引用使用的要求：要求接口中的抽象方法的形参列表和返回值的类型与方法引用的方法的形参列表和返回值类型相同（针对情况1，情况2）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LambdaMethodTest {
    /**
     * 情况一：对象::实例方法
     * Consumer中的void accept(T t)
     * PrintStream中的void println(T t)
     */
    @Test
    public void test01() {
        Consumer<String> con = str -> System.out.println(str);
        con.accept("北京");
        System.out.println("*************");
        PrintStream ps = System.out;
        Consumer<String> con1 = ps::println;
        con1.accept("上海");
    }

    /**
     * 情况一：对象::实例方法
     * Supplier中的 T get()
     * Employee中的 String getName()
     */
    @Test
    public void test02() {
        Employee emp = new Employee(101, "张三", 23, 4000);
        Supplier<String> sup = () -> emp.getName();
        System.out.println(sup.get());
        System.out.println("*************");
        Employee emp1 = new Employee(103, "李四", 22, 2000);
        Supplier<String> sup2 = emp1::getName;
        System.out.println(sup2.get());

    }

    /**
     * 情况二：类::静态方法
     * Comprator中的Int(T t1, T t2)
     * Integer中的int compare(T t1, T t2)
     */
    @Test
    public void test03() {
        Comparator<Integer> com1 = (t1, t2) -> Integer.compare(t1, t2);
        System.out.println(com1.compare(11, 21));
        System.out.println("*************");
        Comparator<Integer> com2 = Integer::compare;
        System.out.println(com2.compare(32, 11));
    }

    /**
     * 情况二：类::静态方法
     * Funtion中的R apply(T t)
     * Math中的Long round(Double d)
     */
    @Test
    public void test04() {
        Function<Double, Long> fun1 = d -> Math.round(d);
        System.out.println(fun1.apply(12.2));
        System.out.println("*************");
        Function<Double, Long> fun2 = Math::round;
        System.out.println(fun2.apply(12.5));
    }

    /**
     * 情况三：类::实例方法
     * Comprator中的Int(T t1, T t2)
     * String中的int t1.compareTo(t2)
     */
    @Test
    public void test05() {
        Comparator<String> comparator = (s1, s2) -> s1.compareTo(s2);
        System.out.println(comparator.compare("abc", "abd"));
        System.out.println("*************");
        Comparator<String> comparator2 = String::compareTo;
        System.out.println(comparator2.compare("abc", "abdm"));
    }

    /**
     * 情况三：类::实例方法
     * Function中的R apply(T t)
     * Employee中的String getName()
     */
    @Test
    public void test06() {
        Employee emp = new Employee(101, "张三", 23, 4000);
        Function<Employee, String> fun = e -> e.getName();
        System.out.println(fun.apply(emp));
        System.out.println("*************");
        Function<Employee, String> fun1 = Employee::getName;
        System.out.println(fun1.apply(emp));
    }
}
    