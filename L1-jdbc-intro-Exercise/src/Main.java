import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);

        Homework homework = new Homework();

        // Please set your user and password
        homework.setConnection("root", "******");

        System.out.println("Please be sure that you use clear database without inserts for tasks 7");
        System.out.println("Don't forget to create procedure usp_get_older for task 9 after you create the clear database");
        System.out.println("Enter number from 2 to 9 to check tasks:");
        int task = Integer.parseInt(scanner.nextLine());

        switch (task) {
            case 2:
                homework.getVillainsNamesEx2();
                break;
            case 3:
                homework.getMinionNamesEx3();
                break;
            case 4:
                homework.addMinionEx4();
                break;
            case 5:
                homework.changeTownNameCasingEx5();
                break;
            case 6:
                System.out.println("This task is optional");
                break;
            case 7:
                homework.printAllMinionNamesEx7();
                break;
            case 8:
                homework.increaseMinionsAgeEx8();
                break;
            case 9:
                homework.increaseMinionsAgeWithProcedureEx9();

                // Creation have to be done in Query Console
//                CREATE PROCEDURE usp_get_older (minion_id INT)
//                BEGIN
//
//                UPDATE minions
//                SET age = age + 1
//                WHERE id = minion_id;
//
//                END
                break;
        }

    }
}