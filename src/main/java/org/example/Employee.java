package main.java.org.example;

public class Employee {

    private String fullName;
    private Integer age;
    private String department;
    private Double salary;

    public Employee(String fullName,Integer age,String department,Double salary )
    {
        this.fullName = fullName;
        this.age = age;
        this.department = department;
        this.salary = salary;

    }


    public String getFullName()
    {
        return fullName;
    }
    public String getDepartment()
    {
        return department;
    }
    public Integer getAge()
    {
        return age;
    }
    public Double getSalary()
    {
        return salary;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
    public void setDepartment(String department)
    {
        this.department = department;
    }
    public void setAge(Integer age)
    {
        this.age = age;
    }
    public void setSalary(Double salary)
    {
        this.salary = salary;
    }

}
