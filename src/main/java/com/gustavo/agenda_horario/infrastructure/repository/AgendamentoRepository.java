package com.gustavo.agenda_horario.infrastructure.repository;

import com.gustavo.agenda_horario.infrastructure.entity.Agendamento;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Agendamento findByServicoAndDataHoraBetween(
            String servico, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);

    @Transactional
    void deleteByDataHoraAndCliente(LocalDateTime dataHora, String cliente);

    Agendamento findByDataHoraBetween(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);

    Agendamento findByDataHoraAndCliente(LocalDateTime dataHora, String cliente);
}