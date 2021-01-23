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

@Entity
@Table(name = "reservation")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation implements Serializable {
    public static Reservation EMPTY = new Reservation();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "startDate")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private LocalDateTime startDateTime;

    @Column(name = "endDate")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private LocalDateTime endDateTime;

    @Column(name = "price")
    private int price;

    @Column(name = "sleeps")
    private int sleeps;

    //@OneToOne(mappedBy = "reservation")
    //private Flat flat;
    //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@ManyToOne
    @JoinColumn(name="flat_id")
    private Flat flat;

    public Reservation () {}
    public Reservation (String CustomerName, LocalDateTime StartDateTime, LocalDateTime EndDateTime, int Price, int Sleeps)
    {
        this.customerName = CustomerName;
        this.startDateTime = StartDateTime;
        this.endDateTime = EndDateTime;
        this.price = Price;
        this.sleeps = Sleeps;
    }

    public static Reservation valueOf(ReservationDTO reservationDTO)
    {
        return new Reservation(reservationDTO.CustomerName,
                reservationDTO.StartDateTime,
                reservationDTO.EndDateTime,
                reservationDTO.Price,
                reservationDTO.Sleeps
        );
    }

    // Getters and Setters should be provided below

}
