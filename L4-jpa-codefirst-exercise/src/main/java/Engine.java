
import entities.hospital.Medicament;
import entities.hospital.Patient;
import entities.hospital.PrescribedMedicaments;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {


        /**
         * To test validations in task 4 - Hospital, remove the comments and run the program with active annotations - @Entity
         * only for classes in entities.hospital package:
         */
//        Patient patient = new Patient("Gosho", "Petkov", "Sofia", "123@abv.bg", LocalDate.now(), new byte[]{100}, true);
//        try {
//            entityManager.getTransaction().begin();
//            entityManager.persist(patient);
//            entityManager.getTransaction().commit();
//            entityManager.getTransaction().begin();
//            Patient found = entityManager.find(Patient.class, 1L);
//            entityManager.getTransaction().commit();
//            found.setFirstName(null);  // Here will throw IllegalArgumentException because of the validations that I made
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//        }


        /**
         * Some checks of relations task 4
         */
//        Medicament medicament1 = new Medicament("Aspirin");
//        Medicament medicament2 = new Medicament("Analgin");
//        Medicament medicament3 = new Medicament("Vitamin C");
//        entityManager.getTransaction().begin();
//        entityManager.persist(medicament1);
//        entityManager.persist(medicament2);
//        entityManager.persist(medicament3);
//        entityManager.getTransaction().commit();
//
//        PrescribedMedicaments myMedicaments = new PrescribedMedicaments();
//        entityManager.getTransaction().begin();
//        entityManager.persist(myMedicaments);
//        entityManager.getTransaction().commit();
//        entityManager.getTransaction().begin();
//        PrescribedMedicaments foundMedicaments = entityManager.find(PrescribedMedicaments.class, 1L);
//        foundMedicaments.getMedicaments().add(medicament1);
//        foundMedicaments.getMedicaments().add(medicament2);
//        foundMedicaments.getMedicaments().add(medicament3);
//        foundMedicaments.getMedicaments().forEach(System.out::println);
//        entityManager.getTransaction().commit();


    }
}
