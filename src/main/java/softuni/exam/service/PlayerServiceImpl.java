package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.Constants;
import softuni.exam.domain.dto.json.PlayerImportDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final ValidatorUtil validator;
    private final Gson gson;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, PictureRepository pictureRepository, ModelMapper mapper, FileUtil fileUtil, ValidatorUtil validator, Gson gson) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.validator = validator;
        this.gson = gson;
    }

    @Override
    public String importPlayers() throws IOException {
        String playersFileContent = readPlayersJsonFile();
        PlayerImportDto[] playerImportDtos = this.gson.fromJson(playersFileContent, PlayerImportDto[].class);
        List<String> messages = new ArrayList<>();
        for (PlayerImportDto playerImportDto : playerImportDtos) {
            Picture picture = this.pictureRepository.findByUrl(playerImportDto.getPicture().getUrl()).orElse(null);
            Picture teamPic = this.pictureRepository.findByUrl(playerImportDto.getTeam().getPicture().getUrl()).orElse(null);
            if (picture==null||teamPic==null){
                messages.add(String.format(Constants.INVALID_DATA_MESSAGE, "player"));
                continue;
            }
            Team team = this.teamRepository.findByNameAndPictureId(playerImportDto.getTeam().getName(), teamPic.getId()).orElse(null);
            Player player = mapper.map(playerImportDto, Player.class);
            player.setPicture(picture);
            player.setTeam(team);
            if (team==null||!validator.isValid(player)){
                messages.add(String.format(Constants.INVALID_DATA_MESSAGE, "player"));
                continue;
            }
            this.playerRepository.saveAndFlush(player);
            messages.add(String.format(Constants.SUCCESSFULLY_IMPORTED_MESSAGE, player.getClass().getSimpleName().toLowerCase(),
                    player.getFirstName() + " " + player.getLastName()));
        }
        return String.join("\n", messages);
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count()>0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_FILES + "json/players.json");
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        List<Player> playersBySalaryHigherThan = this.playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000));
        List<String> result = new ArrayList<>();
        for (Player player : playersBySalaryHigherThan) {
            result.add(String.format("Player name: %s %s\n" +
                            "   Number: %d\n" +
                            "   Salary: %s\n" +
                            "   Team: %s", player.getFirstName(), player.getLastName(),
                    player.getNumber(), player.getSalary(), player.getTeam().getName()));
        }
        return String.join("\n", result);
    }

    @Override
    public String exportPlayersInATeam() {
        List<Player> playersByTeam = this.playerRepository.findAllByTeam_NameOrderByIdAsc("North Hub");
        List<String> result = new ArrayList<>();
        result.add("Team: North Hub");
        for (Player player : playersByTeam) {
            result.add(String.format("  Player name: %s %s - %s\n" +
                            "   Number: %d", player.getFirstName(), player.getLastName(),
                    player.getPosition(), player.getNumber()));
        }
        return String.join("\n", result);
    }
}
