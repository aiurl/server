package io.theurl.bundle.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;
import io.theurl.framework.utility.SnowflakeId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class Bundle extends AggregateRoot<Long> {
    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    public Bundle(long id) {
        super(id);
        extend = new BundleExtend(id);
    }

    private String type;
    private String name;
    private String vanity;
    private String description;
    private String image;
    private int order;
    private Long ownerId;
    private String ownerName;
    private List<BundleItem> items = new ArrayList<>();
    private List<BundleComment> comments = new ArrayList<>();
    private BundleExtend extend;
    private boolean deleted;

    public static Bundle create(String type, String vanity, String name, Long ownerId, String ownerName) {
        var aggregate = new Bundle(SnowflakeId.getInstance().nextId());
        aggregate.type = type;
        aggregate.vanity = vanity;
        aggregate.name = name;
        aggregate.ownerId = ownerId;
        aggregate.ownerName = ownerName;
        return aggregate;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVanity() {
        return vanity;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<BundleItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<BundleComment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void appendItem(String url, String title, String description, String image) {
        if (items.stream().anyMatch(item -> item.getUrl().equals(url))) {
            return;
        }
        var item = BundleItem.create(url, title);
        item.setDescription(description);
        item.setImage(image);
        item.setOrder(items.size() + 1);
        items.add(item);
        extend.incrementItemCount();
    }

    public void removeItem(long id) {
        if (items.removeIf(item -> item.getId() == id)) {
            extend.decrementItemCount();
        }
    }

    public void clearItems() {
        items.clear();
        extend.setItemCount(0);
    }

    public BundleExtend getExtend() {
        return extend;
    }
}
