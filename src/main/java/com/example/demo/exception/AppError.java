package com.example.demo.exception;

import lombok.Data;

import java.util.Date;

@Data
public class AppError { // то что будем кидать юзеру если данные были введенны неверно
    private int status;
    private String message; // сообщение, в котором описана ошибка (то есть причина, почему юзер видит эту ошибку)
    private Date timestamp;

    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
