package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {
    private String townName;
    private int population;

    public Town() {

    }

    @Column(name = "town_name ", nullable = false, unique = true)
    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    @Column(nullable = false)
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Town town = (Town) o;
        return townName.equals(town.townName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(townName);
    }
}
