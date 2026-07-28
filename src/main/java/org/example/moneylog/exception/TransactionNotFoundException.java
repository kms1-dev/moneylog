package org.example.moneylog.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
        super("거래내역을 찾을 수 없습니다.");
    }
}
