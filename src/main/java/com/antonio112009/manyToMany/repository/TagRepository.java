package com.antonio112009.manyToMany.repository;

import com.antonio112009.manyToMany.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select tag " +
            "from Tag tag " +
            "left join fetch tag.posts " +
            "where tag.name = :tagName")
    Tag findByName(String tagName);
}
