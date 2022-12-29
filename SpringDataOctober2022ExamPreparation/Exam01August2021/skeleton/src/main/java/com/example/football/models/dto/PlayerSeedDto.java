package com.example.football.models.dto;

import com.example.football.models.entity.Position;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "player")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDto {

    @XmlElement(name = "first-name")
    private String firstName;
    @XmlElement(name = "last-name")
    private String lastName;
    @XmlElement()
    private String email;
    @XmlElement(name = "birth-date")
    private String birthDate;
    @XmlElement()
    private Position position;
    @XmlElement()
    private TownByNameDto town;
    @XmlElement()
    private TeamByNameDto team;
    @XmlElement()
    private StatByIdDto stat;

    public PlayerSeedDto() {

    }

    @NotBlank
    @Size(min = 2)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotBlank
    @Size(min = 2)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotBlank
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @NotNull
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public TownByNameDto getTown() {
        return town;
    }

    public void setTown(TownByNameDto town) {
        this.town = town;
    }

    public TeamByNameDto getTeam() {
        return team;
    }

    public void setTeam(TeamByNameDto team) {
        this.team = team;
    }

    public StatByIdDto getStat() {
        return stat;
    }

    public void setStat(StatByIdDto stat) {
        this.stat = stat;
    }
}
