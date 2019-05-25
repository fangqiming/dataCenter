package com.gougu.entity.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cache<T> {

    private T data;

    private LocalDateTime time;
}
