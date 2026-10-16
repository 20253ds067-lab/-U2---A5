package mx.edu.utez.casino.service;

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
import mx.edu.utez.casino.service.partida.PartidaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private JugadorRepository jugadorRepository;

    @Mock
    private TiroRepository tiroRepository;

    @InjectMocks
    private PartidaService partidaService;

    private Jugador jugador;
    private Partida partida;

    @BeforeEach
    void setUp() {
        jugador = new Jugador(1L, "Carlos", 100.0, true);
        partida = new Partida(1L, LocalDateTime.now(), EstadoPartida.EN_JUEGO, jugador, 20.0);
    }

    @Test
    void iniciarPartidaExitoso() {
        IniciarPartidaDTO dto = new IniciarPartidaDTO(1L, 20.0);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Partida resultado = partidaService.iniciarPartida(dto);

        assertNotNull(resultado);
        assertEquals(EstadoPartida.EN_JUEGO, resultado.getEstado());
        assertEquals(80.0, jugador.getSaldo());
        verify(jugadorRepository).save(jugador);
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void iniciarPartidaJugadorNoExiste() {
        IniciarPartidaDTO dto = new IniciarPartidaDTO(99L, 20.0);
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> partidaService.iniciarPartida(dto));
    }

    @Test
    void iniciarPartidaJugadorInactivo() {
        jugador.setActivo(false);
        IniciarPartidaDTO dto = new IniciarPartidaDTO(1L, 20.0);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));

        assertThrows(BadRequestException.class, () -> partidaService.iniciarPartida(dto));
    }

    @Test
    void iniciarPartidaSaldoInsuficiente() {
        IniciarPartidaDTO dto = new IniciarPartidaDTO(1L, 200.0);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));

        assertThrows(BadRequestException.class, () -> partidaService.iniciarPartida(dto));
    }

    @Test
    void realizarTiroPartidaNoExiste() {
        RealizarTiroDTO dto = new RealizarTiroDTO(99L);
        when(partidaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> partidaService.realizarTiro(dto));
    }

    @Test
    void realizarTiroPartidaFinalizada() {
        partida.setEstado(EstadoPartida.FINALIZADA);
        RealizarTiroDTO dto = new RealizarTiroDTO(1L);
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        assertThrows(BadRequestException.class, () -> partidaService.realizarTiro(dto));
    }

    @Test
    void realizarTiroValido() {
        RealizarTiroDTO dto = new RealizarTiroDTO(1L);
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(tiroRepository.save(any(Tiro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tiro resultado = partidaService.realizarTiro(dto);

        assertNotNull(resultado);
        assertTrue(resultado.getValorDado1() >= 1 && resultado.getValorDado1() <= 6);
        assertTrue(resultado.getValorDado2() >= 1 && resultado.getValorDado2() <= 6);
        assertEquals(resultado.getValorDado1() + resultado.getValorDado2(), resultado.getSuma());
        verify(tiroRepository).save(any(Tiro.class));
    }

    @Test
    void finalizarPartidaManualExitoso() {
        FinalizarPartidaDTO dto = new FinalizarPartidaDTO(1L);
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any(Partida.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Partida resultado = partidaService.finalizarPartidaManual(dto);

        assertEquals(EstadoPartida.FINALIZADA, resultado.getEstado());
        verify(partidaRepository).save(partida);
    }

    @Test
    void finalizarPartidaManualYaFinalizada() {
        partida.setEstado(EstadoPartida.FINALIZADA);
        FinalizarPartidaDTO dto = new FinalizarPartidaDTO(1L);
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        assertThrows(BadRequestException.class, () -> partidaService.finalizarPartidaManual(dto));
    }

    @Test
    void obtenerHistorialPartidaExitoso() {
        when(jugadorRepository.existsById(1L)).thenReturn(true);
        when(partidaRepository.findByJugadorId(1L)).thenReturn(List.of(partida));

        List<Partida> historial = partidaService.obtenerHistorialPartida(1L);

        assertEquals(1, historial.size());
        assertEquals(1L, historial.get(0).getId());
    }

    @Test
    void obtenerHistorialPartidaJugadorNoExiste() {
        when(jugadorRepository.existsById(99L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class, () -> partidaService.obtenerHistorialPartida(99L));
    }
}
