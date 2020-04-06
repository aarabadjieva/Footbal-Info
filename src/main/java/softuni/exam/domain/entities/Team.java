package softuni.exam.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "teams")
public class Team extends BaseEntity{

    @Column
    @NotNull
    @Length(min = 3, max = 20)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    private Picture picture;

    @OneToMany(mappedBy = "team")
    private List<Player> players;
}
