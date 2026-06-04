package io.theurl.bundle.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;

import java.time.LocalDateTime;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class BundleExtend extends AggregateRoot<Long> {
    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    public BundleExtend(long id) {
        super(id);
    }

    private int itemsCount;
    private int favoriteCount;
    private int commentCount;
    private int visitCount;
    private LocalDateTime lastVisitedAt;

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public LocalDateTime getLastVisitedAt() {
        return lastVisitedAt;
    }

    public void setLastVisitedAt(LocalDateTime lastVisitedAt) {
        this.lastVisitedAt = lastVisitedAt;
    }

    public void incrementItemCount() {
        this.itemsCount++;
    }

    public void incrementFavoriteCount() {
        this.favoriteCount++;
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void incrementVisitCount() {
        this.visitCount++;
    }

    public void decrementItemCount() {
        if (this.itemsCount > 0) {
            this.itemsCount--;
        }
    }

    public void decrementFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }
    }

    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void decrementVisitCount() {
        if (this.visitCount > 0) {
            this.visitCount--;
        }
    }
}
