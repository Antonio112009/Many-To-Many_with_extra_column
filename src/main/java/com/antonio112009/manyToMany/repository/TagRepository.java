package com.antonio112009.manyToMany.repository;

import com.antonio112009.manyToMany.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("from Tag tag " +
            "left join fetch tag.posts " +
            "where tag.name = :tagName")
    Tag findByName(String tagName);

    List<Tag> findByNameIn(List<String> names);

    void deleteByName(String name);
}
