package softuni.exam.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import softuni.exam.domain.enums.Position;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "players")
public class Player extends BaseEntity{

    @Column
    @NotNull
    private String firstName;

    @Column
    @NotNull
    @Length(min = 3, max = 15)
    private String lastName;

    @Column
    @NotNull
    @Min(1)
    @Max(99)
    private int number;

    @Column
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal salary;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private Position position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    private Picture picture;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
}
