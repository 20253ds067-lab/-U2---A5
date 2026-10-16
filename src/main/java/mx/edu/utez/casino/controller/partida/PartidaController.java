package mx.edu.utez.casino.controller.partida;

import jakarta.validation.Valid;
import mx.edu.utez.casino.controller.dto.FinalizarPartidaDTO;
import mx.edu.utez.casino.controller.dto.IniciarPartidaDTO;
import mx.edu.utez.casino.controller.dto.RealizarTiroDTO;
import mx.edu.utez.casino.model.partida.Partida;
import mx.edu.utez.casino.model.tiro.Tiro;
import mx.edu.utez.casino.service.partida.PartidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partida")
@CrossOrigin({"*"})
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping({"/iniciarPartida", "/iniciar-partida"})
    public ResponseEntity<Partida> iniciarPartida(@RequestBody @Valid IniciarPartidaDTO payload){
        return ResponseEntity.status(201).body(this.partidaService.iniciarPartida(payload));
    }

    @PostMapping({"/realizarTiro", "/realizar-tiro"})
    public ResponseEntity<Tiro> realizarTiro(@RequestBody @Valid RealizarTiroDTO payload){
        return ResponseEntity.status(200).body(this.partidaService.realizarTiro(payload));
    }

    @PostMapping({"/finalizarPartidaManual", "/finalizar-partida-manual"})
    public ResponseEntity<Partida> finalizarPartidaManual(@RequestBody @Valid FinalizarPartidaDTO payload){
        return ResponseEntity.status(200).body(this.partidaService.finalizarPartidaManual(payload));
    }

    @GetMapping({"/obtenerHistorialPartida/{jugadorId}", "/historial/{jugadorId}"})
    public ResponseEntity<List<Partida>> obtenerHistorialPartida(@PathVariable Long jugadorId){
        return ResponseEntity.status(200).body(this.partidaService.obtenerHistorialPartida(jugadorId));
    }
}
