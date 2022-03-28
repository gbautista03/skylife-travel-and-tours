package com.skylife.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Passenger.
 */
@Entity
@Table(name = "passenger")
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @NotNull
    @Column(name = "gender", nullable = false)
    private String gender;

    @NotNull
    @Column(name = "citizenship", nullable = false)
    private String citizenship;

    @NotNull
    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @NotNull
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @ManyToMany(mappedBy = "passengers")
    @JsonIgnoreProperties(value = { "inclusionExclusion", "requirements", "ohdc", "passengers", "flightDetails" }, allowSetters = true)
    private Set<PackageTour> packageTours = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Passenger id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Passenger firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Passenger lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public Passenger birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return this.gender;
    }

    public Passenger gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCitizenship() {
        return this.citizenship;
    }

    public Passenger citizenship(String citizenship) {
        this.setCitizenship(citizenship);
        return this;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public Passenger contactNumber(String contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Passenger emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Set<PackageTour> getPackageTours() {
        return this.packageTours;
    }

    public void setPackageTours(Set<PackageTour> packageTours) {
        if (this.packageTours != null) {
            this.packageTours.forEach(i -> i.removePassenger(this));
        }
        if (packageTours != null) {
            packageTours.forEach(i -> i.addPassenger(this));
        }
        this.packageTours = packageTours;
    }

    public Passenger packageTours(Set<PackageTour> packageTours) {
        this.setPackageTours(packageTours);
        return this;
    }

    public Passenger addPackageTour(PackageTour packageTour) {
        this.packageTours.add(packageTour);
        packageTour.getPassengers().add(this);
        return this;
    }

    public Passenger removePackageTour(PackageTour packageTour) {
        this.packageTours.remove(packageTour);
        packageTour.getPassengers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Passenger)) {
            return false;
        }
        return id != null && id.equals(((Passenger) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Passenger{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", gender='" + getGender() + "'" +
            ", citizenship='" + getCitizenship() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            "}";
    }
}
