package course.springdata.hibernateintro.entity;

import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class HibernateIntroMain {
    public static void main(String[] args) {

        // Create Hibernate config
        Configuration cfg = new Configuration(); // org.hibernate
        cfg.configure(); // By default, it takes hibernate.cfg.xml

        // Create SessionFactory
        SessionFactory sf = cfg.buildSessionFactory();

        // Create Hibernate session
        Session session = sf.openSession();

        // Persist an entity
        Student student = new Student("Hristo Georgiev");
//        Student student = new Student("Dimitar Pavlov");
        session.beginTransaction();
        session.save(student);
        session.getTransaction().commit();

        // Read entity by id

        // Variant 1
        session.beginTransaction();
        Student result = session.get(Student.class, 1L);
//        Student result = session.get(Student.class, 1L, LockMode.READ); // LockMode.READ - not necessary. Ones read this parameter can be get in other sessions
        session.getTransaction().commit();
        System.out.printf("Student with ID:%d -> %s", result.getId(), result.getName());

//        // Variant 2 - Extended, just to see
//        session.setHibernateFlushMode(FlushMode.MANUAL); // Not necessary - turn of flush on every session which make it working fast
//        long queryId = 100L;
////        Student result = session.byId(Student.class).load(queryId); - Without optional
//        Optional<Student> result = session.byId(Student.class).loadOptional(queryId);
////        System.out.printf("Student %s", result.orElseGet(() -> null));
//        if (result.isPresent()) {
//            System.out.printf("Student: %s%n", result.get());
//        } else {
//            System.out.printf("Student with ID:%d does not exist%n", queryId);
//        }

        // List all students using HQL

        // Variant 1
//        session.beginTransaction();
//        List<Student> studentList = session.createQuery("FROM Student ", Student.class).list();
//        for (Student std : studentList) {
//            System.out.println(std.getId());
//        }
//        session.getTransaction().commit();


        // Variant 2
        session.beginTransaction();
        session.createQuery("FROM Student ", Student.class)
                .setFirstResult(5) // From 5th record
                .setMaxResults(10) // Get 10 records if exists
                .stream().forEach(student1 -> System.out.printf("%d %s%n", student1.getId(), student1.getName()));

        System.out.println("-----------------------------------------------------------------------");

//        session.createQuery("FROM Student WHERE name = :name", Student.class) // :name - named position parameter, can set it with setParameter
        session.createQuery("FROM Student WHERE name = ?1", Student.class) //  - numeric position parameter, can set it with setParameter
                .setParameter(1,"Dimitar Pavlov")
                .stream().forEach(student1 -> System.out.printf("%d %s%n", student1.getId(), student1.getName()));

        session.getTransaction().commit();

        // Type-safe criteria queries
        System.out.println("----------------------------------------------");
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Student> query = cb.createQuery(Student.class);
        Root<Student> Student_ = query.from(Student.class); // No query by String, here you can't make mistake like making typo or ...
        query.select(Student_).where(cb.like(Student_.get("name"), "D%")); // Where name start with D
        session.createQuery(query).getResultStream()
                .forEach(System.out::println);

        // Close session
        session.close();

    }
}
