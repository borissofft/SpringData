package com.example.dtoexercise;

import com.example.dtoexercise.model.dto.GameAddDto;
import com.example.dtoexercise.model.dto.UserLoginDto;
import com.example.dtoexercise.model.dto.UserRegisterDto;
import com.example.dtoexercise.service.GameService;
import com.example.dtoexercise.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final BufferedReader reader;
    private final UserService userService;
    private final GameService gameService;

    public CommandLineRunnerImpl(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Remember: After delete game drop DB and add it again because Id which is needed for edit and delete commands will be different!");
            System.out.println("Please enter your commands:");

            String[] commands = reader.readLine().split("\\|");
            String command = commands[0];

            switch (command) {
                case "RegisterUser" -> this.userService
                        .registerUser(new UserRegisterDto(commands[1], commands[2], commands[3], commands[4]));
                case "LoginUser" -> this.userService
                        .loginUser(new UserLoginDto(commands[1], commands[2]));
                case "Logout" -> this.userService
                        .logOut();
                case "AddGame" -> this.gameService
                        .addGame(new GameAddDto(commands[1],
                                new BigDecimal(commands[2]),
                                Double.parseDouble(commands[3]),
                                commands[4], commands[5], commands[6],
//                                commands[7]));
                                LocalDate.parse(commands[7], DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                case "EditGame" -> this.gameService
                        .editGame(Long.parseLong(commands[1]),
                                new BigDecimal(commands[2].substring(6)),
                                Double.parseDouble(commands[3].substring(5)));
                case "DeleteGame" -> this.gameService
                        .deleteGame(Long.parseLong(commands[1]));
                case "AllGames" -> this.gameService
                        .printTitlesAndPriceForAllGames();
                case "DetailGame" -> this.gameService
                        .getDetailsFromGame(commands[1]);
                case "BuyGame" -> this.userService
                        .gamePurchase(commands[1]);
                case "OwnedGames" -> this.userService
                        .getOwnedGames();
            }

        }

    }

}
