package com.antonio112009.manyToMany.common;

import com.antonio112009.manyToMany.entity.Post;
import com.antonio112009.manyToMany.entity.PostTag;
import com.antonio112009.manyToMany.entity.Tag;
import com.antonio112009.manyToMany.repository.PostRepository;
import com.antonio112009.manyToMany.repository.PostTagRepository;
import com.antonio112009.manyToMany.repository.TagRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommonTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    private final String tagName1 = "business";
    private final String tagName2 = "development";
    private final String postTitle1 = "Spring Boot";
    private final String postTitle2 = "Hibernate";
    private final String createdOn = "sunday";

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        postTagRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Save one tag to one post via postRepository")
    void saveTagToPost(){
        Tag tag = new Tag(tagName1);
        tagRepository.saveAndFlush(tag);

        Post post = new Post(postTitle1);
        tag = tagRepository.findByName(tagName1);
        post.addTag(tag, createdOn);
        postRepository.saveAndFlush(post);

        post = postRepository.findByTitle(postTitle1);

        // Testing
        assertNotNull(post,"checking if post exists");
        assertEquals(postTitle1, post.getTitle(),"checking if post title is correct");
        assertEquals(1, post.getTags().size(),"checking if middle table exists");
        assertEquals(createdOn, post.getTags().get(0).getCreatedOn(),"checking if createOn is correct");
        assertNotNull(post.getTags().get(0).getTag(),"checking if tag exists");
        assertEquals(tagName1 ,post.getTags().get(0).getTag().getName(),"checking if tag name is correct");
    }


    @Test
    @Order(2)
    @DisplayName("Save one post to one tag via tagRepository")
    void savePostToTag(){
        Post post = new Post(postTitle1);
        postRepository.saveAndFlush(post);

        Tag tag = new Tag(tagName1);
        post = postRepository.findByTitle(postTitle1);
        tag.addPost(post, createdOn);
        tagRepository.saveAndFlush(tag);

        tag = tagRepository.findByName(tagName1);

        // Testing
        assertNotNull(tag,"checking if tag exists");
        assertEquals(tagName1, tag.getName(),"checking if tag name is correct");
        assertEquals(1, tag.getPosts().size(),"checking if middle table exists");
        assertEquals(createdOn, tag.getPosts().get(0).getCreatedOn(),"checking if createOn is correct");
        assertNotNull(tag.getPosts().get(0).getPost(),"checking if post exists");
        assertEquals(postTitle1 ,tag.getPosts().get(0).getPost().getTitle(),"checking if post title is correct");
    }


    @Test
    @Order(3)
    @DisplayName("Save multiple tag to one post via postRepository")
    void saveMultipleTagsToPost(){
        // Following 3 lines can be simplified.
        Tag tag1 = new Tag(tagName1);
        Tag tag2 = new Tag(tagName2);
        tagRepository.saveAll(Arrays.asList(tag1, tag2));
        tagRepository.flush();

        Post post = new Post(postTitle1);
        //Following lines can be also simplified
        List<Tag> tags = tagRepository.findByNameIn(Arrays.asList(tagName1, tagName2));
        for (Tag tagDB : tags){
            post.addTag(tagDB, createdOn);
        }
        postRepository.saveAndFlush(post);

        post = postRepository.findByTitle(postTitle1);

        // Testing
        assertNotNull(post,"checking if post exists");
        assertEquals(postTitle1, post.getTitle(),"checking if post title is correct");
        assertEquals(2, post.getTags().size(),"checking if middle tables exists");
        assertEquals(createdOn, post.getTags().get(0).getCreatedOn(),"checking if createOn in first table is correct");
        assertEquals(createdOn, post.getTags().get(1).getCreatedOn(),"checking if createOn in second table is correct");
        assertNotNull(post.getTags().get(0).getTag(),"checking if tag in first table exists");
        assertNotEquals(post.getTags().get(1).getTag(),"checking if tag2 in second table exists");
        assertEquals(tagName1 ,post.getTags().get(0).getTag().getName(),"checking if tag name in first table is correct");
        assertEquals(tagName2 ,post.getTags().get(1).getTag().getName(),"checking if tag name in second table is correct");
    }


    @Test
    @Order(4)
    @DisplayName("Save multiple post to one tag via tagRepository")
    void saveMultiplePostsToTag(){
        // Following 3 lines can be simplified.
        Post post1 = new Post(postTitle1);
        Post post2 = new Post(postTitle2);
        postRepository.saveAll(Arrays.asList(post1, post2));
        postRepository.flush();

        Tag tag = new Tag(tagName1);
        //Following lines can be also simplified
        List<Post> posts = postRepository.findByTitleIn(Arrays.asList(postTitle1, postTitle2));
        for (Post postDB : posts){
            tag.addPost(postDB, createdOn);
        }
        tagRepository.saveAndFlush(tag);

        tag = tagRepository.findByName(tagName1);

        // Testing
        assertNotNull(tag,"checking if tag exists");
        assertEquals(tagName1, tag.getName(),"checking if tag name is correct");
        assertEquals(2, tag.getPosts().size(),"checking if middle table exists");
        assertEquals(createdOn, tag.getPosts().get(0).getCreatedOn(),"checking if createOn in first table is correct");
        assertEquals(createdOn, tag.getPosts().get(0).getCreatedOn(),"checking if createOn in second table is correct");
        assertNotNull(tag.getPosts().get(0).getPost(),"checking if post in first table  exists");
        assertNotNull(tag.getPosts().get(1).getPost(),"checking if post in second table  exists");
        assertEquals(postTitle1 ,tag.getPosts().get(0).getPost().getTitle(),"checking if post title in first table is correct");
        assertEquals(postTitle2 ,tag.getPosts().get(1).getPost().getTitle(),"checking if post title in second table is correct");
    }


    @Test
    @Order(5)
    @DisplayName("Edit multiple posts and createdOns in tag via tagRepository")
    void editMuliplePostsWithTag() {
        postRepository.saveAll(Arrays.asList(new Post(postTitle1), new Post(postTitle2)));
        postRepository.flush();

        Tag tag = new Tag(tagName1);
        for (Post postDB : postRepository.findByTitleIn(Arrays.asList(postTitle1, postTitle2))){
            tag.addPost(postDB, createdOn);
        }
        tagRepository.saveAndFlush(tag);

        tag = tagRepository.findByName(tagName1);

        tag.setName("Test Name");
        tag.getPosts().get(0).setCreatedOn("Test CreatedOn1");
        tag.getPosts().get(1).setCreatedOn("Test CreatedOn2");
        tag.getPosts().get(0).getPost().setTitle("Test Title1");
        tag.getPosts().get(1).getPost().setTitle("Test Title2");

        tagRepository.saveAndFlush(tag);

        tag = tagRepository.findByName("Test name");

        // Testing
        assertNotNull(tag,"checking if tag exists");
        assertEquals(2, tag.getPosts().size(),"checking if middle table exists");
        assertEquals("Test CreatedOn1", tag.getPosts().get(0).getCreatedOn(),"checking if createOn in first table is correct");
        assertEquals("Test CreatedOn2", tag.getPosts().get(1).getCreatedOn(),"checking if createOn in second table is correct");
        assertNotNull(tag.getPosts().get(0).getPost(),"checking if post in first table  exists");
        assertNotNull(tag.getPosts().get(1).getPost(),"checking if post in second table  exists");
        assertEquals("Test Title1" ,tag.getPosts().get(0).getPost().getTitle(),"checking if post title in first table is correct");
        assertEquals("Test Title2" ,tag.getPosts().get(1).getPost().getTitle(),"checking if post title in second table is correct");
    }


    @Test
    @Order(6)
    @DisplayName("deleting tag with postTag, but not posts via tagRepository")
    void deleteTagWithPostTagMultiplePosts(){
        postRepository.saveAll(Arrays.asList(new Post(postTitle1), new Post(postTitle2)));
        postRepository.flush();

        Tag tag = new Tag(tagName1);
        for (Post postDB : postRepository.findByTitleIn(Arrays.asList(postTitle1, postTitle2))){
            tag.addPost(postDB, createdOn);
        }
        tagRepository.saveAndFlush(tag);


        tag = tagRepository.findByName(tagName1);
//      If we delete one item... It would miss the following one. I don't know how to perform following code better.
        tag.removeAllPosts();

//        Alternative:
//        for (PostTag postTag : new ArrayList<>(tag.getPosts())) {
//            tag.removePost(postTag.getPost());
//        }
        tagRepository.saveAndFlush(tag);

//      Let's delete tag WITHOUT deleting posts
        tagRepository.deleteByName(tagName1);
        tagRepository.flush();

        tag = tagRepository.findByName(tagName1);

        //Testing
        assertNull(tag, "checking that tag is deleted");
        assertEquals(0, postTagRepository.findAll().size(), "checking that postTag table is deleted");
        assertEquals(2, postRepository.findAll().size(), "checking that post table is deleted");
    }


    @Test
    @Order(7)
    @DisplayName("deleting tag with posts (and postTag) connection via tagRepository")
    void deleteTagWithMultiplePosts(){
        postRepository.saveAll(Arrays.asList(new Post(postTitle1), new Post(postTitle2)));
        postRepository.flush();

        Tag tag = new Tag(tagName1);
        for (Post postDB : postRepository.findByTitleIn(Arrays.asList(postTitle1, postTitle2))){
            tag.addPost(postDB, createdOn);
        }
        tagRepository.saveAndFlush(tag);


//      Let's delete tag WITHOUT deleting posts
        tagRepository.deleteByName(tagName1);
        tagRepository.flush();

        tag = tagRepository.findByName(tagName1);

        //Testing
        assertNull(tag, "checking that tag is deleted");
        assertEquals(0, postTagRepository.findAll().size(), "checking that postTag table is deleted");
        assertEquals(0, postRepository.findAll().size(), "checking that post table is deleted");
    }
}
