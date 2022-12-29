
import course.springdata.jpaintro.entity.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaIntroMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("school_jpa");
        EntityManager em = emf.createEntityManager();
        Student student = new Student("Dimitar Pavlov");
//        Student student = new Student("Georgi Pavlov");
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();

        // Start transaction
        em.getTransaction().begin();
        Student found = em.find(Student.class, 1L);
        System.out.printf("Found student: %s", found);
        em.getTransaction().commit();
        // End transaction

        em.getTransaction().begin();
        em.createQuery("SELECT s FROM Student AS s WHERE s.name LIKE :name", Student.class) // Select all students from Student entities(table)
                .setParameter("name", "G%")
                .getResultList().forEach(System.out::println);
        em.getTransaction().commit();

        // Remove
//        em.getTransaction().begin();
//        Student removed = em.find(Student.class, 1L);
//        em.remove(found);
//        System.out.printf("Removed entity: %s%n", removed);
//        em.getTransaction().commit();

        // Merge
        em.getTransaction().begin();
        Student found2 = em.find(Student.class, 3L);
        em.detach(found2); // won't persist when commit
        found2.setName("Atanas Petrov");
        Student managedEntity = em.merge(found2); // Returns it back and can be committed. Managed
        System.out.printf("Same reference: %s%n", managedEntity == found2);
        em.getTransaction().commit();

        em.close();
    }
}
