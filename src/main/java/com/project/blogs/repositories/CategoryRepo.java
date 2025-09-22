package com.project.blogs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.blogs.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
