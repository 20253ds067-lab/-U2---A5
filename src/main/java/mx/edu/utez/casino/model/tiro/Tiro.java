package mx.edu.utez.casino.model.tiro;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.casino.model.partida.Partida;

@Entity
@Table(name = "tiros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    @Column(nullable = false)
    private Integer valorDado1;

    @Column(nullable = false)
    private Integer valorDado2;

    @Column(nullable = false)
    private Integer suma;

    @Column(nullable = false)
    private Boolean esGanador;
}
