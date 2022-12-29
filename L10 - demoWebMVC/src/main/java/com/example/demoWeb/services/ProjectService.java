package com.example.demoWeb.services;

import com.example.demoWeb.dto.ProjectDto;
import com.example.demoWeb.entity.Project;

import java.io.IOException;

public interface ProjectService {

    String FILE_PATH = "files/xmls/projects.xml";
    boolean exists();
    String getXmlForImport() throws IOException;
    Long create(ProjectDto request);

    Project find(Long id);

}
