import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App {
    /**
     *  Ids for all entities stay in public abstract BaseEntity class which is annotated as @MappedSuperclass
     *  Please remove comment from annotation @Entity for all POJO classes which are part for the Task you test(for all classes in the corresponding folder).
     *  After test comment it back and continue with next task
     */

    /**
     * Please change in persistence.xml file names of the database and persistence-unit for every task
     * Names are in the following String constants:
     */

    private static final String GRINGOTTS_PU = "gringotts";
    private static final String SALES_PU = "sales";
    private static final String UNIVERSITY_PU = "university";
    private static final String HOSPITAL_PU = "hospital";
    private static final String PAYMENT_SYSTEM_PU = "payment_system";

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory(GRINGOTTS_PU); // Change the constant to correspond to its database name for every task


        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Engine engine = new Engine(entityManager);

        engine.run();

    }
}
