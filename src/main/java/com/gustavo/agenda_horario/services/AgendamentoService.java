package com.gustavo.agenda_horario.services;

import com.gustavo.agenda_horario.infrastructure.entity.Agendamento;
import com.gustavo.agenda_horario.infrastructure.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    public Agendamento salvar(Agendamento agendamento) {
        LocalDateTime horaAgendamento = agendamento.getDataHora();
        LocalDateTime horaFim = agendamento.getDataHora().plusMinutes(30);

        Agendamento agendados = agendamentoRepository.findByServicoAndDataHoraBetween(
                agendamento.getServico(), horaAgendamento, horaFim);

        if (Objects.isNull(agendados)) {
            return agendamentoRepository.save(agendamento);
        }
        else {
            throw new RuntimeException("Horário já ocupado, tente outro horário");
        }
    }

    public void deletar(LocalDateTime dataHora, String cliente) {
        agendamentoRepository.deleteByDataHoraAndCliente(dataHora, cliente);
    }

    public Agendamento buscarDia(LocalDate data) {
        LocalDateTime primeiraHora = data.atStartOfDay();
        LocalDateTime ultimaHora = data.atTime(23, 59, 59);

        return agendamentoRepository.findByDataHoraBetween(primeiraHora, ultimaHora);
    }

    public Agendamento alterar(Agendamento agendamentoNew, LocalDateTime dataHora, String cliente) {
        Agendamento agendamentoOld = agendamentoRepository.findByDataHoraAndCliente(dataHora, cliente);
        if (Objects.isNull(agendamentoOld)) {
            throw new RuntimeException("Agendamento não encontrado");
        }
        else  {
            agendamentoNew.setId(agendamentoOld.getId());
            return agendamentoRepository.save(agendamentoNew);
        }
    }
}
