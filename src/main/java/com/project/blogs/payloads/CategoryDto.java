package com.project.blogs.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private Integer categoryId;
	
	@NotEmpty
	@Size(min = 4, max= 20, message = "Size must be more than 4 characters but less than 20 characters")
	private String categoryTitle;
	
	@NotEmpty
	@Size(min = 10, max = 40,message = "Size must be more than 4 characters and less than 30 characters")
	private String categoryDescription;

}
