package org.lubrimax.msvc_usuarios.services;

import org.lubrimax.msvc_usuarios.models.entity.Rol;
import org.lubrimax.msvc_usuarios.models.entity.Usuario;
import org.lubrimax.msvc_usuarios.repositories.RolRepository;
import org.lubrimax.msvc_usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;

    @Autowired private RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario asignarRolAUsuario(Long usuarioId, Long rolId) {

        Usuario usuario =
                usuarioRepository
                        .findById(usuarioId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Rol rol =
                rolRepository
                        .findById(rolId)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setRolId(rol.getId());

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario cambiarEstado(Long usuarioId, String nuevoEstado) {
        Usuario usuario =
                usuarioRepository
                        .findById(usuarioId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.cambiarEstado(nuevoEstado);

        return usuarioRepository.save(usuario);
    }
}
