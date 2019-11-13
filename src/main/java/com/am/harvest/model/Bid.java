package com.am.harvest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @NotBlank(message = "Direction cannot be empty")
    @Enumerated(EnumType.STRING)
    private Direction direction;
    //    @NotBlank(message = "Product cannot be empty")
    private String product;
    //    @NotBlank(message = "Quantity cannot be empty")
    private Integer quantity;
    //    @NotBlank(message = "Price cannot be empty")
    private Integer price;
    @Enumerated(EnumType.STRING)
    private BidState state;
    private LocalDateTime date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bid bid = (Bid) o;

        return id != null ? id.equals(bid.id) : bid.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
