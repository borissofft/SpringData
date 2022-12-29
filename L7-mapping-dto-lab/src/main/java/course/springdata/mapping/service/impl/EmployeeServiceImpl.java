package course.springdata.mapping.service.impl;

import course.springdata.mapping.dao.EmployeeRepository;
import course.springdata.mapping.entity.Employee;
import course.springdata.mapping.exception.NonexistingEntityException;
import course.springdata.mapping.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepo;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }


    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeRepo.findAll();
    }

    @Override
    public List<Employee> getAllManagers() {
        return this.employeeRepo.getManagers();
    }

    @Override
    public List<Employee> getAllEmployeesBornBefore(LocalDate toDate) {
        return this.employeeRepo.findAllByBirthdayBeforeOrderBySalaryDesc(toDate);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return this.employeeRepo.findById(id).orElseThrow(
                () -> new NonexistingEntityException(String.format("Employee with ID=%s does not exist", id))
        );
    }


    @Override
    @Transactional
    public Employee addEmployee(Employee employee) {
        employee.setId(null);
        if (employee.getManager() != null) {
            employee.getManager().getSubordinates().add(employee);
        }
        return this.employeeRepo.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee) {
        Employee existing = getEmployeeById(employee.getId());
        Employee updated = this.employeeRepo.save(employee);

        if (existing.getManager() != null && !existing.getManager().equals(employee.getManager())) { // There was an old manager аnd we want to replace him
            existing.getManager().getSubordinates().remove(existing); // remove from existing if there is a manager
        }

        if (updated.getManager() != null && !updated.getManager().equals(existing.getManager())) {
            updated.getManager().getSubordinates().add(updated); // add if there is new manager
        }

        return updated;
    }

    @Override
    @Transactional
    public Employee deleteEmployee(Long id) {
        Employee removed = getEmployeeById(id);
        if (removed.getManager() != null) {
            removed.getManager().getSubordinates().remove(removed);
        }
        this.employeeRepo.delete(removed);
        return removed;
    }

    @Override
    public Long getEmployeesCount() {
        return this.employeeRepo.count();
    }

}
