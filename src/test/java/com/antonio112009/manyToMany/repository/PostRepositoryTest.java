package com.antonio112009.manyToMany.repository;


import com.antonio112009.manyToMany.entity.Post;
import com.antonio112009.manyToMany.entity.Tag;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        postRepository.saveAll(Arrays.asList(new Post("post1"),new Post("post2"),new Post("post3")));
        postRepository.flush();
    }


    @Test
    @Order(1)
    void findByTitle() {
        Post post = postRepository.findByTitle("post1");

        assertNotNull(post, "check that post was found");
        assertEquals("post1", post.getTitle(), "checking that correct title was found");
    }

    @Test
    @Order(2)
    void findByNameIn() {
        List<Post> posts = postRepository.findByTitleIn(Arrays.asList("post1", "post3"));

        assertEquals(2, posts.size(), "checking that we receives 2 object from the database");
        assertEquals("post1", posts.get(0).getTitle(), "checking that list has first tag");
        assertEquals("post3", posts.get(1).getTitle(), "checking that list has second tag");
    }
}