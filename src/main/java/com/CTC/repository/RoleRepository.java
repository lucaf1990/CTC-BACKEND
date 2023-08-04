package com.CTC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CTC.entity.ERole;
import com.CTC.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    

	Role findByRoleName(ERole roles);

	Role findByRoleName(Role roles);

}
