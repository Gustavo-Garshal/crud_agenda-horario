package com.gustavo.agenda_horario.controller;

import com.gustavo.agenda_horario.infrastructure.entity.Agendamento;
import com.gustavo.agenda_horario.services.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<Agendamento> salvar(@RequestBody Agendamento agendamento){
        return ResponseEntity.accepted().body(agendamentoService.salvar(agendamento));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestParam LocalDateTime dataHora, @RequestParam String cliente){
        agendamentoService.deletar(dataHora, cliente);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Agendamento> buscaDia(@RequestParam LocalDate data){
        return ResponseEntity.ok().body(agendamentoService.buscarDia(data));
    }

    @PutMapping
    public ResponseEntity<Agendamento> alterar(
            @RequestBody Agendamento agendamento, @RequestParam LocalDateTime dataHora, @RequestParam String cliente){
        return ResponseEntity.accepted().body(agendamentoService.alterar(agendamento, dataHora, cliente));
    }
}
