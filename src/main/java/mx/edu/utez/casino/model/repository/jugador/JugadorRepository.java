package mx.edu.utez.casino.model.repository.jugador;

import mx.edu.utez.casino.model.jugador.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
}
