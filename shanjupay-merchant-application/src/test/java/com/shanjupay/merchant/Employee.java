package com.shanjupay.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname Employee
 * @Description TODO
 * @Date 2020/4/19 15:15
 * @Created by 小二哥
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private int id;
    private String name;
    private int age;
    private int salary;
}
    