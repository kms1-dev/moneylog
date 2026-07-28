package org.example.moneylog.dto;

import lombok.Getter;

@Getter
public class CategoryTotalResponse {
    private final String categoryName;
    private final Long total;

    public CategoryTotalResponse(String categoryName, Long total) {
        this.categoryName = categoryName;
        this.total = total;
    }
}
