package org.example.moneylog.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class ResponseMeta {

    private final Pagination pagination;

    private ResponseMeta(Pagination pagination) {
        this.pagination = pagination;
    }

    public static ResponseMeta fromPage(Page<?> page) {
        Pagination pagination = new Pagination(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
        return new ResponseMeta(pagination);
    }

    @Getter
    public static class Pagination {
        private final int page;
        private final int size;
        private final long totalItems;
        private final int totalPages;
        private final boolean hasNext;
        private final boolean hasPrev;

        public Pagination(int page, int size, long totalItems, int totalPages, boolean hasNext, boolean hasPrev) {
            this.page = page;
            this.size = size;
            this.totalItems = totalItems;
            this.totalPages = totalPages;
            this.hasNext = hasNext;
            this.hasPrev = hasPrev;
        }
    }
}
