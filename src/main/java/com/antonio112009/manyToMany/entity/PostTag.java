package com.antonio112009.manyToMany.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@ToString(exclude = {"tag"})
public class PostTag {

    public PostTag() {}

    public PostTag(Post post, Tag tag, String createdOn) {
        this.post = post;
        this.tag = tag;
        this.createdOn = createdOn;
        this.id = new PostTagId(post.getId(), tag.getId());
    }

    @EmbeddedId
    private PostTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    private Tag tag;

    @Column
    private String createdOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        PostTag that = (PostTag) o;
        return Objects.equals(post, that.post) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, tag);
    }
}