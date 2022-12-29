package course.springdata.intro.init;

import course.springdata.intro.entity.Account;
import course.springdata.intro.entity.User;
import course.springdata.intro.exception.InvalidAccountOperationException;
import course.springdata.intro.exception.NonExistingEntityException;
import course.springdata.intro.service.AccountService;
import course.springdata.intro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class ConsoleRunner implements CommandLineRunner {

    @Autowired // best do injection with constructor, not like this
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {

        User user1 = new User("Ivan Petrov", 42);
        Account account1 = new Account(new BigDecimal(3500));

        user1 = this.userService.register(user1);
        account1 = this.accountService.createUserAccount(user1, account1);

        try {
            this.accountService.withdrawMoney(new BigDecimal(2000), account1.getId());
            this.accountService.depositMoney(new BigDecimal(200), account1.getId());
            this.accountService.getAllAccounts().forEach(System.out::println);
        } catch (NonExistingEntityException | InvalidAccountOperationException ex) {
            log.error(String.format("Error executing operation for account %s: %s", account1, ex.getMessage()));
        }

        System.out.println("\n---------------------------------\n");
        // Transfer demo

        User user2 = new User("Georgi Petrov", 25);
        Account account2 = new Account(new BigDecimal(23000));

        user2 = this.userService.register(user2);
        account2 = this.accountService.createUserAccount(user2, account2);

        try {
            this.accountService.transferMoney(new BigDecimal(1000), account1.getId(), account2.getId());
            this.accountService.getAllAccounts().forEach(System.out::println);
        } catch (NonExistingEntityException | InvalidAccountOperationException ex) {
            log.error(String.format("Error executing operation for account %s: %s", account1, ex.getMessage()));
        }

    }

}
