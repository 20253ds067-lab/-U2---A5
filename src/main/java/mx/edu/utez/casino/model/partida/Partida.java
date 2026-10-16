package mx.edu.utez.casino.model.partida;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.casino.model.enums.EstadoPartida;
import mx.edu.utez.casino.model.jugador.Jugador;

import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPartida estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @Column(nullable = false)
    private Double apuesta;
}
