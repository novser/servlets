package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// Stub
@Repository
public class PostRepository {
    private static int currentId = 1;
    private static ConcurrentMap<Long, Post> dataBase = new ConcurrentHashMap<>();

    public List<Post> all() {
        return dataBase.values().stream().toList();
    }

    public Optional<Post> getById(long id) {
        Post post = dataBase.get(id);
        if (id <= 0 || post == null) {
            return Optional.empty();
        }
        return Optional.of(post);
    }

    public Post save(Post post) {
        long id = post.getId();
        Post currentPost = dataBase.get(id);
        if (id == 0) {
            long newId = currentId++;
            Post newPost = new Post(newId, post.getContent());
            dataBase.put(newId, newPost);
            return newPost;
        } else if (id < 0 || currentPost == null) {
            throw new NotFoundException();
        } else {
            currentPost.setContent(post.getContent());
            return currentPost;
        }
    }

    public void removeById(long id) {
        Post currentPost = dataBase.get(id);
        if (currentPost == null) {
            throw new NotFoundException();
        }
        dataBase.remove(id);
    }
}
