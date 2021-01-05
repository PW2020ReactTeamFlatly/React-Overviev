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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "flat")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Flat implements Serializable {
    public static Flat EMPTY = new Flat();

    //@OneToMany(mappedBy = "flat")
    //@ElementCollection
    //private Set<Reservation> reservations;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private String rating;


}
