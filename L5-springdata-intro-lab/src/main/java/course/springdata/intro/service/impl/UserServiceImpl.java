package course.springdata.intro.service.impl;

import course.springdata.intro.dao.UserRepository;
import course.springdata.intro.entity.User;
import course.springdata.intro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional // If no exceptions - transaction commit for all business methods, else rollback(runtime exceptions).
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepo) { // dependency injection. Do it by constructor, not by private field UserRepository userRepo
        this.userRepo = userRepo;
    }

    @Override
    public User register(User user) {
        return this.userRepo.save(user);
    }

}
