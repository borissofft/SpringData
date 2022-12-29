package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PictureSeedDto;
import softuni.exam.instagraphlite.models.dto.PictureViewDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String PICTURES_FILE_PATH = "src/main/resources/files/pictures.json";
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos = this.gson.fromJson(this.readFromFileContent(), PictureSeedDto[].class);
// Never try collect it toList() then save the list in DB, because it will enter repeating entities if exists!!! If you want to collect it firs than save use Set<> equals() and hashCode()
        Arrays.stream(pictureSeedDtos)
                .filter(pictureSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(pictureSeedDto)
                            && !isEntityExist(pictureSeedDto.getPath()); // by unique field
                    generateOutputContent(sb, pictureSeedDto, isValid);
                    return isValid;
                })
                .map(pictureSeedDto -> this.modelMapper.map(pictureSeedDto, Picture.class))
                .forEach(pictureRepository::save);

        return sb.toString().trim();
    }

    @Override
    public boolean isEntityExist(String path) {
        return this.pictureRepository.existsByPath(path);
    }

    @Override
    public Picture findPictureByPath(String profilePicturePath) {
        return this.pictureRepository.findByPath(profilePicturePath).orElse(null);
    }

    private static void generateOutputContent(StringBuilder sb, PictureSeedDto pictureSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported Picture, with size %.2f", pictureSeedDto.getSize())
                        : "Invalid picture")
                .append(System.lineSeparator());
    }

    @Override
    public String exportPictures() {
        // Variant 1
//        StringBuilder sb = new StringBuilder();
//        List<Picture> pictures = this.pictureRepository.findAllBySizeGreaterThanOrderBySize(30000.00);
//        pictures.forEach(picture -> sb.append(String.format("%.2f – %s\n", picture.getSize(), picture.getPath())));
//        return sb.toString().trim();

        // Variant 2
        StringBuilder sb = new StringBuilder();
        List<PictureViewDto> pictureViewDtos =
                this.pictureRepository.findAllBySizeGreaterThanOrderBySize(30000.00)
                        .stream()
                        .map(picture -> this.modelMapper.map(picture, PictureViewDto.class)).toList();

        pictureViewDtos.forEach(pictureViewDto -> sb.append(String.format("%.2f – %s%n", pictureViewDto.getSize(), pictureViewDto.getPath())));

        return sb.toString().trim();
    }

}
