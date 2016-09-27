package com.mark.interview.payroll.model;

import java.util.Objects;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>POJO the ties together {@link Employee} and {@link Salary} classes to represent an Employee's salary
 */
public final class EmployeeSalary {

    private final Employee employee;
    private final Salary salary;

    public EmployeeSalary(Employee employee, Salary salary) {
        if ( employee == null) { throw new IllegalArgumentException("Provided employee cannot be null"); }
        if ( salary == null) { throw new IllegalArgumentException("Provided salary cannot be null"); }
        this.employee = employee;
        this.salary = salary;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Salary getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeSalary that = (EmployeeSalary) o;
        return Objects.equals(employee, that.employee) &&
                Objects.equals(salary, that.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, salary);
    }

    @Override
    public String toString() {
        return "EmployeeSalary{" +
                "employee=" + employee +
                ", salary=" + salary +
                '}';
    }
}
