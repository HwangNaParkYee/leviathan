package com.example.leviathan.service;

import com.example.leviathan.domain.Article;
import com.example.leviathan.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

}
