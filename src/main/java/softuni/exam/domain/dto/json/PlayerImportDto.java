package softuni.exam.domain.dto.json;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import softuni.exam.domain.dto.xml.PictureImportDto;
import softuni.exam.domain.dto.xml.TeamImportDto;

import java.math.BigDecimal;

@Getter
@Setter
public class PlayerImportDto {

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private int number;

    @Expose
    private BigDecimal salary;

    @Expose
    private String position;

    @Expose
    private PictureImportDto picture;

    @Expose
    private TeamImportDto team;
}
