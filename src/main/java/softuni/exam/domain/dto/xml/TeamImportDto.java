package softuni.exam.domain.dto.xml;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamImportDto {

    @XmlElement
    @Expose
    private String name;

    @XmlElement(name = "picture")
    @Expose
    private PictureImportDto picture;
}
