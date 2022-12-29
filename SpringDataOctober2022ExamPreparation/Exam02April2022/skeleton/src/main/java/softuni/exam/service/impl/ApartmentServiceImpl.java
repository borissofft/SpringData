package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedDto;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Service
public class ApartmentServiceImpl implements ApartmentService {

    private static final String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";

    private final ApartmentRepository apartmentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.apartmentRepository = apartmentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        ApartmentSeedRootDto apartmentSeedRootDto = this.xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDto.class);
//        List<Apartment> apartmentList =
        apartmentSeedRootDto.getApartments()
                .stream()
                .filter(apartmentSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(apartmentSeedDto)
                            && !isEntityExist(apartmentSeedDto.getTown(), apartmentSeedDto.getArea());
                    generateOutputContent(sb, apartmentSeedDto, isValid);
                    return isValid;
                })
                .map(apartmentSeedDto -> {
                    Apartment apartment = this.modelMapper.map(apartmentSeedDto, Apartment.class);
                    Town town = this.townService.findTownByName(apartmentSeedDto.getTown());
                    apartment.setTown(town);
                    return apartment;
                })
                .forEach(apartmentRepository::save);
//                        .toList();

        return sb.toString().trim();
    }

    @Override
    public Apartment findApartmentById(long apartmentId) {
        return this.apartmentRepository.findById(apartmentId).orElse(null);
    }

    private boolean isEntityExist(String townName, double area) {
        Town town = this.townService.findTownByName(townName);
        return this.apartmentRepository.existsByTownAndArea(town, area);
    }

    private static void generateOutputContent(StringBuilder sb, ApartmentSeedDto apartmentSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported apartment %s - %.2f", apartmentSeedDto.getApartmentType(), apartmentSeedDto.getArea())
                        : "Invalid apartment")
                .append(System.lineSeparator());
    }

}
