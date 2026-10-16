package mx.edu.utez.casino.service;

import mx.edu.utez.casino.controller.dto.CrearJugadorDTO;
import mx.edu.utez.casino.controller.dto.RecargarSaldoDTO;
import mx.edu.utez.casino.exception.customExceptions.RecursoNoEncontradoException;
import mx.edu.utez.casino.model.jugador.Jugador;
import mx.edu.utez.casino.model.repository.jugador.JugadorRepository;
import mx.edu.utez.casino.service.jugador.JugadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JugadorServiceTest {

    @Mock
    private JugadorRepository jugadorRepository;

    @InjectMocks
    private JugadorService jugadorService;

    private Jugador jugador;

    @BeforeEach
    void setUp() {
        jugador = new Jugador(1L, "Carlos", 50.0, true);
    }

    @Test
    void crearJugadorExitoso() {
        CrearJugadorDTO dto = new CrearJugadorDTO("Carlos", 50.0);
        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> {
            Jugador j = invocation.getArgument(0);
            j.setId(1L);
            return j;
        });

        Jugador resultado = jugadorService.crear(dto);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        assertEquals(50.0, resultado.getSaldo());
        assertTrue(resultado.getActivo());
    }

    @Test
    void recargarSaldoExitoso() {
        RecargarSaldoDTO dto = new RecargarSaldoDTO(1L, 100.0);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(jugadorRepository.save(any(Jugador.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Jugador resultado = jugadorService.recargarSaldo(dto);

        assertEquals(150.0, resultado.getSaldo());
        verify(jugadorRepository).save(jugador);
    }

    @Test
    void recargarSaldoJugadorNoExiste() {
        RecargarSaldoDTO dto = new RecargarSaldoDTO(99L, 100.0);
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> jugadorService.recargarSaldo(dto));
    }

    @Test
    void obtenerTodos() {
        when(jugadorRepository.findAll()).thenReturn(List.of(jugador));

        List<Jugador> resultado = jugadorService.obtenerTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerPorIdExitoso() {
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));

        Jugador resultado = jugadorService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
    }

    @Test
    void obtenerPorIdNoExiste() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> jugadorService.obtenerPorId(99L));
    }
}
