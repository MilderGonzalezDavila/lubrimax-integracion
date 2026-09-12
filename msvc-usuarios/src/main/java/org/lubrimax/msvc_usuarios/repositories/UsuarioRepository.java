package org.lubrimax.msvc_usuarios.repositories;

import org.lubrimax.msvc_usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {}
