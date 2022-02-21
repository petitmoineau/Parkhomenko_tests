package com.example.library.controller;

import com.example.library.model.BookDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public String saveBook(String findStr, BookDto bookDto, Model model)
    {
        bookList.add(bookDto);

        return "book-create";
    }

    @RequestMapping(value="/create", method= RequestMethod.POST, params="showAll")
    public String redirect(BookDto bookDto, Model model)
    {
        model.addAttribute("bookList", bookList);

        return "book-create";
    }

    @RequestMapping(value="/create", method= RequestMethod.POST, params="find")
    public String find(BookDto bookDto, @RequestParam String findStr, Model model)
    {
        List<BookDto> result = bookList.stream()
                .filter(item -> item.getTitle().contains(findStr) || item.getIsbn().contains(findStr))
                .collect(Collectors.toList());

        model.addAttribute("foundBookList", result);

        return "book-create";
    }
}
