package com.example.snkrs.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveCategoryRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must be in kebab-case")
    private String slug;
}
