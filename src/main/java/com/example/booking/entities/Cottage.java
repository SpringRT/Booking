package com.example.booking.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Cottage {

    @Id
    @GeneratedValue
    private int id;

    private int beds;

    private boolean parkingLot;

    private boolean playground;

    private boolean arbour;

    private boolean pets;

    private float rent;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "cottage")
    private List<Booking> bookings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public boolean isParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(boolean parkingLot) {
        this.parkingLot = parkingLot;
    }

    public boolean isPlayground() {
        return playground;
    }

    public void setPlayground(boolean playground) {
        this.playground = playground;
    }

    public boolean isArbour() {
        return arbour;
    }

    public void setArbour(boolean arbour) {
        this.arbour = arbour;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public float getRent() {
        return rent;
    }

    public void setRent(float rent) {
        this.rent = rent;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.id;
        hash = 89 * hash + this.beds;
        hash = 89 * hash + (this.parkingLot ? 1 : 0);
        hash = 89 * hash + (this.playground ? 1 : 0);
        hash = 89 * hash + (this.arbour ? 1 : 0);
        hash = 89 * hash + (this.pets ? 1 : 0);
        hash = 89 * hash + Float.floatToIntBits(this.rent);
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
        final Cottage other = (Cottage) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.beds != other.beds) {
            return false;
        }
        if (this.parkingLot != other.parkingLot) {
            return false;
        }
        if (this.playground != other.playground) {
            return false;
        }
        if (this.arbour != other.arbour) {
            return false;
        }
        if (this.pets != other.pets) {
            return false;
        }
        return Float.floatToIntBits(this.rent) == Float.floatToIntBits(other.rent);
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder("#")
                .append(id)
                .append(", спальных мест: ")
                .append(beds)
                .append(", аренда: ")
                .append(rent);

        List<String> options = new ArrayList<>(4);
        if (arbour) {
            options.add("беседка");
        }

        if (parkingLot) {
            options.add("парковка");
        }

        if (playground) {
            options.add("детская площадка");
        }

        if (pets) {
            options.add("с питомцами");
        }

        if (!options.isEmpty()) {
            stringBuilder.append(", опции: ")
                    .append(options.stream().collect(Collectors.joining(", ")));
        }

        return stringBuilder.toString();
    }
}
