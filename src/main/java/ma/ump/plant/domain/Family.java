package ma.ump.plant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Family.
 */
@Entity
@Table(name = "family")
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "family")
    @JsonIgnoreProperties(value = { "ecologicalStatus", "family" }, allowSetters = true)
    private Set<Plant> plants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Family id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Family name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Plant> getPlants() {
        return this.plants;
    }

    public Family plants(Set<Plant> plants) {
        this.setPlants(plants);
        return this;
    }

    public Family addPlants(Plant plant) {
        this.plants.add(plant);
        plant.setFamily(this);
        return this;
    }

    public Family removePlants(Plant plant) {
        this.plants.remove(plant);
        plant.setFamily(null);
        return this;
    }

    public void setPlants(Set<Plant> plants) {
        if (this.plants != null) {
            this.plants.forEach(i -> i.setFamily(null));
        }
        if (plants != null) {
            plants.forEach(i -> i.setFamily(this));
        }
        this.plants = plants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Family)) {
            return false;
        }
        return id != null && id.equals(((Family) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Family{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
