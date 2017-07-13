package smartbook.hutech.edu.smartbook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hienl on 7/5/2017.
 */
@Entity(indexes = {
        @Index(value = "bid,page,quizIndex", unique = true)
})
public class Answered {
    @Id(autoincrement = true)
    private Long id;

    @Property
    private int bid;

    @Property
    private int page;

    @Property(nameInDb = "quiz_index")
    private int quizIndex;

    @Property(nameInDb = "quiz_answer")
    private String quizAnswer;

@Generated(hash = 1720031839)
public Answered(Long id, int bid, int page, int quizIndex, String quizAnswer) {
    this.id = id;
    this.bid = bid;
    this.page = page;
    this.quizIndex = quizIndex;
    this.quizAnswer = quizAnswer;
}

@Generated(hash = 2061286730)
public Answered() {
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

public int getQuizIndex() {
    return this.quizIndex;
}

public void setQuizIndex(int quizIndex) {
    this.quizIndex = quizIndex;
}

public String getQuizAnswer() {
    return this.quizAnswer;
}

public void setQuizAnswer(String quizAnswer) {
    this.quizAnswer = quizAnswer;
}

}
