package com.example.blog_application.blogapplication.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.blog_application.blogapplication.dto.ReqRes;
import com.example.blog_application.blogapplication.exceptions.ResourceNotFoundException;
import com.example.blog_application.blogapplication.models.Category;
import com.example.blog_application.blogapplication.models.Post;
import com.example.blog_application.blogapplication.models.User;
import com.example.blog_application.blogapplication.payloads.PostDto;
import com.example.blog_application.blogapplication.payloads.PostResponse;
import com.example.blog_application.blogapplication.repository.CategoryRepo;
import com.example.blog_application.blogapplication.repository.PostRepo;
import com.example.blog_application.blogapplication.repository.UserRepo;
import com.example.blog_application.blogapplication.services.PostService;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepo postRepo ;

    @Autowired
    private UserRepo userRepo ;
    
    @Autowired
    private CategoryRepo categoryRepo ;

    @Autowired
    private ModelMapper modelMapper ;

    @Override
    public ReqRes createPost(PostDto postDto,Integer userId, Integer categoryId) {
        ReqRes response = new ReqRes();
        try{
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "UserId", userId));
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));
        Post post =this.modelMapper.map(postDto,Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);
        
        Post data =this.postRepo.save(post);
        response.setId(data.getPostId());
        response.setStatusCode(200);
        response.setMessage("Post Saved");
        }catch(Exception e){
        response.setStatusCode(400);
        response.setMessage("Something went wrong");
        }
        return response ;
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "PostId", postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());
        Post updatedPost=this.postRepo.save(post);
        return this.modelMapper.map(updatedPost, PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "PostId", postId));
        this.postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
        Sort sort = null;
        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        }else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize,sort);
        Page<Post> pagePost=this.postRepo.findAll(p);
        List<Post> allPosts= pagePost.getContent();
        List<PostDto> postDtos=allPosts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        PostResponse postResponse =new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());
        return postResponse ;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post= this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "PostId", postId));
        return this.modelMapper.map(post,PostDto.class) ;
    }

    // @Override
    // public List<PostDto> getPostsByCategory(Integer categoryId) {
    //     Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));
    //     List<Post> posts = this.postRepo.findByCategory(cat);
    //     List<PostDto> postDtos=posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
    //     return postDtos ;
    // }
    @Override
public List<PostDto> getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
    Category cat = this.categoryRepo.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

    // Change the sorting direction to descending order for most recent first
    Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.DESC : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
    Page<Post> postPage = this.postRepo.findByCategory(cat, pageable);

    List<PostDto> postDtos = postPage.getContent().stream()
            .map(post -> this.modelMapper.map(post, PostDto.class))
            .collect(Collectors.toList());

    return postDtos;
}

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        
        User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "UserID", userId));
        List<Post> posts=this.postRepo.findByUser(user);
        List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos ;
    }

    @Override
    public List<PostDto> searchPosts(String keywords) {
        List<Post> posts=this.postRepo.searchByTitle("%"+keywords+"%");
        List<PostDto> postDtos=posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos ;
    }
    
}
