package com.example.library.controller;

import com.example.library.model.BookDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/book")
public class BookController {

    ArrayList<BookDto> bookList = new ArrayList<>();

    @GetMapping("/create")
    public String bookFormGet(Model model)
    {
        model.addAttribute("bookDto", new BookDto());
        return "book-create";
    }

    @RequestMapping(value="/create", method=RequestMethod.POST, params="submit")
    public String saveBook(BookDto bookDto, Model model)
    {
        model.addAttribute("newBook", bookDto);
        bookList.add(bookDto);

        return "book-create";
    }

    @RequestMapping(value="/create", method= RequestMethod.POST)//, params="redirect"
    public String redirect()
    {
        return "redirect:/book/added";
    }


    @GetMapping("/added")
    public String bookAdded(Model model)
    {
        model.addAttribute("bookList", bookList);
        return "show-book";
    }
}
