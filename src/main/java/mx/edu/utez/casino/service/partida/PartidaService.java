package mx.edu.utez.casino.service.partida;

import mx.edu.utez.casino.controller.dto.FinalizarPartidaDTO;
import mx.edu.utez.casino.controller.dto.IniciarPartidaDTO;
import mx.edu.utez.casino.controller.dto.RealizarTiroDTO;
import mx.edu.utez.casino.exception.customExceptions.BadRequestException;
import mx.edu.utez.casino.exception.customExceptions.RecursoNoEncontradoException;
import mx.edu.utez.casino.model.enums.EstadoPartida;
import mx.edu.utez.casino.model.jugador.Jugador;
import mx.edu.utez.casino.model.partida.Partida;
import mx.edu.utez.casino.model.repository.jugador.JugadorRepository;
import mx.edu.utez.casino.model.repository.partida.PartidaRepository;
import mx.edu.utez.casino.model.repository.tiro.TiroRepository;
import mx.edu.utez.casino.model.tiro.Tiro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JugadorRepository jugadorRepository;
    private final TiroRepository tiroRepository;
    private final Random random = new Random();

    public PartidaService(PartidaRepository partidaRepository, JugadorRepository jugadorRepository, TiroRepository tiroRepository) {
        this.partidaRepository = partidaRepository;
        this.jugadorRepository = jugadorRepository;
        this.tiroRepository = tiroRepository;
    }

    @Transactional
    public Partida iniciarPartida(IniciarPartidaDTO payload) {
        Jugador jugador = jugadorRepository.findById(payload.getJugadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El jugador no existe"));

        if (!jugador.getActivo()) {
            throw new BadRequestException("El jugador no está activo");
        }
        if (jugador.getSaldo() < payload.getApuesta()) {
            throw new BadRequestException("El jugador no tiene saldo suficiente");
        }

        jugador.setSaldo(jugador.getSaldo() - payload.getApuesta());
        jugadorRepository.save(jugador);

        Partida partida = new Partida();
        partida.setFecha(LocalDateTime.now());
        partida.setEstado(EstadoPartida.EN_JUEGO);
        partida.setJugador(jugador);
        partida.setApuesta(payload.getApuesta());
        return partidaRepository.save(partida);
    }

    @Transactional
    public Tiro realizarTiro(RealizarTiroDTO payload) {
        Partida partida = partidaRepository.findById(payload.getPartidaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("La partida no existe"));

        if (partida.getEstado() != EstadoPartida.EN_JUEGO) {
            throw new BadRequestException("La partida no está en juego");
        }

        int valorDado1 = random.nextInt(6) + 1;
        int valorDado2 = random.nextInt(6) + 1;
        int suma = valorDado1 + valorDado2;
        boolean esGanador = false;

        if (suma == 7 || suma == 11) {
            esGanador = true;
            partida.setEstado(EstadoPartida.FINALIZADA);
            Jugador jugador = partida.getJugador();
            jugador.setSaldo(jugador.getSaldo() + (partida.getApuesta() * 2));
            jugadorRepository.save(jugador);
            partidaRepository.save(partida);
        } else if (suma == 2 || suma == 3 || suma == 12) {
            partida.setEstado(EstadoPartida.FINALIZADA);
            partidaRepository.save(partida);
        }

        Tiro tiro = new Tiro();
        tiro.setPartida(partida);
        tiro.setValorDado1(valorDado1);
        tiro.setValorDado2(valorDado2);
        tiro.setSuma(suma);
        tiro.setEsGanador(esGanador);
        return tiroRepository.save(tiro);
    }

    @Transactional
    public Partida finalizarPartidaManual(FinalizarPartidaDTO payload) {
        Partida partida = partidaRepository.findById(payload.getPartidaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("La partida no existe"));

        if (partida.getEstado() == EstadoPartida.FINALIZADA) {
            throw new BadRequestException("La partida ya está finalizada");
        }

        partida.setEstado(EstadoPartida.FINALIZADA);
        return partidaRepository.save(partida);
    }

    public List<Partida> obtenerHistorialPartida(Long jugadorId) {
        if (!jugadorRepository.existsById(jugadorId)) {
            throw new RecursoNoEncontradoException("El jugador no existe");
        }
        return partidaRepository.findByJugadorId(jugadorId);
    }
}
