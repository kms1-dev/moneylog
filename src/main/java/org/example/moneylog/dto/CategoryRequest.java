package org.example.moneylog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.moneylog.domain.CategoryType;

@Getter
@NoArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;

    @NotNull(message = "카테고리 타입은 필수입니다.")
    private CategoryType type;
}
