package com.board.jwt.service;
import com.board.jwt.domain.Author;
import com.board.jwt.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class AuthorUserService implements UserDetailsService {

    //    외부 접근 불가능
    private final AuthorRepository repository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Author author = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 Email 입니다."));
        return new User(author.getEmail(), author.getPassword(), Arrays.asList(new SimpleGrantedAuthority(author.getRole().toString())));
    }
}
