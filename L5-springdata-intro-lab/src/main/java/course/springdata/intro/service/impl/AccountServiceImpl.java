package course.springdata.intro.service.impl;

import course.springdata.intro.dao.AccountRepository;
import course.springdata.intro.entity.Account;
import course.springdata.intro.entity.User;
import course.springdata.intro.exception.InvalidAccountOperationException;
import course.springdata.intro.exception.NonExistingEntityException;
import course.springdata.intro.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional // Commit if no exceptions in All methods or rollback
@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepo;

    @Autowired
    public void setAccountRepo(AccountRepository accountRepo) { // dependency injection
        this.accountRepo = accountRepo;
    }

    @Override
    public Account createUserAccount(User user, Account account) {
        account.setId(null); // when we set it to null we are sure that we create new account, not update existing
        account.setUser(user);
        user.getAccounts().add(account);
        return this.accountRepo.save(account);
    }

    @Override
    public void withdrawMoney(BigDecimal amount, Long accountId) {

        Account account = this.accountRepo.findById(accountId).orElseThrow(() ->
                new NonExistingEntityException(String.format("Entity with ID:%s does not exist.", accountId)));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InvalidAccountOperationException(
                    String.format("Error withdrawing amount: %s. Insufficient amount:%s in account ID:%s.",
                            amount, account.getBalance(), accountId));
        }

        account.setBalance(account.getBalance().subtract(amount)); // Commit if no exceptions - @Transactional

    }

    @Override
    public void depositMoney(BigDecimal amount, Long accountId) {

        Account account = this.accountRepo.findById(accountId).orElseThrow(() ->
                new NonExistingEntityException(String.format("Entity with ID:%s does not exist.", accountId)));

        account.setBalance(account.getBalance().add(amount)); // Commit if no exceptions - @Transactional

    }

    @Override
    public void transferMoney(BigDecimal amount, Long fromAccountId, Long toAccountId) {

        depositMoney(amount, toAccountId);
        withdrawMoney(amount, fromAccountId);

    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }


}
