package pw.react.backend.model;

import lombok.Getter;
import lombok.Setter;
import pw.react.backend.service.FlatService;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationDTO{

    public String CustomerName;
    public LocalDateTime StartDateTime;
    public LocalDateTime EndDateTime;
    public int Price;
    public int Persons;
    public Long FlatId;

    public ReservationDTO() {}
    public ReservationDTO(String customerName,
                          LocalDateTime startDateTime,
                          LocalDateTime endDateTime,
                          int price,
                          int persons,
                          Long flatId)
    {
        this.CustomerName = customerName;
        this.StartDateTime = startDateTime;
        this.EndDateTime = endDateTime;
        this.Price = price;
        this.Persons = persons;
        this.FlatId = flatId;
    }

    public Flat findFlatById(FlatService flatService)
    {
        Flat result = flatService.findFlatById(this.FlatId);
        return result;
    }

}