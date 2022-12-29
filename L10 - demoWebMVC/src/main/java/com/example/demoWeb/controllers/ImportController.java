package com.example.demoWeb.controllers;

import com.example.demoWeb.dto.*;
import com.example.demoWeb.services.CompanyService;
import com.example.demoWeb.services.EmployeeService;
import com.example.demoWeb.services.ProjectService;
import com.example.demoWeb.util.XmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class ImportController extends BaseController {
    private final CompanyService companyService;
    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final XmlConverter xmlConverter;

    @Autowired
    public ImportController(CompanyService companyService, ProjectService projectService, EmployeeService employeeService, XmlConverter xmlConverter) {
        this.companyService = companyService;
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.xmlConverter = xmlConverter;
    }

    @GetMapping("/import/xml")
    public String importXml(Model model, HttpServletRequest request) {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }
        // addAttribute - adds values for variables in html file(see: import-xml.html)
        model.addAttribute("areImported", new boolean[]{this.companyService.exists(), this.projectService.exists(), this.employeeService.exists()});
        return "xml/import-xml";
    }

    @GetMapping("/import/companies")
    public String importCompanies(Model model, HttpServletRequest request) throws IOException {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("companies", this.companyService.getXmlForImport());
        return "xml/import-companies";
    }

    @PostMapping("/import/companies")
    public String importCompanies(ImportCompaniesDto request) {
        var companyRoot = this.xmlConverter.deserialize(request.getCompanies(), CompanyCollectionDto.class);
        companyRoot.getCompanies().forEach(this.companyService::create);

        return "redirect:/import-xml";
    }


    @GetMapping("/import/projects")
    public String importProjects(Model model, HttpServletRequest request) throws IOException {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("projects", this.projectService.getXmlForImport());
        return "xml/import-projects";
    }

    @PostMapping("/import/projects")
    public String importProjects(ImportProjectsDto request) {
        var projectRoot = this.xmlConverter.deserialize(request.getProjects(), ProjectCollectionDto.class);
        projectRoot.getProjects().forEach(this.projectService::create);

        return "redirect:/import-xml";
    }


    @GetMapping("/import/employees")
    public String importEmployees(Model model, HttpServletRequest request) throws IOException {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("employees" ,this.employeeService.getXmlForImport());
        return "xml/import-employees";
    }

    @PostMapping("/import/employees")
    public String importEmployees(ImportEmployeesDto request) {
        var employeeRoot = this.xmlConverter.deserialize(request.getEmployees(), EmployeeCollectionDto.class);
        employeeRoot.getEmployees().forEach(this.employeeService::create);

        return "redirect:/import-xml";
    }

}
