package com.antonio112009.manyToMany.repository;

import com.antonio112009.manyToMany.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("from Post post " +
            "left join fetch post.tags " +
            "where post.title = :title")
    Post findByTitle(String title);

    List<Post> findByTitleIn(List<String> titles);
}
