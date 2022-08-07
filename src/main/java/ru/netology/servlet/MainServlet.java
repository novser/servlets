package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (path.contains("/api/posts")) {
                actionApiPosts(path, method, req, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void actionApiPosts(String path, String method, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(path.matches("/api/posts/\\d+")) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            if (method.equals("GET")) {
                controller.getById(id, resp);
            } else if (method.equals("DELETE")){
                controller.removeById(id, resp);
            }
        } else {
            if (method.equals("GET")) {
                controller.all(resp);
            } else if (method.equals("POST")){
                controller.save(req.getReader(), resp);
            }
        }
    }
}

