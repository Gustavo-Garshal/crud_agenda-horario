package com.gustavo.agenda_horario.infrastructure.repository;

import com.gustavo.agenda_horario.infrastructure.entity.Agendamento;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByServicoAndDataHora(
            String servico, LocalDateTime dataHora);

    @Transactional
    void deleteByDataHoraAndCliente(LocalDateTime dataHora, String cliente);

    List<Agendamento> findByDataHoraBetween(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);

    Agendamento findByDataHoraAndCliente(LocalDateTime dataHora, String cliente);
}