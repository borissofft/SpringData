package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PostSeedDto;
import softuni.exam.instagraphlite.models.dto.PostSeedRootDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final String POSTS_FILE_PATH = "src/main/resources/files/posts.xml";
    private final PostRepository postRepository;
    private final UserService userService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService, PictureService pictureService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(POSTS_FILE_PATH));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        PostSeedRootDto postSeedRootDto = this.xmlParser.fromFile(POSTS_FILE_PATH, PostSeedRootDto.class);
// Never try collect it toList() then save the list in DB, because it will enter repeating entities if exists!!! If you want to collect it firs than save use Set<> equals() and hashCode()
//        List<Post> postList = // !!! Just to see is everything is mapped !!!
        postSeedRootDto.getPosts()
                .stream()
                .filter(postSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(postSeedDto)
                            && isUserExist(postSeedDto.getUser().getUsername())
                            && isPictureExist(postSeedDto.getPicture().getPath());
                    generateOutputContent(sb, postSeedDto, isValid);
                    return isValid;
                })
                .map(postSeedDto -> {
                    Post post = this.modelMapper.map(postSeedDto, Post.class);
                    Picture picture = this.pictureService.findPictureByPath(postSeedDto.getPicture().getPath());
                    post.setPicture(picture);
                    User user = this.userService.findUserByUsername(postSeedDto.getUser().getUsername());
                    post.setUser(user);
                    return post;
                })
                .forEach(postRepository::save);
//                .toList();


        return sb.toString().trim();
    }

    private boolean isUserExist(String username) {
        return this.userService.isEntityExist(username);
    }

    private boolean isPictureExist(String picturePath) {
        return this.pictureService.isEntityExist(picturePath);
    }

    private static void generateOutputContent(StringBuilder sb, PostSeedDto postSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported Post, made by %s", postSeedDto.getUser().getUsername())
                        : "Invalid Post")
                .append(System.lineSeparator());
    }

}

// -23:00