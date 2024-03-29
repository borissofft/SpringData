package com.example.football.service.impl;

import com.example.football.models.dto.StatSeedDto;
import com.example.football.models.dto.StatSeedRootDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {

    private static final String STATS_FILE_PATH = "src/main/resources/files/xml/stats.xml";
    private final StatRepository statRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public StatServiceImpl(StatRepository statRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.statRepository = statRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(STATS_FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        StatSeedRootDto statSeedRootDto = this.xmlParser.fromFile(STATS_FILE_PATH, StatSeedRootDto.class);
//        List<Stat> stats =
        statSeedRootDto.getStats()
                .stream()
                .filter(statSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(statSeedDto)
                            && !isEntityExist(statSeedDto.getPassing(), statSeedDto.getShooting(), statSeedDto.getEndurance());
                    generateOutputContent(sb, statSeedDto, isValid);
                    return isValid;
                })
                .map(statSeedDto -> this.modelMapper.map(statSeedDto, Stat.class))
                .forEach(statRepository::save);
//                .toList();

        return sb.toString().trim();
    }

    @Override
    public Stat findStatById(long id) {
        return this.statRepository.findById(id).orElse(null);
    }

    private boolean isEntityExist(float passing, float shooting, float endurance) {
        return this.statRepository.existsByPassingAndShootingAndEndurance(passing, shooting, endurance);
    }

    private static void generateOutputContent(StringBuilder sb, StatSeedDto statSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported Stat %.2f - %.2f - %.2f", statSeedDto.getShooting(), statSeedDto.getPassing(), statSeedDto.getEndurance())
                        : "Invalid Stat")
                .append(System.lineSeparator());
    }

}
