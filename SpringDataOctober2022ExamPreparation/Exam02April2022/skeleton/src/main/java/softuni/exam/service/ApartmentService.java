package softuni.exam.service;

import softuni.exam.models.dto.ApartmentByIdDto;
import softuni.exam.models.entity.Apartment;

import javax.xml.bind.JAXBException;
import java.io.IOException;


public interface ApartmentService {
    
    boolean areImported();

    String readApartmentsFromFile() throws IOException;

    String importApartments() throws IOException, JAXBException;

    Apartment findApartmentById(long apartmentId);
}
