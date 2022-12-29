package com.example.dtoexercise.service.impl;

import com.example.dtoexercise.model.dto.UserLoginDto;
import com.example.dtoexercise.model.dto.UserRegisterDto;
import com.example.dtoexercise.model.entity.Game;
import com.example.dtoexercise.model.entity.Order;
import com.example.dtoexercise.model.entity.User;
import com.example.dtoexercise.repository.GameRepository;
import com.example.dtoexercise.repository.OrderRepository;
import com.example.dtoexercise.repository.UserRepository;
import com.example.dtoexercise.service.UserService;
import com.example.dtoexercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User loggedInUser;
    private final GameRepository gameRepository;

    private final OrderRepository orderRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, GameRepository gameRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gameRepository = gameRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {

        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("Incorrect password / confirm password");
            return;
        }

        Set<ConstraintViolation<UserRegisterDto>> violations = validationUtil.getViolations(userRegisterDto);

        if (!violations.isEmpty()) {
            violations.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
            return;
        }

        // TODO map dto to Entity and save it to DB
        User user = this.modelMapper.map(userRegisterDto, User.class);

        if (this.userRepository.count() == 0) {
            user.setAdmin(true);
        }

        boolean isUserFound = this.userRepository.findFirstByEmail(userRegisterDto.getEmail()).isPresent();

        if (isUserFound) {
            System.out.println("Email already exist");
            return;
        }

        this.userRepository.save(user);
        System.out.printf("%s was registered%n", user.getFullName());

    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {

        Set<ConstraintViolation<UserLoginDto>> violations = validationUtil.getViolations(userLoginDto);

        if (!violations.isEmpty()) {
            violations.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
            return;
        }

        if (this.loggedInUser != null) {
            System.out.printf("User %s is already logged!%n", this.loggedInUser.getFullName());
            return;
        }

        User user = this.userRepository // we get all details from the user and can check if it is admin later...
                .findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword()).orElse(null);

        if (user == null) {
            System.out.println("Incorrect username / password");
            return;
        }

//        if (this.loggedInUser != null) { // There is already logged user. Not needed for that task!
//            System.out.println();
//        }

        this.loggedInUser = user;
        System.out.printf("Successfully logged in %s%n", this.loggedInUser.getFullName());
    }

    @Override
    public void logOut() {
        if (this.loggedInUser == null) {
            System.out.println("Cannot log out. No user was logged in.");
        } else {
            System.out.printf("User %s successfully logged out%n", this.loggedInUser.getFullName());
            this.loggedInUser = null;
        }
    }

    @Override
    public void gamePurchase(String title) {

        if (this.loggedInUser == null) {
            System.out.println("No logged in user!");
            return;
        }

        Game game = this.gameRepository.findFirstByTitle(title);

        if (game == null) {
            System.out.println("No such game in the game list");
            return;
        }


        User logged = this.userRepository.findFirstByEmail(loggedInUser.getEmail()).orElse(null); // already checked and it is not null
        Set<Game> games = logged.getGames();

        boolean gameExist = false;
        for (Game g : games) {
            if (g.getTitle().equals(title)) {
                gameExist = true;
                break;
            }
        }

        if (gameExist) {
            System.out.printf("Game with name: %s already exist in your orders!%n", title);
            return;
        }

        games.add(game);

        Set<Order> orders = logged.getOrders();
        Order order = new Order(logged, games);
        orders.add(order);

        this.userRepository.save(logged);
        System.out.printf("Successfully bought %s%n", game.getTitle());

    }

    @Override
    public void getOwnedGames() {

        if (this.loggedInUser == null) {
            System.out.println("No logged in user, no owned games!");
            return;
        }

        User logged = this.userRepository.findFirstByEmail(loggedInUser.getEmail()).orElse(null);

        if (logged.getOrders().isEmpty()) {
            System.out.println("Please buy some games first. Use for example: BuyGame|Overwatch");
        }

        logged.getOrders().forEach(order -> order.getGames().forEach(game ->  System.out.printf("%s%n", game.getTitle())));
    }

}
