package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicSeedDto;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class MechanicServiceImpl implements MechanicService {

    private static final String MECHANICS_FILE_PATH = "src/main/resources/files/json/mechanics.json";
    private static final String INVALID_MECHANIC = "Invalid mechanic";
    private static final String VALID_MECHANIC = "Successfully imported mechanic %s %s";
    private final MechanicRepository mechanicRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.mechanicRepository = mechanicRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Path.of(MECHANICS_FILE_PATH));
    }

    @Override
    public String importMechanics() throws IOException {
        StringBuilder sb = new StringBuilder();
        MechanicSeedDto[] mechanicSeedDtos = this.gson.fromJson(this.readMechanicsFromFile(), MechanicSeedDto[].class);
//        List<Mechanic> mechanics =
        Arrays.stream(mechanicSeedDtos)
                .filter(mechanicSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(mechanicSeedDto)
                            && !isEntityByEmailExist(mechanicSeedDto.getEmail());
                    generateOutputContent(sb, mechanicSeedDto, isValid);
                    return isValid;
                })
                .map(mechanicSeedDto -> this.modelMapper.map(mechanicSeedDto, Mechanic.class))
                .forEach(mechanicRepository::save);
//                .toList();
        return sb.toString().trim();
    }

    @Override
    public boolean existByFirstName(String firstName) {
        return this.mechanicRepository.existsByFirstName(firstName);
    }

    @Override
    public Mechanic findMechanicByFirstName(String firstName) {
        return this.mechanicRepository.findByFirstName(firstName);
    }

    private boolean isEntityByEmailExist(String email) {
        return this.mechanicRepository.existsByEmail(email);
    }

    private static void generateOutputContent(StringBuilder sb, MechanicSeedDto mechanicSeedDto, boolean isValid) {
        sb.append(isValid ? String.format(VALID_MECHANIC, mechanicSeedDto.getFirstName(), mechanicSeedDto.getLastName())
                        : INVALID_MECHANIC)
                .append(System.lineSeparator());
    }

}
