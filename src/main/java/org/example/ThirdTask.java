package main.java.org.example;

import java.util.ArrayList;

public class ThirdTask {

    public static void main()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Иван Петров", 34, "Отдел продаж", 1850.0));
        employees.add(new Employee("Мария Ковалёва", 28, "Маркетинг", 2100.0));
        employees.add(new Employee("Андрюс Янулайтис", 41, "IT", 3200.0));
        employees.add(new Employee("Елена Смирнова", 30, "Бухгалтерия", 1950.0));
        employees.add(new Employee("Томас Каваляускас", 45, "Логистика", 2400.0));
        employees.add(new Employee("Ольга Руденко", 26, "HR", 1700.0));

        employees
                .stream()
                .filter(employee -> employee.getAge() > 30)
                .forEach(employee -> System.out.println(employee.getFullName() + " " + employee.getAge()));

    }



}
