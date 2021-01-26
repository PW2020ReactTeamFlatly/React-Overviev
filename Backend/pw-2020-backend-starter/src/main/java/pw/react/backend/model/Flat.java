package pw.react.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pw.react.backend.utils.JsonDateDeserializer;
import pw.react.backend.utils.JsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "flat")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Flat implements Serializable {
    public static Flat EMPTY = new Flat();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "pricePerNight")
    private int pricePerNight;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "information")
    private String information;

    @Column(name = "rating")
    private int rating;

    //@OneToOne(cascade = CascadeType.ALL)
    //private Reservation reservation;

    //@OneToMany(fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "flat")
    private List<Reservation> reservations;

    public Flat() {}
    public Flat(String  Name, int  PricePerNight, String City, String Address, String Information, int Rating )
    {
        this.name = Name;
        this.pricePerNight = PricePerNight;
        this.city = City;
        this.address = Address;
        this.information = Information;
        this.rating = Rating;
    }
    // Getters and Setters should be provided below

}