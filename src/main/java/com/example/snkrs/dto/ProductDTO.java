package com.example.snkrs.dto;

import com.example.snkrs.model.Category;
import com.example.snkrs.model.Size;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

@Data
public class ProductDTO implements Serializable {
    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must be in kebab-case")
    private String slug;

    @NotBlank
    private String description;

    @Nullable
    private MultipartFile image;

    @NotNull
    private List<String> categories;

    private List<Category> categoryList;

    @NotNull
    private String sizes;

    private boolean publish = true;
}
