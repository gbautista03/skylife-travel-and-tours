package com.skylife.travel.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A OHDC.
 */
@Entity
@Table(name = "ohdc")
public class OHDC implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "destination", nullable = false, unique = true)
    private String destination;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "contact_description", nullable = false)
    private String contactDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OHDC id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestination() {
        return this.destination;
    }

    public OHDC destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return this.description;
    }

    public OHDC description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactDescription() {
        return this.contactDescription;
    }

    public OHDC contactDescription(String contactDescription) {
        this.setContactDescription(contactDescription);
        return this;
    }

    public void setContactDescription(String contactDescription) {
        this.contactDescription = contactDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OHDC)) {
            return false;
        }
        return id != null && id.equals(((OHDC) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OHDC{" +
            "id=" + getId() +
            ", destination='" + getDestination() + "'" +
            ", description='" + getDescription() + "'" +
            ", contactDescription='" + getContactDescription() + "'" +
            "}";
    }
}
