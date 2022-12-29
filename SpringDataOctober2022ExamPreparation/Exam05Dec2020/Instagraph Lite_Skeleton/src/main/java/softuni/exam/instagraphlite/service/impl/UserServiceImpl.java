package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.UsersSeedDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE_PATH = "src/main/resources/files/users.json";
    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PictureService pictureService, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USERS_FILE_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder sb = new StringBuilder();

        UsersSeedDto[] usersSeedDtos = this.gson.fromJson(this.readFromFileContent(), UsersSeedDto[].class);
// Never try collect it toList() then save the list in DB, because it will enter repeating entities if exists!!! If you want to collect it firs than save use Set<> equals() and hashCode()
        Arrays.stream(usersSeedDtos)
                .filter(usersSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(usersSeedDto)
                            && !isEntityExist(usersSeedDto.getUsername())
                            && isPictureExist(usersSeedDto.getProfilePicture());
                    generateOutputContent(sb, usersSeedDto, isValid);
                    return isValid;
                })
                .map(usersSeedDto -> {
                    User user = this.modelMapper.map(usersSeedDto, User.class);
                    Picture profilePicture = this.pictureService.findPictureByPath(usersSeedDto.getProfilePicture());
                    user.setProfilePicture(profilePicture);
                    return user;
                })
                .forEach(userRepository::save);

        return sb.toString().trim();
    }

    private boolean isPictureExist(String profilePicturePath) {
        return this.pictureService.isEntityExist(profilePicturePath);
    }

    @Override
    public boolean isEntityExist(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    private static void generateOutputContent(StringBuilder sb, UsersSeedDto usersSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported User: %s", usersSeedDto.getUsername())
                        : "Invalid User")
                .append(System.lineSeparator());
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder sb = new StringBuilder();
        List<User> users = this.userRepository.findAllOrderByCountDescThanById();
//        for (User user : users) {
////            List<Post> sorted = user.getPosts().stream().sorted((f, s) -> Double.compare(f.getPicture().getSize(), s.getPicture().getSize())).toList();
//            List<Post> sorted = user.getPosts().stream().sorted(Comparator.comparingDouble(f -> f.getPicture().getSize())).toList();
//            Set<Post> sortedSet = new LinkedHashSet<>(sorted);
//            user.setPosts(sortedSet);
//        }

        users.forEach(user -> {
            List<Post> sorted = user.getPosts().stream().sorted(Comparator.comparingDouble(f -> f.getPicture().getSize())).toList();
            Set<Post> sortedSet = new LinkedHashSet<>(sorted); // Never use just HashSet<>() because you will lose the order from previous sort
            user.setPosts(sortedSet);
        });

        users.forEach(user -> {
            sb.append(String.format("User: %s\n" +
                    "Post count: %d\n", user.getUsername(), user.getPosts().size()));
            Set<Post> posts = user.getPosts();
            posts.forEach(post -> {
                sb.append(String.format("==Post Details:\n" +
                        "----Caption: %s\n" +
                        "----Picture Size: %.2f\n", post.getCaption(), post.getPicture().getSize()));
            });

        });

        return sb.toString().trim();
    }

}
