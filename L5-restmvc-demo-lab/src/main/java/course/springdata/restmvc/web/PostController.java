package course.springdata.restmvc.web;

import course.springdata.restmvc.dao.PostRepository;
import course.springdata.restmvc.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostRepository postRepo;

    @GetMapping
    public Collection<Post> getAllPosts() {
        return this.postRepo.findAll();
    }

    // Read
    @GetMapping("/{id}")
    public Post getAllPosts(@PathVariable Long id) {
        return this.postRepo.findById(id).orElseThrow();
    }

    // Create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post addPost(@RequestBody Post post) {
        return this.postRepo.save(post);
    }

    // Update...

    // Delete...

}
