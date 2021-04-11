package ma.ump.plant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Plant.
 */
@Entity
@Table(name = "plant")
public class Plant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "scientific_name", nullable = false)
    private String scientificName;

    @Column(name = "synonym")
    private String synonym;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "english_name")
    private String englishName;

    @NotNull
    @Column(name = "voucher_number", nullable = false, unique = true)
    private String voucherNumber;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "botanical_description")
    private String botanicalDescription;

    @Column(name = "therapeutic_uses")
    private String therapeuticUses;

    @Column(name = "used_parts")
    private String usedParts;

    @Column(name = "preparation")
    private String preparation;

    @Column(name = "pharmacological_activities")
    private String pharmacologicalActivities;

    @Column(name = "major_phytochemicals")
    private String majorPhytochemicals;

    @OneToOne
    @JoinColumn(unique = true)
    private EcologicalStatus ecologicalStatus;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "plants" }, allowSetters = true)
    private Family family;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plant id(Long id) {
        this.id = id;
        return this;
    }

    public String getScientificName() {
        return this.scientificName;
    }

    public Plant scientificName(String scientificName) {
        this.scientificName = scientificName;
        return this;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getSynonym() {
        return this.synonym;
    }

    public Plant synonym(String synonym) {
        this.synonym = synonym;
        return this;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getLocalName() {
        return this.localName;
    }

    public Plant localName(String localName) {
        this.localName = localName;
        return this;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getEnglishName() {
        return this.englishName;
    }

    public Plant englishName(String englishName) {
        this.englishName = englishName;
        return this;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getVoucherNumber() {
        return this.voucherNumber;
    }

    public Plant voucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
        return this;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Plant picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Plant pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public String getBotanicalDescription() {
        return this.botanicalDescription;
    }

    public Plant botanicalDescription(String botanicalDescription) {
        this.botanicalDescription = botanicalDescription;
        return this;
    }

    public void setBotanicalDescription(String botanicalDescription) {
        this.botanicalDescription = botanicalDescription;
    }

    public String getTherapeuticUses() {
        return this.therapeuticUses;
    }

    public Plant therapeuticUses(String therapeuticUses) {
        this.therapeuticUses = therapeuticUses;
        return this;
    }

    public void setTherapeuticUses(String therapeuticUses) {
        this.therapeuticUses = therapeuticUses;
    }

    public String getUsedParts() {
        return this.usedParts;
    }

    public Plant usedParts(String usedParts) {
        this.usedParts = usedParts;
        return this;
    }

    public void setUsedParts(String usedParts) {
        this.usedParts = usedParts;
    }

    public String getPreparation() {
        return this.preparation;
    }

    public Plant preparation(String preparation) {
        this.preparation = preparation;
        return this;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getPharmacologicalActivities() {
        return this.pharmacologicalActivities;
    }

    public Plant pharmacologicalActivities(String pharmacologicalActivities) {
        this.pharmacologicalActivities = pharmacologicalActivities;
        return this;
    }

    public void setPharmacologicalActivities(String pharmacologicalActivities) {
        this.pharmacologicalActivities = pharmacologicalActivities;
    }

    public String getMajorPhytochemicals() {
        return this.majorPhytochemicals;
    }

    public Plant majorPhytochemicals(String majorPhytochemicals) {
        this.majorPhytochemicals = majorPhytochemicals;
        return this;
    }

    public void setMajorPhytochemicals(String majorPhytochemicals) {
        this.majorPhytochemicals = majorPhytochemicals;
    }

    public EcologicalStatus getEcologicalStatus() {
        return this.ecologicalStatus;
    }

    public Plant ecologicalStatus(EcologicalStatus ecologicalStatus) {
        this.setEcologicalStatus(ecologicalStatus);
        return this;
    }

    public void setEcologicalStatus(EcologicalStatus ecologicalStatus) {
        this.ecologicalStatus = ecologicalStatus;
    }

    public Family getFamily() {
        return this.family;
    }

    public Plant family(Family family) {
        this.setFamily(family);
        return this;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plant)) {
            return false;
        }
        return id != null && id.equals(((Plant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plant{" +
            "id=" + getId() +
            ", scientificName='" + getScientificName() + "'" +
            ", synonym='" + getSynonym() + "'" +
            ", localName='" + getLocalName() + "'" +
            ", englishName='" + getEnglishName() + "'" +
            ", voucherNumber='" + getVoucherNumber() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", botanicalDescription='" + getBotanicalDescription() + "'" +
            ", therapeuticUses='" + getTherapeuticUses() + "'" +
            ", usedParts='" + getUsedParts() + "'" +
            ", preparation='" + getPreparation() + "'" +
            ", pharmacologicalActivities='" + getPharmacologicalActivities() + "'" +
            ", majorPhytochemicals='" + getMajorPhytochemicals() + "'" +
            "}";
    }
}
