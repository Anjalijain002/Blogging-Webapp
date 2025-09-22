package com.project.blogs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.blogs.entities.Category;
import com.project.blogs.entities.Post;
import com.project.blogs.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer> {

	List<Post> findByUser(User user);
	
	List<Post> findByCategory(Category category);
	
	List<Post> findByPostTitleContaining(String title);
	
	
}
