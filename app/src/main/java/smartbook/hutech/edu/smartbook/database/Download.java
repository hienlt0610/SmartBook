package smartbook.hutech.edu.smartbook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by hienl on 7/13/2017.
 */

@Entity(indexes = {
        @Index(value = "bid", unique = true)
})
public class Download {
    @Id(autoincrement = true)
    private Long id;

    @Property
    private int bid;
    @Property
    private String title;
    @Property
    private String author;
    @Property
    private String description;
    @Property
    private long totalSize;
    @Property
    private long created;
    @Property
    private long lastRead;
    @Transient
    private String imgPath;
@Generated(hash = 470094925)
public Download(Long id, int bid, String title, String author,
        String description, long totalSize, long created, long lastRead) {
    this.id = id;
    this.bid = bid;
    this.title = title;
    this.author = author;
    this.description = description;
    this.totalSize = totalSize;
    this.created = created;
    this.lastRead = lastRead;
}
@Generated(hash = 1462805409)
public Download() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public int getBid() {
    return this.bid;
}
public void setBid(int bid) {
    this.bid = bid;
}
public String getTitle() {
    return this.title;
}
public void setTitle(String title) {
    this.title = title;
}
public String getAuthor() {
    return this.author;
}
public void setAuthor(String author) {
    this.author = author;
}
public String getDescription() {
    return this.description;
}
public void setDescription(String description) {
    this.description = description;
}
public long getTotalSize() {
    return this.totalSize;
}
public void setTotalSize(long totalSize) {
    this.totalSize = totalSize;
}
public long getCreated() {
    return this.created;
}
public void setCreated(long created) {
    this.created = created;
}
public long getLastRead() {
    return this.lastRead;
}
public void setLastRead(long lastRead) {
    this.lastRead = lastRead;
}

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}

