package org.lubrimax.msvc_usuarios.services;

import org.lubrimax.msvc_usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listar();

    Optional<Usuario> buscarPorId(Long id);

    Usuario guardar(Usuario usuario);

    void eliminar(Long id);

    Usuario asignarRolAUsuario(Long usuarioId, Long rolId);

    Usuario cambiarEstado(Long usuarioId, String nuevoEstado);
}
