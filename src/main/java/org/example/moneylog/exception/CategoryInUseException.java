package org.example.moneylog.exception;

public class CategoryInUseException extends RuntimeException {
    public CategoryInUseException() {
        super("이 카테고리를 사용하는 거래내역이 있어서 삭제할 수 없습니다.");
    }
}
