package io.theurl.bundle.domain.aggregate;

import io.theurl.framework.domain.Entity;

import java.time.LocalDateTime;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class BundleComment extends Entity<Long> {

    public BundleComment(long id) {
        super(id);
        createdAt = LocalDateTime.now();
    }

    private String content;
    private Long authorId;
    private String authorName;
    private String contact;
    private LocalDateTime createdAt;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
