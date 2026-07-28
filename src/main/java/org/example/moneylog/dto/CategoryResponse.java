package org.example.moneylog.dto;

import lombok.Getter;
import org.example.moneylog.domain.Category;
import org.example.moneylog.domain.CategoryType;

@Getter
public class CategoryResponse {

    private final Long id;
    private final String name;
    private final CategoryType type;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.type = category.getType();
    }
}
