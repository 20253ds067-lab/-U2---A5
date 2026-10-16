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
public class FinalizarPartidaDTO {

    @NotNull(message = "El id de la partida es obligatorio")
    @Positive(message = "El id de la partida debe ser mayor a 0")
    private Long partidaId;
}
