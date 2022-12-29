package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PictureSeedDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String PICTURES_FILE_PATH = "src/main/resources/files/json/pictures.json";
    private final PictureRepository pictureRepository; // Every service have to work with only one repository !!!! (It's own repository)
    private final CarService carService; // Never use here repository, always invoque a service
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarService carService, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.carService = carService;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos = this.gson.fromJson(this.readPicturesFromFile(), PictureSeedDto[].class);

        // Variant 1 - General
                Arrays.stream(pictureSeedDtos)
                        .filter(pictureSeedDto -> {
                            boolean isValid = this.validationUtil.isValid(pictureSeedDto)
                                    && this.pictureRepository.findByName(pictureSeedDto.getName()).isEmpty();
                            generateOutputContent(sb, pictureSeedDto, isValid);
                            return isValid;
                        })
                        .map(pictureSeedDto -> {
                            Picture picture = modelMapper.map(pictureSeedDto, Picture.class);
                            Car carById = this.carService.findById(pictureSeedDto.getCar());
                            picture.setCar(carById);
                            return picture;
                        })
                        .forEach(pictureRepository::save);

        // Variant 2 - Only for debug and see all Picture objects
//        List<Picture> pictureList =
//                Arrays.stream(pictureSeedDtos)
//                        .filter(pictureSeedDto -> {
//                            boolean isValid = this.validationUtil.isValid(pictureSeedDto)
//                                    && this.pictureRepository.findByName(pictureSeedDto.getName()).isEmpty();
//                            generateOutputContent(sb, pictureSeedDto, isValid);
//                            return isValid;
//                        })
//                        .map(pictureSeedDto -> {
//                            Picture picture = modelMapper.map(pictureSeedDto, Picture.class);
//                            Car carById = this.carService.findById(pictureSeedDto.getCar());
//                            picture.setCar(carById);
//                            return picture;
//                        })
//                        .toList(); // Never try to save the list in DB, because it will enter repeating entities if exists!!! If you want to collect it firs than save use Set<> equals() and hashCode()

        // Variant 3 - Save and Flush without using filter()
//        Arrays.stream(pictureSeedDtos)
//                .forEach(pictureSeedDto -> {
//                    boolean isValid = this.validationUtil.isValid(pictureSeedDto)
//                            && this.pictureRepository.findByName(pictureSeedDto.getName()).isEmpty();
//                    if (isValid) {
//                        Picture picture = this.modelMapper.map(pictureSeedDto, Picture.class);
//                        Car carById = this.carService.findById(pictureSeedDto.getCar());
//                        picture.setCar(carById);
//                        this.pictureRepository.saveAndFlush(picture);
//                    }
//                    generateOutputContent(sb, pictureSeedDto, isValid);
//                });

        // Variant 4 - Here the massages from valid/invalid content will be wrong if there are pictures with the same name multiple times but the imported data is right,
        // because of using Set with implemented equals() and hashCode() in target Class
//                Set<Picture> pictureSet =
//                Arrays.stream(pictureSeedDtos)
//                        .filter(pictureSeedDto -> {
//                            boolean isValid = this.validationUtil.isValid(pictureSeedDto);
//                            generateOutputContent(sb, pictureSeedDto, isValid);
//                            return isValid;
//                        })
//                        .map(pictureSeedDto -> {
//                            Picture picture = modelMapper.map(pictureSeedDto, Picture.class);
//                            Car carById = this.carService.findById(pictureSeedDto.getCar());
//                            picture.setCar(carById);
//                            return picture;
//                        })
//                        .collect(Collectors.toSet());
//        this.pictureRepository.saveAll(pictureSet);

        return sb.toString().trim();
    }

    private static void generateOutputContent(StringBuilder sb, PictureSeedDto pictureSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully import picture - %s", pictureSeedDto.getName())
                        : "Invalid picture")
                .append(System.lineSeparator());
    }

}
