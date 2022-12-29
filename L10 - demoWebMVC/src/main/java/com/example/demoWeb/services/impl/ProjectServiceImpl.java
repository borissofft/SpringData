package com.example.demoWeb.services.impl;

import com.example.demoWeb.dto.ProjectDto;
import com.example.demoWeb.entity.Project;
import com.example.demoWeb.repositories.ProjectRepository;
import com.example.demoWeb.services.CompanyService;
import com.example.demoWeb.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, CompanyService companyService, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean exists() {
        return this.projectRepository.existsAllBy();
    }

    @Override
    public String getXmlForImport() throws IOException {
        return new String(this.getClass().getClassLoader().getResourceAsStream(FILE_PATH).readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public Long create(ProjectDto request) {
        var existing = this.projectRepository.findFirstByNameAndCompany_Name(request.getName(), request.getCompany().getName());
        if (existing != null) {
            return existing.getId();
        }

        var companyId = this.companyService.create(request.getCompany());
        var company = this.companyService.find(companyId);
        var project =   this.modelMapper.map(request, Project.class);
        project.setCompany(company);
        this.projectRepository.save(project);

        return project.getId();
    }

    @Override
    public Project find(Long id) {
        return this.projectRepository.findById(id).orElse(null);
    }

}
