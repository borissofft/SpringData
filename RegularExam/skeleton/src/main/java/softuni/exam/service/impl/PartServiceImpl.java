package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartSeedDto;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class PartServiceImpl implements PartService {

    private static final String PARTS_FILE_PATH = "src/main/resources/files/json/parts.json";
    private static final String INVALID_PART = "Invalid part";
    private static final String VALID_PART = "Successfully imported part %s - %.2f";
    private final PartRepository partRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Path.of(PARTS_FILE_PATH));
    }

    @Override
    public String importParts() throws IOException {
        StringBuilder sb = new StringBuilder();
        PartSeedDto[] partSeedDtos = this.gson.fromJson(this.readPartsFileContent(), PartSeedDto[].class);
//        List<Part> parts =
        Arrays.stream(partSeedDtos)
                .filter(partSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(partSeedDto)
                            && !isEntityExist(partSeedDto.getPartName());
                    generateOutputContent(sb, partSeedDto, isValid);
                    return isValid;
                })
                .map(partSeedDto -> this.modelMapper.map(partSeedDto, Part.class))
                .forEach(partRepository::save);
//                .toList();

        return sb.toString().trim();
    }

    @Override
    public Part findPartById(long partId) {
        return this.partRepository.findById(partId).orElse(null);
    }

    private static void generateOutputContent(StringBuilder sb, PartSeedDto partSeedDto, boolean isValid) {
        sb.append(isValid ? String.format(VALID_PART, partSeedDto.getPartName(), partSeedDto.getPrice())
                        : INVALID_PART)
                .append(System.lineSeparator());
    }

    private boolean isEntityExist(String partName) {
        return this.partRepository.existsByPartName(partName);
    }

}
