package com.skylife.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PackageTour.
 */
@Entity
@Table(name = "package_tour")
public class PackageTour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "days", nullable = false)
    private Integer days;

    @NotNull
    @Column(name = "nights", nullable = false)
    private Integer nights;

    @NotNull
    @Column(name = "destination", nullable = false)
    private String destination;

    @NotNull
    @Column(name = "tour_code", nullable = false, unique = true)
    private String tourCode;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "hotel", nullable = false)
    private String hotel;

    @NotNull
    @Column(name = "room_type", nullable = false)
    private String roomType;

    @NotNull
    @Column(name = "number_of_guest", nullable = false)
    private Integer numberOfGuest;

    @OneToOne
    @JoinColumn(unique = true)
    private PackageInclusionsExclusions inclusionExclusion;

    @OneToOne
    @JoinColumn(unique = true)
    private Requirements requirements;

    @OneToOne
    @JoinColumn(unique = true)
    private OHDC ohdc;

    @ManyToMany
    @JoinTable(
        name = "rel_package_tour__passenger",
        joinColumns = @JoinColumn(name = "package_tour_id"),
        inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    @JsonIgnoreProperties(value = { "packageTours" }, allowSetters = true)
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_package_tour__flight_details",
        joinColumns = @JoinColumn(name = "package_tour_id"),
        inverseJoinColumns = @JoinColumn(name = "flight_details_id")
    )
    @JsonIgnoreProperties(value = { "packageTours" }, allowSetters = true)
    private Set<FlightDetails> flightDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PackageTour id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDays() {
        return this.days;
    }

    public PackageTour days(Integer days) {
        this.setDays(days);
        return this;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getNights() {
        return this.nights;
    }

    public PackageTour nights(Integer nights) {
        this.setNights(nights);
        return this;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }

    public String getDestination() {
        return this.destination;
    }

    public PackageTour destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTourCode() {
        return this.tourCode;
    }

    public PackageTour tourCode(String tourCode) {
        this.setTourCode(tourCode);
        return this;
    }

    public void setTourCode(String tourCode) {
        this.tourCode = tourCode;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PackageTour date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHotel() {
        return this.hotel;
    }

    public PackageTour hotel(String hotel) {
        this.setHotel(hotel);
        return this;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public PackageTour roomType(String roomType) {
        this.setRoomType(roomType);
        return this;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getNumberOfGuest() {
        return this.numberOfGuest;
    }

    public PackageTour numberOfGuest(Integer numberOfGuest) {
        this.setNumberOfGuest(numberOfGuest);
        return this;
    }

    public void setNumberOfGuest(Integer numberOfGuest) {
        this.numberOfGuest = numberOfGuest;
    }

    public PackageInclusionsExclusions getInclusionExclusion() {
        return this.inclusionExclusion;
    }

    public void setInclusionExclusion(PackageInclusionsExclusions packageInclusionsExclusions) {
        this.inclusionExclusion = packageInclusionsExclusions;
    }

    public PackageTour inclusionExclusion(PackageInclusionsExclusions packageInclusionsExclusions) {
        this.setInclusionExclusion(packageInclusionsExclusions);
        return this;
    }

    public Requirements getRequirements() {
        return this.requirements;
    }

    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    public PackageTour requirements(Requirements requirements) {
        this.setRequirements(requirements);
        return this;
    }

    public OHDC getOhdc() {
        return this.ohdc;
    }

    public void setOhdc(OHDC oHDC) {
        this.ohdc = oHDC;
    }

    public PackageTour ohdc(OHDC oHDC) {
        this.setOhdc(oHDC);
        return this;
    }

    public Set<Passenger> getPassengers() {
        return this.passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public PackageTour passengers(Set<Passenger> passengers) {
        this.setPassengers(passengers);
        return this;
    }

    public PackageTour addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.getPackageTours().add(this);
        return this;
    }

    public PackageTour removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.getPackageTours().remove(this);
        return this;
    }

    public Set<FlightDetails> getFlightDetails() {
        return this.flightDetails;
    }

    public void setFlightDetails(Set<FlightDetails> flightDetails) {
        this.flightDetails = flightDetails;
    }

    public PackageTour flightDetails(Set<FlightDetails> flightDetails) {
        this.setFlightDetails(flightDetails);
        return this;
    }

    public PackageTour addFlightDetails(FlightDetails flightDetails) {
        this.flightDetails.add(flightDetails);
        flightDetails.getPackageTours().add(this);
        return this;
    }

    public PackageTour removeFlightDetails(FlightDetails flightDetails) {
        this.flightDetails.remove(flightDetails);
        flightDetails.getPackageTours().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackageTour)) {
            return false;
        }
        return id != null && id.equals(((PackageTour) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PackageTour{" +
            "id=" + getId() +
            ", days=" + getDays() +
            ", nights=" + getNights() +
            ", destination='" + getDestination() + "'" +
            ", tourCode='" + getTourCode() + "'" +
            ", date='" + getDate() + "'" +
            ", hotel='" + getHotel() + "'" +
            ", roomType='" + getRoomType() + "'" +
            ", numberOfGuest=" + getNumberOfGuest() +
            "}";
    }
}
