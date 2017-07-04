package smartbook.hutech.edu.smartbook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

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
    private String bid;
    @Property
    private int page;

    @Generated(hash = 3167285)
    public Bookmarked(Long id, String bid, int page) {
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

    public String getBid() {
        return this.bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
