package ma.ump.plant.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import ma.ump.plant.domain.enumeration.EcologicalStatusType;

/**
 * A EcologicalStatus.
 */
@Entity
@Table(name = "ecological_status")
public class EcologicalStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private EcologicalStatusType name;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EcologicalStatus id(Long id) {
        this.id = id;
        return this;
    }

    public EcologicalStatusType getName() {
        return this.name;
    }

    public EcologicalStatus name(EcologicalStatusType name) {
        this.name = name;
        return this;
    }

    public void setName(EcologicalStatusType name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EcologicalStatus)) {
            return false;
        }
        return id != null && id.equals(((EcologicalStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EcologicalStatus{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
