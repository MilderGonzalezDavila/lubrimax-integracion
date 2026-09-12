package org.lubrimax.msvc_usuarios.repositories;

import org.lubrimax.msvc_usuarios.models.entity.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends CrudRepository<Rol, Long> {}
