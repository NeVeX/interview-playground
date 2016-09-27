package com.mark.interview.payroll.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>Simple POJO to represent an Employee
 */
public final class Employee {

    private final String firstName;
    private final String lastName;

    public Employee(String firstName, String lastName) {
        if (StringUtils.isBlank(firstName)) { throw new IllegalArgumentException("Provided firstName is blank"); }
        if (StringUtils.isBlank(lastName)) { throw new IllegalArgumentException("Provided lastName is blank"); }
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * @return - Simple concatenation of first name and last name (e.g. "Jane Fonda")
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(firstName, employee.firstName) &&
                Objects.equals(lastName, employee.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
