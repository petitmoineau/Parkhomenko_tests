package com.example.library.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AjaxResponseBody {

    ArrayList<BookDto> bookList = new ArrayList<>();
    String msg;


    public void addBook(BookDto bookDto) {
        bookList.add(bookDto);
    }

}
