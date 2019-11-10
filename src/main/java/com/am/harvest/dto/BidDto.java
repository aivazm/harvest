package com.am.harvest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class BidDto {
    @Size(max = 7, message = "Направление должно быть либо Покупка, либо Продажа")
    @NotEmpty(message = "Направление не может быть пустым")
    private String direction;
    @Size(max = 20, message = "Наименование товара не должно превышать 20 символов")
    @NotEmpty(message = "Наименование не может быть пустым")
    private String product;
    @Min(value = 1, message = "Количество должно быть больше нуля")
//    @NotEmpty(message = "Количество не может быть пустым")
    private Integer quantity;
    @Min(value = 1, message = "Цена должна быть больше нуля")
//    @NotEmpty(message = "Цена не может быть пустой")
    private Integer price;
}
