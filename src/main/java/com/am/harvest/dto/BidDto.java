package com.am.harvest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class BidDto {
    @NotEmpty(message = "Направление не может быть пустым")
    @Pattern(message = "Направление должно быть либо Покупка, либо Продажа",
            regexp = "Покупка|Продажа")
    private String direction;
    @Size(max = 20, message = "Наименование товара не должно превышать 20 символов")
    @NotEmpty(message = "Наименование не может быть пустым")
    private String product;
    @NotNull(message = "Количество не может быть пустым")
    @Min(value = 1, message = "Количество должно быть от 1 до 999 999")
    @Max(value = 999999, message = "Количество должно быть от 1 до 999 999")
    @Digits(integer=5, fraction=0, message = "Не более 999 999")
    private Integer quantity;
    @NotNull(message = "Цена не может быть пустой")
    @Min(value = 1, message = "Цена должна быть от 1 до 999 999")
    @Max(value = 999999, message = "Цена должна быть от 1 до 999 999")
    @Digits(integer=5, fraction=0, message = "Не более 999 999")
    private BigDecimal price;

    @Override
    public String toString() {
        return "Заявка{" +
                "Направление='" + direction + '\'' +
                ", Товар='" + product + '\'' +
                ", Количество=" + quantity +
                ", Цена=" + price +
                '}';
    }
}
