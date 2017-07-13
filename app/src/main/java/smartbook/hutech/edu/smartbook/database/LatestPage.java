package smartbook.hutech.edu.smartbook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hienl on 7/5/2017.
 */

@Entity
public class LatestPage {
    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private int bid;

    @Index(unique = true)
    private int page;

    @Generated(hash = 1198380907)
    public LatestPage(Long id, int bid, int page) {
        this.id = id;
        this.bid = bid;
        this.page = page;
    }

    @Generated(hash = 2098889515)
    public LatestPage() {
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
