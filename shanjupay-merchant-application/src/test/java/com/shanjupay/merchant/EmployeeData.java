package com.shanjupay.merchant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname EmployeeData
 * @Description TODO
 * @Date 2020/4/19 15:17
 * @Created by 小二哥
 */
public class EmployeeData {
    public static List<Employee> getEmployees() {
        ArrayList<Employee> list = new ArrayList<>();
        list.add(new Employee(1000, "张三", 18, 60000));
        list.add(new Employee(1001, "李四", 19, 70000));
        list.add(new Employee(1002, "王五", 20, 30000));
        list.add(new Employee(1003, "赵六", 21, 80000));
        list.add(new Employee(1004, "陈七", 22, 20000));
        return list;
    }
}
    