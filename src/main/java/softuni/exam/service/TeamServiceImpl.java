package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.Constants;
import softuni.exam.domain.dto.xml.TeamImportDto;
import softuni.exam.domain.dto.xml.TeamImportRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final ValidatorUtil validator;
    private final XmlParser xmlParser;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PictureRepository pictureRepository, ModelMapper mapper, FileUtil fileUtil, ValidatorUtil validator, XmlParser xmlParser) {
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.validator = validator;
        this.xmlParser = xmlParser;
    }

    @Override
    public String importTeams() throws JAXBException {
        TeamImportRootDto teamImportRootDto = this.xmlParser.importXml(TeamImportRootDto.class, Constants.PATH_TO_FILES + "xml/teams.xml");
        List<String> messages = new ArrayList<>();
        for (TeamImportDto teamImportDto : teamImportRootDto.getTeamImportDtos()) {
            Picture picture = this.pictureRepository.findByUrl(teamImportDto.getPicture().getUrl()).orElse(null);
            Team team = mapper.map(teamImportDto, Team.class);
            team.setPicture(picture);
            if (picture==null||!validator.isValid(team)){
                messages.add(String.format(Constants.INVALID_DATA_MESSAGE, "team"));
                continue;
            }
            this.teamRepository.saveAndFlush(team);
            messages.add(String.format(Constants.SUCCESSFULLY_IMPORTED_MESSAGE, team.getClass().getSimpleName().toLowerCase(),
                    team.getName()));
        }
        return String.join("\n", messages);
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count()>0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
         return this.fileUtil.readFile(Constants.PATH_TO_FILES + "xml/teams.xml");
    }
}
