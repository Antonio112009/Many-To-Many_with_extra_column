package com.antonio112009.manyToMany.repository;

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
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
        tagRepository.saveAll(Arrays.asList(new Tag("tag1"),new Tag("tag2"),new Tag("tag3")));
        tagRepository.flush();
    }


    @Test
    @Order(1)
    void findByName() {
        Tag tag = tagRepository.findByName("tag1");

        assertNotNull(tag, "check that tag was found");
        assertEquals("tag1", tag.getName(), "checking that correct tag was found");
    }

    @Test
    @Order(2)
    void findByNameIn() {
        List<Tag> tags = tagRepository.findByNameIn(Arrays.asList("tag1", "tag3"));

        assertEquals(2, tags.size(), "checking that we receives 2 object from the database");
        assertEquals("tag1", tags.get(0).getName(), "checking that first list item has first tag");
        assertEquals("tag3", tags.get(1).getName(), "checking that list has second tag");
    }
}