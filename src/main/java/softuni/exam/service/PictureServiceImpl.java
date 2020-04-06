package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.Constants;
import softuni.exam.domain.dto.xml.PictureImportDto;
import softuni.exam.domain.dto.xml.PictureImportRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final ValidatorUtil validator;
    private final XmlParser xmlParser;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper mapper, FileUtil fileUtil, ValidatorUtil validator, XmlParser xmlParser) {
        this.pictureRepository = pictureRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.validator = validator;
        this.xmlParser = xmlParser;
    }

    @Override
    public String importPictures() throws JAXBException {
        PictureImportRootDto pictureImportRootDto = this.xmlParser.importXml(PictureImportRootDto.class, Constants.PATH_TO_FILES + "xml/pictures.xml");
        List<String> messages = new ArrayList<>();
        for (PictureImportDto pictureImportDto : pictureImportRootDto.getPictureImportDtos()) {
            if (!validator.isValid(pictureImportDto)){
                messages.add(String.format(Constants.INVALID_DATA_MESSAGE, "picture"));
                continue;
            }
            Picture picture = mapper.map(pictureImportDto, Picture.class);
            this.pictureRepository.saveAndFlush(picture);
            messages.add(String.format(Constants.SUCCESSFULLY_IMPORTED_MESSAGE, picture.getClass().getSimpleName().toLowerCase(),
                    picture.getUrl()));
        }
        return String.join("\n", messages);
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count()>0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_FILES + "xml/pictures.xml");
    }

}

