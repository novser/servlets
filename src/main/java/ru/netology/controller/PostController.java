package ru.netology.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        final var data = service.all();
        response.getWriter().print(prepareResponse(response).toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        final var data = service.getById(id);
        response.getWriter().print(prepareResponse(response).toJson(data));
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        final var gson = prepareResponse(response);
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        response.getWriter().print(gson.toJson(data));
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        service.removeById(id);
        prepareResponse(response);
        response.getWriter().print("Пост удален");
    }

    private Gson prepareResponse(HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
        return new Gson();
    }
}
