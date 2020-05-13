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
@ToString(exclude = "tags")
public class Post {

    public Post(){}
    public Post(String title){
        this.title = title;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PostTag> tags = new ArrayList<>();

    public void addTag(Tag tag, String createdOn) {
        PostTag postTag = new PostTag(this, tag, createdOn);
        tags.add(postTag);
        tag.getPosts().add(postTag);
    }

    public void removeTag(Tag tag) {
        for(PostTag postTag : tag.getPosts()){
            if (postTag.getPost().equals(this) && postTag.getTag().equals(tag)) {
                postTag.getTag().getPosts().remove(postTag);
                postTag.setPost(null);
                postTag.setTag(null);
            }
        }
    }

    public void removeAllTags(){
        for(PostTag postTag : new ArrayList<>(tags)) {
            tags.remove(postTag);
            postTag.getPost().getTags().remove(postTag);
            postTag.setPost(null);
            postTag.setTag(null);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

}
