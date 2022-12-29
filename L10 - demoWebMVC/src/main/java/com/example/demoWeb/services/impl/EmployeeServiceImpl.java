package com.example.demoWeb.services.impl;

import com.example.demoWeb.dto.EmployeeDto;
import com.example.demoWeb.entity.Employee;
import com.example.demoWeb.repositories.EmployeeRepository;
import com.example.demoWeb.services.EmployeeService;
import com.example.demoWeb.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ProjectService projectService, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean exists() {
        return this.employeeRepository.existsAllBy();
    }

    @Override
    public String getXmlForImport() throws IOException {
        return new String(this.getClass().getClassLoader().getResourceAsStream(FILE_PATH).readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public Long create(EmployeeDto request) {
        var existing = this.employeeRepository.findFirstByFirstNameAndLastNameAndAge(
                request.getFirstName(),
                request.getLastName(),
                request.getAge()
        );

        if (existing != null) {
            return existing.getId();
        }

        var employee = this.modelMapper.map(request, Employee.class);
        var projectId = this.projectService.create(request.getProject());
        var project = this.projectService.find(projectId);
        employee.setProject(project);
        this.employeeRepository.save(employee);

        return employee.getId();
    }

}
