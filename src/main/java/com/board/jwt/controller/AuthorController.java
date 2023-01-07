package com.board.jwt.controller;

import com.board.jwt.domain.Author;
import com.board.jwt.domain.Role;
import com.board.jwt.service.AuthorService;
import com.board.jwt.service.JwtTokenProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@Controller
public class AuthorController {
    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthorController(AuthorService authorService, JwtTokenProvider jwtTokenProvider) {
        this.authorService = authorService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    회원가입
    @PostMapping("/authors/new")
    public String create(AuthorPostForm authorPostForm){
        Author author = new Author();
        author.setName(authorPostForm.getName());
        author.setEmail(authorPostForm.getEmail());
        author.setPassword(authorPostForm.getPassword());
        author.setCreateDate(LocalDateTime.now());
        if(authorPostForm.getRole().equals("user")){
            author.setRole(Role.USER);
        }else{
            author.setRole(Role.ADMIN);
        }
        authorService.create(author);
        return "ok";
    }

//    login로직
    @PostMapping("/doLogin")
    @ResponseBody
    public HashMap<String, Object> doLogin(@RequestBody AuthorPostForm authorPostForm) {
        authorService.login(authorPostForm);
        String jwt = jwtTokenProvider.createToken(authorPostForm.getEmail());
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("token", jwt);
        return map;
    }

//    로그인후 회원정보 조회
    @GetMapping("authors/api/findById/{authorId}")
    @ResponseBody
    public Author findById(@PathVariable() Long authorId){
        return authorService.findById(authorId).orElse(null);
    }

    @GetMapping("/token")
    public String tokenTest(Model model){
        return "/token";
    }

}
