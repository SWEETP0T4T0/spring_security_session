package com.board.jwt.service;

import com.board.jwt.controller.AuthorPostForm;
import com.board.jwt.domain.Author;
import com.board.jwt.repository.AuthorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorService{

//    외부 접근 불가능
    private final AuthorRepository repository;
    private final PasswordEncoder passwordEncoder;


//    생성자
    public AuthorService(AuthorRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(Author author){
        repository.save(author);
        author.encodePassword(passwordEncoder);
    }

    public void login(AuthorPostForm authorPostForm) {
        Author author = repository.findByEmail(authorPostForm.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(authorPostForm.getPassword(), author.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 맞지 않습니다.");
        }
    }


    //    SpringDataJpaRepository일경우
    public Optional<Author> findById(Long memberId){
        return repository.findById(memberId);
    }


}
