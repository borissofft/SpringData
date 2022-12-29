
import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

        System.out.println("Use clear database for tasks 7");
        System.out.println("Enter number from 2 to 13 to check tasks:");
        int task;
        try {
            task = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (task) {
            case 2 -> changeCasingEx2();
            case 3 -> {
                try {
                    containsEmployeeEx3();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case 4 -> employeesWithSalaryOver50000Ex4();
            case 5 -> employeesFromDepartmentEx5();
            case 6 -> {
                try {
                    addingNewAddressAndUpdatingEmployeeEx6();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case 7 -> addressesWithEmployeeCountEx7();
            case 8 -> {
                try {
                    getEmployeeWithProjectEx8();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case 9 -> findLatestThenProjectsEx9();
            case 10 -> increaseSalariesEx10();
            case 11 -> {
                try {
                    findEmployeesByFirstNameEx11();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case 12 -> employeesMaximumSalariesEx12();
            case 13 -> {
                try {
                    removeTownsEx13();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> System.out.println("Please use only numbers from 2 to 13");
        }


    }


    private void changeCasingEx2() {

        // Variant 1 - just to see most of the entityManager methods
//        List<Town> towns = entityManager
//                .createQuery("SELECT t FROM Town AS t " +
//                        "WHERE length(t.name) <= 5", Town.class)
//                .getResultList();
//        entityManager.getTransaction().begin();
//        towns.forEach(entityManager::detach);
//
//        for (Town town : towns) {
//            town.setName(town.getName().toLowerCase());
//        }
//        towns.forEach(entityManager::merge);
//        entityManager.flush();
//        entityManager.getTransaction().commit();

        // Variant 2 - Remember always to start and end transaction when you try to make changes into the database!
        entityManager.getTransaction().begin();
        entityManager.createQuery("SELECT t FROM Town AS t " +
                        "WHERE length(t.name) <= 5", Town.class)
                .getResultStream()
                .forEach(town -> town.setName(town.getName().toLowerCase()));
        entityManager.getTransaction().commit();

    }

    private void containsEmployeeEx3() throws IOException {
        System.out.println("Enter employee full name:");
        String fullName = reader.readLine();

        List<Employee> employees = entityManager
                .createQuery("SELECT e FROM Employee AS e WHERE CONCAT(e.firstName, ' ', e.lastName) = :name", Employee.class)
                .setParameter("name", fullName).getResultList();
        System.out.println(employees.size() == 0 ? "No" : "Yes");
    }

    private void employeesWithSalaryOver50000Ex4() {

        // Variant 1
//        List<Employee> employees = entityManager
//                .createQuery("SELECT e FROM Employee AS e WHERE e.salary > 50000", Employee.class).getResultList();
//        for (Employee employee : employees) {
//            System.out.println(employee.getFirstName());
//        }

        // Variant 2
        entityManager
                .createQuery("SELECT e FROM Employee AS e WHERE e.salary > 50000", Employee.class)
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void employeesFromDepartmentEx5() {
        entityManager.createQuery("SELECT e FROM Employee AS e " +
                        "WHERE e.department.name = 'Research and Development' " +
                        "ORDER BY e.salary, e.id", Employee.class)
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s from Research and Development - $%.2f%n",
                        e.getFirstName(), e.getLastName(), e.getSalary()));

    }

    private void addingNewAddressAndUpdatingEmployeeEx6() throws IOException {
        Address address = createAddress("Vitoshka 15");
        System.out.println("Enter employee last name:");
        String lastName = reader.readLine();
        try {
            Employee employee = entityManager
                    .createQuery("SELECT e FROM Employee AS e WHERE e.lastName = :name", Employee.class)
                    .setParameter("name", lastName).getSingleResult();
            entityManager.getTransaction().begin();
            employee.setAddress(address);
            entityManager.getTransaction().commit();
        } catch (NoResultException e) {
            System.out.println("No such last name in table employees. " +
                    "Start the program and enter last name which persist in the table.");
        }

    }

    private Address createAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);
        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();
        return address;
    }

    private void addressesWithEmployeeCountEx7() {
        List<Address> addresses = entityManager
//                .createQuery("SELECT a FROM Address AS a GROUP BY a.text " +
                .createQuery("SELECT a FROM Address AS a " +
                        "ORDER BY a.employees.size DESC ", Address.class)
                .setMaxResults(10)
                .getResultList();

        addresses.forEach(address -> System.out.printf("%s, %s - %d employees%n",
                address.getText(),
                address.getTown().getName(),
                address.getEmployees().size()));
    }

    private void getEmployeeWithProjectEx8() throws IOException {
        System.out.println("Enter valid employee id:");
        int id = Integer.parseInt(reader.readLine());

        Employee employee = entityManager.find(Employee.class, id);
        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());
        employee.getProjects()
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(project -> System.out.printf("\t%s%n", project.getName()));
    }

    private void findLatestThenProjectsEx9() {
        List<Project> projects = entityManager
                .createQuery("SELECT p FROM Project AS p " +
                        "ORDER BY p.startDate DESC, p.name", Project.class)
                .setMaxResults(10)
                .getResultList();

        projects.stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.printf("Project name: %s%n" +
                                " \tProject Description: %s%n" +
                                " \tProject Start Date:%s %s:%s%s%n" +
                                " \tProject End Date: %s%n",
                        p.getName(),
                        p.getDescription(),
                        p.getStartDate().toLocalDate(),
                        p.getStartDate().toLocalTime(),
                        p.getStartDate().toLocalTime().getNano(),
                        p.getStartDate().toLocalTime().getNano(),
                        p.getEndDate()
                ));

        System.out.println();
    }

    private void increaseSalariesEx10() {
        entityManager.getTransaction().begin();
        int affectedRows = entityManager
                .createQuery("UPDATE Employee AS e " +
                        "SET e.salary = e.salary * 1.12 " +
                        "WHERE e.department.id IN(1, 2, 4, 11)")
                .executeUpdate();
        entityManager.getTransaction().commit();

//        System.out.printf("Affected rows %d%n", affectedRows); // Not needed. It's for my info

        entityManager.createQuery("SELECT e FROM Employee AS e " +
                        "WHERE e.department.id IN(1, 2, 4, 11)", Employee.class)
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s ($%.2f)%n", e.getFirstName(), e.getLastName(), e.getSalary()));
    }

    private void findEmployeesByFirstNameEx11() throws IOException {
        System.out.println("Enter pattern with which have to start the first name of some employee:");
        String pattern = reader.readLine();
        List<Employee> employees = entityManager
                .createQuery("SELECT e FROM Employee AS e " +
                        "WHERE e.firstName LIKE :name", Employee.class)
                .setParameter("name", pattern + "%")
                .getResultList();

        employees.forEach(e -> System.out.printf("%s %s - %s - ($%.2f)%n",
                e.getFirstName(),
                e.getLastName(),
                e.getJobTitle(),
                e.getSalary()
        ));
    }

    @SuppressWarnings("unchecked")
    private void employeesMaximumSalariesEx12() {

//         Variant 1 - With standard SQL query - returns List<Object[]>
        List<Object[]> rows = entityManager
                .createNativeQuery("SELECT d.name, MAX(e.salary) AS 'max_salary' FROM departments AS d\n" +
                        "JOIN employees e on d.department_id = e.department_id\n" +
                        "GROUP BY d.name\n" +
                        "HAVING `max_salary` NOT BETWEEN 30000 AND 70000")
                .getResultList();

        rows.forEach(row -> System.out.printf("%s %s%n", row[0], row[1]));


        // Variant 2 - Try with JPQL language - the query is wrong
//        List<Department> departments = entityManager
//                .createQuery("SELECT d.name, MAX(e.salary) FROM Department AS d, Employee AS e WHERE d.id = e.id GROUP BY d.id")
//                .getResultList();

        // Variant 3 - From other person homework... Check it
//        entityManager
//                .createQuery("SELECT e.department.name, MAX(e.salary) FROM Employee e GROUP BY e.department.name HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000", Object[].class)
//                .getResultList().forEach(e -> System.out.printf("%s %.2f%n", e[0], e[1]));
//
//        System.out.println();
    }

    private void removeTownsEx13() throws IOException {
        // Variant 1
        System.out.println("Enter town name:");
        String townName = reader.readLine();
        Town town = entityManager.createQuery("SELECT t FROM Town AS t " +
                        "WHERE t.name = :name", Town.class)
                .setParameter("name", townName)
                .getSingleResult();

        int affectedRows = removeAddressesByTownId(town.getId());

        entityManager.getTransaction().begin();
        entityManager.remove(town);
        entityManager.getTransaction().commit();

        System.out.printf("%d address in %s deleted%n", affectedRows, townName);


        // Variant 2
//        System.out.println("Enter town name:");
//        String townName = reader.readLine();
//        List<Address> addresses = entityManager
//                .createQuery("SELECT a FROM Address AS a " +
//                        "WHERE a.town.name = :name", Address.class)
//                .setParameter("name", townName)
//                .getResultList();
//
//        entityManager.getTransaction().begin();
//        Town town = addresses.get(0).getTown();
//        int countDeleted = 0;
//        for (Address address : addresses) {
//            for (Employee employee : address.getEmployees()) {
//                employee.setAddress(null);
//            }
//            countDeleted++;
//            entityManager.flush();
//            entityManager.remove(address);
//        }
//
//        entityManager.remove(town);
//        entityManager.getTransaction().commit();
//
//        System.out.printf("%d address in %s deleted", countDeleted, townName);

    }

    private int removeAddressesByTownId(Integer id) {
        List<Address> addresses = entityManager
                .createQuery("SELECT a FROM Address AS a " +
                        "WHERE a.town.id = :p_id", Address.class)
                .setParameter("p_id", id)
                .getResultList();

        entityManager.getTransaction().begin();
        for (Address address : addresses) {
            for (Employee employee : address.getEmployees()) {
                employee.setAddress(null);
            }
        }
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        addresses.forEach(entityManager::remove);
        entityManager.getTransaction().commit();

        return addresses.size();
    }

}
