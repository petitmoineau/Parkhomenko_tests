package com.example.library.controller;

import com.example.library.model.AjaxResponseBody;
import com.example.library.model.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/book")
public class BookController {

    ArrayList<BookDto> bookList = new ArrayList<>();
    AjaxResponseBody showResult = new AjaxResponseBody();

    @GetMapping("/create")
    public String bookFormGet(Model model)
    {
        model.addAttribute("bookDto", new BookDto());
        return "book-create";
    }

    @PostMapping("/show")
    public ResponseEntity<?> rememberBookViaAjax(
            @RequestBody BookDto bookDto, Errors errors) {

        if (errors.hasErrors()) {

            showResult.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(showResult);

        }

        else
        {
            bookList.add(bookDto);
            showResult.setMsg("success");
            showResult.addBook(bookDto);

            return ResponseEntity.ok(showResult);
        }

    }

    @PostMapping("/find")
    public ResponseEntity<?> findBookViaAjax(
            @RequestBody String findStr, Errors errors) {


        AjaxResponseBody findResult = new AjaxResponseBody();
        if (errors.hasErrors()) {

            findResult.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(findResult);

        }

        else
        {
            findResult.setMsg("success");
            List<BookDto> result = bookList.stream()
                    .filter(item -> item.getBooktitle().contains(findStr) || item.getIsbn().contains(findStr))
                    .collect(Collectors.toList());
            findResult.setBookList(new ArrayList<BookDto>(result));//?
            return ResponseEntity.ok(findResult);
        }
    }
}
