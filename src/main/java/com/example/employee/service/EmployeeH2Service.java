package com.example.employee.service;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import com.example.employee.model.EmployeeRowMapper;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.model.Employee;

@Service
public class EmployeeH2Service implements EmployeeRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Employee> getAllEmployees() {

        List<Employee> listEmployee = db.query("select * from employeelist", new EmployeeRowMapper());
        ArrayList<Employee> arrList = new ArrayList<>(listEmployee);
        return arrList;

    }

    @Override
    public Employee getEmployeeById(int id) {
        try {
            Employee employee = db.queryForObject("select * from employeelist where employeeId = ?",
                    new EmployeeRowMapper(), id);
            return employee;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Employee addEmployee(Employee employee) {

        db.update("insert into employeelist(employeeName, email, department) values(?,?,?)", employee.getEmployeeName(),
                employee.getEmail(), employee.getDepartment());
        Employee employee1 = db.queryForObject(
                "Select * from employeelist where employeeName = ? and email = ? and department = ?",
                new EmployeeRowMapper(), employee.getEmployeeName(), employee.getEmail(), employee.getDepartment());
        return employee1;

    }

    @Override
    public Employee updateEmployee(int id, Employee employee) {
        if (employee.getEmployeeName() != null) {
            db.update("update employeelist set employeeName = ? where employeeId = ?", employee.getEmployeeName(), id);
        }
        if (employee.getEmail() != null) {
            db.update("update employeelist set email = ? where employeeId = ?", employee.getEmail(), id);
        }
        if (employee.getDepartment() != null) {
            db.update("update employeelist set department = ? where employeeId = ?", employee.getDepartment(), id);
        }

        return getEmployeeById(id);
    }

    @Override
    public void deleteEmployee(int id) {
        db.update("delete from employeeList where employeeId = ?", id);
    }
}