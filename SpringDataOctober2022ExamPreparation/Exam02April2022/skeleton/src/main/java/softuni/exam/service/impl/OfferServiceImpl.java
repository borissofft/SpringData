package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedDto;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final AgentService agentService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, AgentService agentService, ApartmentService apartmentService,
                            ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.offerRepository = offerRepository;
        this.agentService = agentService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        OfferSeedRootDto offerSeedRootDto = this.xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class);
//        List<Offer> offers =
        offerSeedRootDto.getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(offerSeedDto)
                            && isAgentExist(offerSeedDto.getAgent().getName());
                    generateOutputContent(sb, offerSeedDto, isValid);
                    return isValid;
                })
                .map(offerSeedDto -> {
                    Offer offer = this.modelMapper.map(offerSeedDto, Offer.class);
                    Agent agent = this.agentService.findAgentByFirstName(offerSeedDto.getAgent().getName());
                    Apartment apartment = this.apartmentService.findApartmentById(offerSeedDto.getApartment().getId());
                    offer.setAgent(agent);
                    offer.setApartment(apartment);
                    return offer;
                })
                .forEach(offerRepository::save);
//                        .toList();
        return sb.toString().trim();
    }

    private boolean isAgentExist(String agentFirstName) {
        return this.agentService.isEntityExist(agentFirstName);
    }

            private static void generateOutputContent(StringBuilder sb, OfferSeedDto offerSeedDto, boolean isValid) {
                sb.append(isValid ? String.format("Successfully imported offer %.2f", offerSeedDto.getPrice())
                                : "Invalid offer")
                        .append(System.lineSeparator());
            }

    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();
        List<Offer> bestOffers = this.offerRepository.findAllByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType.three_rooms);
        bestOffers.forEach(offer -> sb.append(String.format("Agent %s %s with offer №%d:\n" +
                        "-Apartment area: %.2f\n" +
                        "--Town: %s\n" +
                        "---Price: %.2f$\n",
                offer.getAgent().getFirstName(), offer.getAgent().getLastName(), offer.getId(),
                offer.getApartment().getArea(), offer.getApartment().getTown().getTownName(), offer.getPrice())));
        return sb.toString().trim();
    }

}
