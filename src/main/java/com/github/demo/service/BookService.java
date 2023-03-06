package com.github.demo.service;

import com.github.demo.model.Book;

import java.util.List;

public class BookService {

    private List<Book> books;

    public BookService() throws BookServiceException {
        this.books = BookUtils.getSampleBooks();
    }

    public List<Book> getBooks() throws BookServiceException {
        return books;
    }

}