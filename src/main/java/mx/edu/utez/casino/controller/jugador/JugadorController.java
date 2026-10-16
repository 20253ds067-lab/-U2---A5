package mx.edu.utez.casino.controller.jugador;

import jakarta.validation.Valid;
import mx.edu.utez.casino.controller.dto.CrearJugadorDTO;
import mx.edu.utez.casino.controller.dto.RecargarSaldoDTO;
import mx.edu.utez.casino.model.jugador.Jugador;
import mx.edu.utez.casino.service.jugador.JugadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jugador")
@CrossOrigin({"*"})
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public ResponseEntity<List<Jugador>> obtenerTodos(){
        return ResponseEntity
                .status(200)
                .body(
                  this.jugadorService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jugador> obtenerPorId(@PathVariable Long id){
        return ResponseEntity
                .status(200)
                .body(this.jugadorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Jugador> crear(@RequestBody @Valid CrearJugadorDTO payload){
        return ResponseEntity.status(201).body(this.jugadorService.crear(payload));
    }

    @PostMapping({"/recargarSaldo", "/recargar-saldo"})
    public ResponseEntity<Jugador> recargarSaldo(@RequestBody @Valid RecargarSaldoDTO payload){
        return ResponseEntity.status(200).body(this.jugadorService.recargarSaldo(payload));
    }
}
