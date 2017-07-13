package smartbook.hutech.edu.smartbook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hienl on 7/5/2017.
 */

@Entity(
        indexes = {
                @Index(value = "bid, page", unique = true)
        }
)
public class Bookmarked {
    @Id(autoincrement = true)
    private Long id;
    @Property
    private int bid;
    @Property
    private int page;
@Generated(hash = 714045245)
public Bookmarked(Long id, int bid, int page) {
    this.id = id;
    this.bid = bid;
    this.page = page;
}
@Generated(hash = 1136567324)
public Bookmarked() {
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
public int getPage() {
    return this.page;
}
public void setPage(int page) {
    this.page = page;
}

}
