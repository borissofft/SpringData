package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SellerSeedDto;
import softuni.exam.models.dto.SellerSeedRootDto;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SellerServiceImpl implements SellerService {

    private static final String SELLERS_FILE_PATH = "src/main/resources/files/xml/sellers.xml";
    private final SellerRepository sellerRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        SellerSeedRootDto sellerSeedRootDto = this.xmlParser.fromFile(SELLERS_FILE_PATH, SellerSeedRootDto.class);

        // Variant 1 - General
        sellerSeedRootDto.getSellers()
                .stream()
                .filter(sellerSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(sellerSeedDto)
                            && this.sellerRepository.findByEmail(sellerSeedDto.getEmail()).isEmpty();
                    generateOutputContent(sb, sellerSeedDto, isValid);
                    return isValid;
                })
                .map(sellerSeedDto -> modelMapper.map(sellerSeedDto, Seller.class))
                .forEach(sellerRepository::save);

        // Variant 2 - Only for debug and see all Seller objects
//        List<Seller> sellerList =
//                sellerSeedRootDto.getSellers()
//                        .stream()
//                        .filter(sellerSeedDto -> {
//                            boolean isValid = this.validationUtil.isValid(sellerSeedDto)
//                                    && this.sellerRepository.findByEmail(sellerSeedDto.getEmail()).isEmpty();
//                            generateOutputContent(sb, sellerSeedDto, isValid);
//                            return isValid;
//                        })
//                        .map(sellerSeedDto -> modelMapper.map(sellerSeedDto, Seller.class))
//                        .toList(); // Never try to save the list in DB, because it will enter repeating entities if exists!!! If you want to collect it firs than save use Set<> equals() and hashCode()

        // Variant 3 - Save and Flush without using filter()
//        sellerSeedRootDto.getSellers()
//                .forEach(sellerSeedDto -> {
//                    boolean isValid = this.validationUtil.isValid(sellerSeedDto)
//                            && this.sellerRepository.findByEmail(sellerSeedDto.getEmail()).isEmpty();
//                    if (isValid) {
//                        this.sellerRepository.saveAndFlush(this.modelMapper.map(sellerSeedDto, Seller.class));
//                    }
//                    generateOutputContent(sb, sellerSeedDto, isValid);
//                });


        // Variant 4
        // Here the massages from valid/invalid content will be wrong if there are seller with the same email multiple times but the imported data is right,
        // because of using Set with implemented equals() and hashCode() in target Class
//        Set<Seller> sellerSet =
//                sellerSeedRootDto.getSellers()
//                        .stream()
//                        .filter(sellerSeedDto -> {
//                            boolean isValid = this.validationUtil.isValid(sellerSeedDto);
//                            generateOutputContent(sb, sellerSeedDto, isValid);
//                            return isValid;
//                        })
//                        .map(sellerSeedDto -> modelMapper.map(sellerSeedDto, Seller.class))
//                        .collect(Collectors.toSet());
//        this.sellerRepository.saveAll(sellerSet);

        return sb.toString().trim();
    }

    @Override
    public Seller findById(Long id) {
        return this.sellerRepository
                .findById(id)
                .orElse(null);
    }

    private static void generateOutputContent(StringBuilder sb, SellerSeedDto sellerSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully import seller %s - %s", sellerSeedDto.getLastName(), sellerSeedDto.getEmail())
                        : "Invalid seller")
                .append(System.lineSeparator());
    }

}
