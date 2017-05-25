package com.example.booking.entities;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class Booking {

    @Id
    @GeneratedValue
    private int id;

    private String customerName;

    private long customerPhone;

    @ManyToOne
    private Cottage cottage;

    @ManyToOne
    private ApplicationUser applicationUser;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date arrivalDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date departureDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(long customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Cottage getCottage() {
        return cottage;
    }

    public void setCottage(Cottage cottage) {
        this.cottage = cottage;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.customerName);
        hash = 79 * hash + (int) (this.customerPhone ^ (this.customerPhone >>> 32));
        hash = 79 * hash + Objects.hashCode(this.cottage);
        hash = 79 * hash + Objects.hashCode(this.applicationUser);
        hash = 79 * hash + Objects.hashCode(this.arrivalDate);
        hash = 79 * hash + Objects.hashCode(this.departureDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Booking other = (Booking) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.customerPhone != other.customerPhone) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.cottage, other.cottage)) {
            return false;
        }
        if (!Objects.equals(this.applicationUser, other.applicationUser)) {
            return false;
        }
        if (!Objects.equals(this.arrivalDate, other.arrivalDate)) {
            return false;
        }
        return Objects.equals(this.departureDate, other.departureDate);
    }

    @Override
    public String toString() {
        return "Booking{" + "id=" + id + ", customerName=" + customerName + ", customerPhone=" + customerPhone + ", cottage=" + cottage + ", applicationUser=" + applicationUser + ", arrivalDate=" + arrivalDate + ", departureDate=" + departureDate + '}';
    }
}
