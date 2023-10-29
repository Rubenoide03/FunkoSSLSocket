package models;

import lombok.Builder;
import lombok.Data;
import utils.CurrencyDateUtilFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record MyFunko(UUID cod, String nombre, ModeloF modelo, double precio, LocalDate fecha, LocalDateTime created_at,
                      LocalDateTime updated_at
) {


    @Override
    public String toString() {
        return "MyFunko{" +
                "cod=" + cod +
                ", nombre='" + nombre + '\'' +
                ", modelo=" + modelo +
                ", precio=" + CurrencyDateUtilFormatter.formatLocalCurrency(precio) +
                ", fecha=" + CurrencyDateUtilFormatter.formatLocalDate(fecha)  +
                ", created_at=" + CurrencyDateUtilFormatter.formatLocalDateTime(created_at)  +
                ", updated_at=" + CurrencyDateUtilFormatter.formatLocalDateTime(updated_at) +
                '}';
    }
}
