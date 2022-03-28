package com.skylife.travel.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PackageInclusionsExclusions.
 */
@Entity
@Table(name = "package_inclusions_exclusions")
public class PackageInclusionsExclusions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "destination", nullable = false, unique = true)
    private String destination;

    @Lob
    @Column(name = "inclusions", nullable = false)
    private String inclusions;

    @Lob
    @Column(name = "exclusions", nullable = false)
    private String exclusions;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PackageInclusionsExclusions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestination() {
        return this.destination;
    }

    public PackageInclusionsExclusions destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getInclusions() {
        return this.inclusions;
    }

    public PackageInclusionsExclusions inclusions(String inclusions) {
        this.setInclusions(inclusions);
        return this;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public String getExclusions() {
        return this.exclusions;
    }

    public PackageInclusionsExclusions exclusions(String exclusions) {
        this.setExclusions(exclusions);
        return this;
    }

    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackageInclusionsExclusions)) {
            return false;
        }
        return id != null && id.equals(((PackageInclusionsExclusions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PackageInclusionsExclusions{" +
            "id=" + getId() +
            ", destination='" + getDestination() + "'" +
            ", inclusions='" + getInclusions() + "'" +
            ", exclusions='" + getExclusions() + "'" +
            "}";
    }
}
