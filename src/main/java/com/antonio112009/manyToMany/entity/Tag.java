package com.antonio112009.manyToMany.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


@Entity
@Table
@Getter
@Setter
@ToString
//@ToString(exclude = "posts")
public class Tag {

    public Tag() { }
    public Tag(String name) {
        this.name = name;
    }


    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @OneToMany(
            mappedBy = "tag",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PostTag> posts = new ArrayList<>();

    public void addPost(Post post, String createdOn) {
        PostTag postTag = new PostTag(post, this, createdOn);
        posts.add(postTag);
        post.getTags().add(postTag);
    }

    public void removePost(Post post) {
        for(PostTag postTag : post.getTags()){
            if (postTag.getPost().equals(post) && postTag.getTag().equals(this)) {
                posts.remove(postTag);
                postTag.getPost().getTags().remove(postTag);
                postTag.setPost(null);
                postTag.setTag(null);
            }
        }
    }

    public void removeAllPosts(){
        for(PostTag postTag : new ArrayList<>(posts)) {
            posts.remove(postTag);
            postTag.getPost().getTags().remove(postTag);
            postTag.setPost(null);
            postTag.setTag(null);
        }
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if(obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Tag other = (Tag) obj;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}