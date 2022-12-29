package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {

    private static final String AGENTS_FILE_PATH = "src/main/resources/files/json/agents.json";

    private final AgentRepository agentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.agentRepository = agentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENTS_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder sb = new StringBuilder();
        AgentSeedDto[] agentSeedDtos = this.gson.fromJson(this.readAgentsFromFile(), AgentSeedDto[].class);
//        List<Agent> agents =
        Arrays.stream(agentSeedDtos)
                .filter(agentSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(agentSeedDto)
                            && !isEntityExist(agentSeedDto.getFirstName());
                    generateOutputContent(sb, agentSeedDto, isValid);
                    return isValid;
                })
                .map(agentSeedDto -> {
                    Agent agent = this.modelMapper.map(agentSeedDto, Agent.class);
                    Town town = this.townService.findTownByName(agentSeedDto.getTown());
                    agent.setTown(town);
                    return agent;
                })
                .forEach(agentRepository::save);
//                .toList();
        return sb.toString().trim();
    }

    @Override
    public boolean isEntityExist(String firstName) {
        return this.agentRepository.existsByFirstName(firstName);
    }

    @Override
    public Agent findAgentByFirstName(String firstName) {
        return this.agentRepository.findByFirstName(firstName).orElse(null);
    }

    private static void generateOutputContent(StringBuilder sb, AgentSeedDto agentSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported agent - %s %s", agentSeedDto.getFirstName(), agentSeedDto.getLastName())
                        : "Invalid agent")
                .append(System.lineSeparator());
    }

}
