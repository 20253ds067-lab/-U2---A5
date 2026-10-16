package mx.edu.utez.casino.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecargarSaldoDTO {

    @NotNull(message = "El id del jugador es obligatorio")
    @Positive(message = "El id del jugador debe ser mayor a 0")
    private Long jugadorId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto a recargar debe ser mayor a 0")
    private Double monto;
}
