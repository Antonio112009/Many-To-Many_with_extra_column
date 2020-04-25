package com.antonio112009.manyToMany.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table
@Data
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(
            mappedBy = "tag",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PostTag> posts = new ArrayList<>();

    public void addPost(Post post) {
        PostTag postTag = new PostTag(post, this);
        posts.add(postTag);
        post.getTags().add(postTag);
    }

    public void removePost(Post post) {
        for (Iterator<PostTag> iterator = posts.iterator();
             iterator.hasNext(); ) {
            PostTag postTag = iterator.next();

            if (postTag.getPost().equals(post) && postTag.getTag().equals(this)) {
                iterator.remove();
                postTag.getPost().getTags().remove(postTag);
                postTag.setPost(null);
                postTag.setTag(null);
            }
        }
    }
}