package com.crowoj.api.service;

/**
 * @author crow
 * @create 2021/10/31 2:00
 * @description
 */
public interface BaseService<T> {
    <S extends T> Iterable<S> batchSave(Iterable<S> var1);
    <S extends T> Iterable<S> batchUpdate(Iterable<S> var1);
}
