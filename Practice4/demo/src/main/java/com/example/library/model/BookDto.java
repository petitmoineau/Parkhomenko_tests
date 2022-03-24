package com.example.library.model;

import lombok.Data;

@Data
public class BookDto {

    private String booktitle;
    private String isbn;
    private String author;

    @Override
    public String toString()
    {
        return "\"" + this.booktitle + "\" " + this.isbn + " " + this.author;
    }
}
