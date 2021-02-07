package pw.react.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import pw.react.backend.utils.JsonDateDeserializer;
import pw.react.backend.utils.JsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    @Column(name = "persons")
    private int persons;

    @Column(name = "idFlat")
    private long idFlat;

    @Column(name = "flatName")
    private String flatName;

    //@OneToOne(mappedBy = "reservation")
    //private Flat flat;
    //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@ManyToOne
    //@JoinColumn(name="flat_id")
    //private Flat flat;

    @JsonBackReference
    @ManyToOne
    private Flat flat;

    public Reservation () {}
    public Reservation (String CustomerName, LocalDateTime StartDateTime, LocalDateTime EndDateTime, int Price, int persons, long FlatId, String FlatName)
    {
        this.customerName = CustomerName;
        this.startDateTime = StartDateTime;
        this.endDateTime = EndDateTime;
        this.price = Price;
        this.persons = persons;
        this.idFlat = FlatId;
        this.flatName = FlatName;
    }

    public static Reservation valueOf(ReservationDTO reservationDTO)
    {
        return new Reservation(reservationDTO.CustomerName,
                reservationDTO.StartDateTime,
                reservationDTO.EndDateTime,
                reservationDTO.Price,
                reservationDTO.Persons,
                reservationDTO.FlatId,
                "EMPTY"
        );
    }

    // Getters and Setters should be provided below

}
