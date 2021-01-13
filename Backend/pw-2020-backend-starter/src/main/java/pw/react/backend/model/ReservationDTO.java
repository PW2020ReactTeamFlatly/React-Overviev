package pw.react.backend.model;

import java.time.LocalDateTime;

public class ReservationDTO{
    public String CustomerName;
    public LocalDateTime StartDateTime;
    public LocalDateTime EndDateTime;
    public int Price;
    public int Sleeps;
    public long FlatId;

    public ReservationDTO() {}
    public ReservationDTO(String customerName,
                          LocalDateTime startDateTime,
                          LocalDateTime endDateTime,
                          int price,
                          int sleeps,
                          long flatId) {
        this.CustomerName = customerName;
        this.StartDateTime = startDateTime;
        this.EndDateTime = endDateTime;
        this.Price = price;
        this.Sleeps = sleeps;
        this.FlatId = flatId;
    }
}