package com.skylife.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FlightDetails.
 */
@Entity
@Table(name = "flight_details")
public class FlightDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "origin", nullable = false)
    private String origin;

    @NotNull
    @Column(name = "destination", nullable = false)
    private String destination;

    @NotNull
    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @NotNull
    @Column(name = "carrier", nullable = false)
    private String carrier;

    @NotNull
    @Column(name = "departure_date", nullable = false)
    private ZonedDateTime departureDate;

    @Column(name = "arrival_date")
    private ZonedDateTime arrivalDate;

    @ManyToMany(mappedBy = "flightDetails")
    @JsonIgnoreProperties(value = { "inclusionExclusion", "requirements", "ohdc", "passengers", "flightDetails" }, allowSetters = true)
    private Set<PackageTour> packageTours = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FlightDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return this.origin;
    }

    public FlightDetails origin(String origin) {
        this.setOrigin(origin);
        return this;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public FlightDetails destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }

    public FlightDetails flightNumber(String flightNumber) {
        this.setFlightNumber(flightNumber);
        return this;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getCarrier() {
        return this.carrier;
    }

    public FlightDetails carrier(String carrier) {
        this.setCarrier(carrier);
        return this;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public ZonedDateTime getDepartureDate() {
        return this.departureDate;
    }

    public FlightDetails departureDate(ZonedDateTime departureDate) {
        this.setDepartureDate(departureDate);
        return this;
    }

    public void setDepartureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public ZonedDateTime getArrivalDate() {
        return this.arrivalDate;
    }

    public FlightDetails arrivalDate(ZonedDateTime arrivalDate) {
        this.setArrivalDate(arrivalDate);
        return this;
    }

    public void setArrivalDate(ZonedDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Set<PackageTour> getPackageTours() {
        return this.packageTours;
    }

    public void setPackageTours(Set<PackageTour> packageTours) {
        if (this.packageTours != null) {
            this.packageTours.forEach(i -> i.removeFlightDetails(this));
        }
        if (packageTours != null) {
            packageTours.forEach(i -> i.addFlightDetails(this));
        }
        this.packageTours = packageTours;
    }

    public FlightDetails packageTours(Set<PackageTour> packageTours) {
        this.setPackageTours(packageTours);
        return this;
    }

    public FlightDetails addPackageTour(PackageTour packageTour) {
        this.packageTours.add(packageTour);
        packageTour.getFlightDetails().add(this);
        return this;
    }

    public FlightDetails removePackageTour(PackageTour packageTour) {
        this.packageTours.remove(packageTour);
        packageTour.getFlightDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlightDetails)) {
            return false;
        }
        return id != null && id.equals(((FlightDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlightDetails{" +
            "id=" + getId() +
            ", origin='" + getOrigin() + "'" +
            ", destination='" + getDestination() + "'" +
            ", flightNumber='" + getFlightNumber() + "'" +
            ", carrier='" + getCarrier() + "'" +
            ", departureDate='" + getDepartureDate() + "'" +
            ", arrivalDate='" + getArrivalDate() + "'" +
            "}";
    }
}
