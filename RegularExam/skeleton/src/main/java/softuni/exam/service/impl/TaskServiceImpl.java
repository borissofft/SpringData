package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskSeedDto;
import softuni.exam.models.dto.TaskSeedRootDto;
import softuni.exam.models.entity.CarType;
import softuni.exam.models.entity.Task;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.MechanicService;
import softuni.exam.service.PartService;
import softuni.exam.service.TaskService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private static final String TASKS_FILE_PATH = "src/main/resources/files/xml/tasks.xml";
    private static final String INVALID_TASK = "Invalid task";
    private static final String VALID_TASK = "Successfully imported task %.2f";

    private static final String HIGHEST_PRICED_TASKS = "Car %s %s with %dkm\n" +
            "-Mechanic: %s %s - task №%d:\n" +
            "--Engine: %.1f\n" +
            "---Price: %.2f$\n";
    private final TaskRepository taskRepository;
    private final CarService carService;
    private final PartService partService;
    private final MechanicService mechanicService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, CarService carService, PartService partService,
                           MechanicService mechanicService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.taskRepository = taskRepository;
        this.carService = carService;
        this.partService = partService;
        this.mechanicService = mechanicService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of(TASKS_FILE_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        TaskSeedRootDto taskSeedRootDto = this.xmlParser.fromFile(TASKS_FILE_PATH, TaskSeedRootDto.class);
//        List<Task> tasks =
        taskSeedRootDto.getTasks()
                .stream()
                .filter(taskSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(taskSeedDto)
                            && isCarExist(taskSeedDto.getCar().getId())
                            && isMechanicExist(taskSeedDto.getMechanic().getFirstName());
                    generateOutputContent(sb, taskSeedDto, isValid);
                    return isValid;
                })
                .map(taskSeedDto -> {
                    Task task = this.modelMapper.map(taskSeedDto, Task.class);
                    task.setCar(this.carService.findCarById(taskSeedDto.getCar().getId()));
                    task.setMechanic(this.mechanicService.findMechanicByFirstName(taskSeedDto.getMechanic().getFirstName()));
                    task.setPart(this.partService.findPartById(taskSeedDto.getPart().getId()));
                    return task;
                })
                .forEach(taskRepository::save);
//                .toList();
        return sb.toString().trim();
    }

    private boolean isMechanicExist(String firstName) {
        return this.mechanicService.existByFirstName(firstName);
    }

    private boolean isCarExist(long id) {
        return this.carService.existCarById(id);
    }

    private static void generateOutputContent(StringBuilder sb, TaskSeedDto taskSeedDto, boolean isValid) {
        sb.append(isValid ? String.format(VALID_TASK, taskSeedDto.getPrice())
                        : INVALID_TASK)
                .append(System.lineSeparator());
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {
        StringBuilder sb = new StringBuilder();
        List<Task> taskList = this.taskRepository.findAllByCar_CarTypeOrderByPriceDesc(CarType.coupe);
        taskList.forEach(task -> sb.append(String.format(HIGHEST_PRICED_TASKS,
                task.getCar().getCarMake(), task.getCar().getCarModel(), task.getCar().getKilometers(),
                task.getMechanic().getFirstName(), task.getMechanic().getLastName(), task.getId(),
                task.getCar().getEngine(), task.getPrice())));

        return sb.toString().trim();
    }

}
