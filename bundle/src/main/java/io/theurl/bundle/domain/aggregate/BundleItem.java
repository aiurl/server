package io.theurl.bundle.domain.aggregate;

import io.theurl.framework.domain.Entity;
import io.theurl.framework.utility.SnowflakeId;

public class BundleItem extends Entity<Long> {
    /**
     * Initializes the entity with the given id.
     *
     * @param id the identifier of the entity
     */
    public BundleItem(long id) {
        super(id);
    }

    private String url;
    private String title;
    private String description;
    private String image;
    private int order;

    public static BundleItem create(String url, String title) {
        BundleItem item = new BundleItem(SnowflakeId.getInstance().nextId());
        item.url = url;
        item.title = title;
        return item;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
