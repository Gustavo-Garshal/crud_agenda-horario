package com.gustavo.agenda_horario.services;

import com.gustavo.agenda_horario.infrastructure.entity.Agendamento;
import com.gustavo.agenda_horario.infrastructure.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    private void validaSlotHorario(LocalDateTime dataHora) {
        int minuto = dataHora.getMinute();

        if(minuto != 0 && minuto != 30){
            throw new RuntimeException("Agendamento permitido apenas a cada 30 minutos");
        }

        LocalTime hora = dataHora.toLocalTime();
        LocalTime primeiroHorario = LocalTime.of(7, 30);
        LocalTime ultimoHorario = LocalTime.of(17, 30);

        if (hora.isBefore(primeiroHorario) || hora.isAfter(ultimoHorario)) {
            throw new RuntimeException("Agendamento fora do expediente");
        }
    }

    public Agendamento salvar(Agendamento agendamento) {
        LocalDateTime dataHora = agendamento.getDataHora();

        validaSlotHorario(dataHora);

        if (agendamentoRepository.existsByServicoAndDataHora(agendamento.getServico(), dataHora)) {
            throw new RuntimeException("Horário já ocupado, tente outro horário");
        }

        return agendamentoRepository.save(agendamento);
    }

    public void deletar(LocalDateTime dataHora, String cliente) {
        agendamentoRepository.deleteByDataHoraAndCliente(dataHora, cliente);
    }

    public List<Agendamento> buscarDia(LocalDate data) {
        LocalDateTime primeiraHora = data.atTime(7,0,0);
        LocalDateTime ultimaHora = data.atTime(17, 30, 0);

        return agendamentoRepository.findByDataHoraBetween(primeiraHora, ultimaHora);
    }

    public Agendamento alterar(Agendamento agendamentoNew, LocalDateTime dataHora, String cliente) {
        Agendamento agendamentoOld = agendamentoRepository.findByDataHoraAndCliente(dataHora, cliente);
        
        if (Objects.isNull(agendamentoOld)) {
            throw new RuntimeException("Agendamento não encontrado");
        }

        if (agendamentoNew.getDataHora() != null) {
            LocalDateTime novoHorario = agendamentoNew.getDataHora();

            validaSlotHorario(novoHorario);
            boolean ocupado = agendamentoRepository.existsByServicoAndDataHora(agendamentoNew.getServico(), novoHorario);

            if (ocupado) {
                throw new RuntimeException("Horario já ocupado, tente outro horario");
            }else {
                agendamentoOld.setDataHora(novoHorario);
            }
        }

        if (agendamentoNew.getCliente() != null) {agendamentoOld.setCliente(agendamentoNew.getCliente());}

        if (agendamentoNew.getColaborador() != null) {agendamentoOld.setColaborador(agendamentoNew.getColaborador());}

        if (agendamentoNew.getServico() != null) {agendamentoOld.setServico(agendamentoNew.getServico());}

        return agendamentoRepository.save(agendamentoOld);
    }
}
