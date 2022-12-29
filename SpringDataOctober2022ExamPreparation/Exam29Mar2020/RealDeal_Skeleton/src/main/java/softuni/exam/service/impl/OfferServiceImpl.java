package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedDto;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final CarService carService;
    private final SellerService sellerService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, CarService carService, SellerService sellerService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.offerRepository = offerRepository;
        this.carService = carService;
        this.sellerService = sellerService;
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

        // Variant 1 - General
        offerSeedRootDto.getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    LocalDateTime addedOn = LocalDateTime.parse(offerSeedDto.getAddedOn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    boolean isValid = this.validationUtil.isValid(offerSeedDto)
                            && this.offerRepository.findByDescriptionAndAddedOn(offerSeedDto.getDescription(), addedOn).isEmpty();
                    generateOutputContent(sb, offerSeedDto, isValid);
                    return isValid;
                })
                .map(offerSeedDto -> {
                    Offer offer = this.modelMapper.map(offerSeedDto, Offer.class);
                    Car car = this.carService.findById(offerSeedDto.getCar().getId());
                    Seller seller = this.sellerService.findById(offerSeedDto.getSeller().getId());
                    offer.setCar(car);
                    offer.setSeller(seller);
                    offer.setPictures(car.getPictures());
                    return offer;
                })
                .forEach(offerRepository::save);

        // Variant 2 - Only for debug and see all Seller objects
//        List<Offer> offersList = offerSeedRootDto.getOffers()
//                .stream()
//                .filter(offerSeedDto -> {
//                    LocalDateTime addedOn = LocalDateTime.parse(offerSeedDto.getAddedOn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                    boolean isValid = this.validationUtil.isValid(offerSeedDto)
//                            && this.offerRepository.findByDescriptionAndAddedOn(offerSeedDto.getDescription(), addedOn).isEmpty();
//                    generateOutputContent(sb, offerSeedDto, isValid);
//
//                    return isValid;
//                })
//                .map(offerSeedDto -> {
//                    Offer offer = this.modelMapper.map(offerSeedDto, Offer.class);
//                    Car car = this.carService.findById(offerSeedDto.getCar().getId());
//                    Seller seller = this.sellerService.findById(offerSeedDto.getSeller().getId());
//                    offer.setCar(car);
//                    offer.setSeller(seller);
//                    offer.setPictures(car.getPictures());
//                    return offer;
//                })
//                .toList(); // Never try to save the list in DB, because it will enter repeating entities if exists!!! If you want to collect it firs than save use Set<> equals() and hashCode()

        // Variant 3 - Save and Flush without using filter()
//        offerSeedRootDto.getOffers()
//                .forEach(offerSeedDto -> {
//                    LocalDateTime addedOn = LocalDateTime.parse(offerSeedDto.getAddedOn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                    boolean isValid = this.validationUtil.isValid(offerSeedDto)
//                            && this.offerRepository.findByDescriptionAndAddedOn(offerSeedDto.getDescription(), addedOn).isEmpty();
//                    if (isValid) {
//                        Offer offer = this.modelMapper.map(offerSeedDto, Offer.class);
//                        Car car = this.carService.findById(offerSeedDto.getCar().getId());
//                        Seller seller = this.sellerService.findById(offerSeedDto.getSeller().getId());
//                        offer.setCar(car);
//                        offer.setSeller(seller);
//                        offer.setPictures(car.getPictures());
//                        this.offerRepository.saveAndFlush(offer);
//                    }
//                    generateOutputContent(sb, offerSeedDto, isValid);
//                });

        return sb.toString().trim();
    }

    private static void generateOutputContent(StringBuilder sb, OfferSeedDto offerSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully import offer %s - %s", offerSeedDto.getAddedOn(), offerSeedDto.isHasGoldStatus())
                        : "Invalid offer")
                .append(System.lineSeparator());
    }

}
