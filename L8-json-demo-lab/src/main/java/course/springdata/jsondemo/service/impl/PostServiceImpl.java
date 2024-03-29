package course.springdata.jsondemo.service.impl;

import course.springdata.jsondemo.dao.PostRepository;
import course.springdata.jsondemo.dao.UserRepository;
import course.springdata.jsondemo.entity.Post;
import course.springdata.jsondemo.exception.InvalidEntityDataException;
import course.springdata.jsondemo.exception.NonexisitingEntityException;
import course.springdata.jsondemo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    @Autowired
    public PostServiceImpl(PostRepository postRepo, UserRepository userRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }


    @Override
    public List<Post> getAllPosts() {
        return this.postRepo.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return this.postRepo.findById(id).orElseThrow(() ->
            new NonexisitingEntityException(String.format("Post with ID=%s does not exist.", id)));
    }

    @Transactional
    @Override
    public Post addPost(Post post) {
        post.setId(null);
        if(post.getAuthor() == null) {
            if(post.getAuthorId() == null) {
                throw new InvalidEntityDataException("Post author is required but missing."); // request 400
            }
            post.setAuthor(this.userRepo.findById(post.getAuthorId()).orElseThrow(() ->
                    new InvalidEntityDataException(
                            String.format("Post author with ID:%s does not exist.",post.getAuthorId()))));
        }
        return this.postRepo.save(post);
    }

    @Transactional
    @Override
    public Post updatePost(Post post) {
        getPostById(post.getId());
        return this.postRepo.save(post);
    }

    @Transactional
    @Override
    public Post deletePost(Long id) {
        Post removed = getPostById(id);
        this.postRepo.deleteById(id);
        return removed;
    }

    @Override
    public long getPostsCount() {
        return this.postRepo.count();
    }
}
