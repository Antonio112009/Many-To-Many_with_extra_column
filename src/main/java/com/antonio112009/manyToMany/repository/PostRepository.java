package com.antonio112009.manyToMany.repository;

import com.antonio112009.manyToMany.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("from Post post left join fetch post.tags tag where post.title = :title")
    Post findByTitle(String title);
}
