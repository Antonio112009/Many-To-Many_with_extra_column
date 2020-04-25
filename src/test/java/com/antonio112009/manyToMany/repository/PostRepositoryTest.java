package com.antonio112009.manyToMany.repository;


import com.antonio112009.manyToMany.entity.Post;
import com.antonio112009.manyToMany.entity.Tag;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        postTagRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    @Order(1)
    @Transactional
    void saveTagToPost(){
        Tag tag = new Tag();
        tag.setName("business");
        tagRepository.save(tag);

        Post post = new Post();
        post.setTitle("Hibernate");
        tag = tagRepository.findByName("business");
        post.addTag(tag);
        postRepository.save(post);

        post = postRepository.findByTitle("Hibernate");

        assertNotNull(post,"checking if post exists");
        assertEquals("Hibernate", post.getTitle(),"checking if title exists");
        assertEquals(1, post.getTags().size(),"checking if middle table exists");
        assertNotNull(post.getTags().get(0).getTag(),"checking if middle table exists");
        assertEquals("business",post.getTags().get(0).getTag().getName(),"checking if middle table exists");
    }

    @Test
    @Order(2)
    @Transactional
    void savePostToTag() {
        Post post = new Post();
        post.setTitle("Hibernate");
        postRepository.save(post);

        Tag tag = new Tag();
        tag.setName("business");
        Post postDb = postRepository.findByTitle("Hibernate");
        tag.addPost(postDb);

        tagRepository.save(tag);

        tag = tagRepository.findByName("business");

        assertNotNull(tag,"checking if tag exists");
        assertEquals("business", tag.getName(),"checking if name exists");
        assertEquals(1, tag.getPosts().size(),"checking if middle table exists");
        assertNotNull(tag.getPosts().get(0).getPost(),"checking if post exists");
        assertEquals("Hibernate",tag.getPosts().get(0).getPost().getTitle(),"checking if title correct");
    }

    @Test
    @Order(3)
    @Transactional
    void saveMultiplePostToTag() {
        Post post = new Post();
        post.setTitle("Hibernate");
        postRepository.save(post);

        Post post2 = new Post();
        post2.setTitle("High-Performance Java Persistence");
        postRepository.save(post2);

        Tag tag = new Tag();
        tag.setName("business");
        tag.addPost(postRepository.findByTitle("Hibernate"));
        tag.addPost(postRepository.findByTitle("High-Performance Java Persistence"));
        tagRepository.save(tag);

        tag = tagRepository.findByName("business");

        assertNotNull(tag,"checking if tag exists");
        assertEquals("business", tag.getName(),"checking if name exists");
        assertEquals(2, tag.getPosts().size(),"checking if middle table exists");
        assertNotNull(tag.getPosts().get(0).getPost(),"checking if post exists");
        assertNotNull(tag.getPosts().get(1).getPost(),"checking if post exists");
        assertEquals("Hibernate",tag.getPosts().get(0).getPost().getTitle(),"checking if title correct");
    }

    @Test
    @Order(4)
    @Transactional
    void editPostToTag() {
        Post post = new Post();
        post.setTitle("Hibernate");
        postRepository.save(post);

        Tag tag = new Tag();
        tag.setName("business");
        Post postDb = postRepository.findByTitle("Hibernate");
        tag.addPost(postDb);

        tagRepository.save(tag);

        // Modifying data
        tag = tagRepository.findByName("business");
        tag.setName("work");
        tag.getPosts().get(0).setCreatedOn("Testing this");
        tag.getPosts().get(0).getPost().setTitle("Spring");
        tagRepository.save(tag);

        // Test
        tag = tagRepository.findByName("work");

        assertNotNull(tag,"checking if tag exists");
        assertEquals("work", tag.getName(),"checking if name exists");
        assertEquals(1, tag.getPosts().size(),"checking if middle table exists");
        assertEquals("Testing this", tag.getPosts().get(0).getCreatedOn(),"checking if text in middle table correct");
        assertNotNull(tag.getPosts().get(0).getPost(),"checking if post exists");
        assertEquals("Spring",tag.getPosts().get(0).getPost().getTitle(),"checking if title correct");
    }

    @Test
    @Order(5)
    @Transactional
    void deleteConnectionPostToTag() {
        Post post = new Post();
        post.setTitle("Hibernate");
        postRepository.save(post);

        Tag tag = new Tag();
        tag.setName("business");
        Post postDb = postRepository.findByTitle("Hibernate");
        tag.addPost(postDb);

        tagRepository.save(tag);

        // Deleting

        tag = tagRepository.findByName("business");
        tag.removePost(postRepository.findByTitle("Hibernate"));
        tagRepository.save(tag);

        // Test

        tag = tagRepository.findByName("business");
        post = postRepository.findByTitle("Hibernate");

        assertNotNull(tag,"checking if tag exists");
        assertNotNull(post,"checking if post exists");
        assertEquals("Hibernate", post.getTitle() ,"checking if title correct");
        assertEquals(0, post.getTags().size(),"checking if middle table doesn't exists");
    }
}