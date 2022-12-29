package course.springdata.mapping.dao;

import course.springdata.mapping.entity.Employee;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

//    @Query("SELECT e FROM Employee AS e WHERE e.manager.id = e.id")
    @Query("SELECT e FROM Employee AS e WHERE e.subordinates IS NOT EMPTY")
    List<Employee> getManagers();

    List<Employee> findAllByBirthdayBeforeOrderBySalaryDesc(LocalDate date);

}
