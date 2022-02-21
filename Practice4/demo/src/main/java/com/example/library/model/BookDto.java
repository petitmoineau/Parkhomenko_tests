package com.example.library.model;

import lombok.Data;

@Data
public class BookDto {

    private String title;
    private String isbn;
    private String author;

    @Override
    public String toString()
    {
        return "\"" + this.title + "\" " + this.isbn + " " + this.author;
    }
}
