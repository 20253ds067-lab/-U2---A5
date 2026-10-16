package mx.edu.utez.casino.service.jugador;

import mx.edu.utez.casino.controller.dto.CrearJugadorDTO;
import mx.edu.utez.casino.controller.dto.RecargarSaldoDTO;
import mx.edu.utez.casino.exception.customExceptions.RecursoNoEncontradoException;
import mx.edu.utez.casino.model.jugador.Jugador;
import mx.edu.utez.casino.model.repository.jugador.JugadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public List<Jugador> obtenerTodos() {
        return jugadorRepository.findAll();
    }

    public Jugador obtenerPorId(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El jugador no existe"));
    }

    @Transactional
    public Jugador crear(CrearJugadorDTO payload) {
        Jugador jugador = new Jugador();
        jugador.setNombre(payload.getNombre());
        jugador.setSaldo(payload.getSaldo());
        jugador.setActivo(true);
        return jugadorRepository.save(jugador);
    }

    @Transactional
    public Jugador recargarSaldo(RecargarSaldoDTO payload) {
        Jugador jugador = jugadorRepository.findById(payload.getJugadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El jugador no existe"));
        jugador.setSaldo(jugador.getSaldo() + payload.getMonto());
        return jugadorRepository.save(jugador);
    }
}
