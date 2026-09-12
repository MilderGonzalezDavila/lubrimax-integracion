package org.lubrimax.msvc_agenda_tecnico.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lubrimax.msvc_agenda_tecnico.clients.UsuarioClienteRest;
import org.lubrimax.msvc_agenda_tecnico.models.PeriodoDeTrabajo;
import org.lubrimax.msvc_agenda_tecnico.models.entity.AgendaDelTecnico;
import org.lubrimax.msvc_agenda_tecnico.repositories.AgendaDelTecnicoRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendaDelTecnicoServiceImplTest {

    @Mock
    private AgendaDelTecnicoRepository repository;

    @Mock
    private UsuarioClienteRest usuarioCliente;

    @InjectMocks
    private AgendaDelTecnicoServiceImpl service;

    @Test
    void asignaTrabajoCuandoElUsuarioEsTecnicoActivo() {
        when(usuarioCliente.buscarUsuario(1L))
                .thenReturn(new UsuarioClienteRest.UsuarioResponse(1L, "TECNICO", true));
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(repository.save(any(AgendaDelTecnico.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 11, 8, 0);
        var asignacion = service.asignar(1L, 10L, new PeriodoDeTrabajo(inicio, inicio.plusHours(1)));

        assertEquals(10L, asignacion.getOrdenId());
    }
}
