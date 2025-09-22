package com.project.blogs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.blogs.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Integer>{

}
