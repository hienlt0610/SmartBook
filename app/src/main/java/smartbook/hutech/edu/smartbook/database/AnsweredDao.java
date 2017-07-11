package smartbook.hutech.edu.smartbook.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ANSWERED".
*/
public class AnsweredDao extends AbstractDao<Answered, Long> {

    public static final String TABLENAME = "ANSWERED";

    /**
     * Properties of entity Answered.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Bid = new Property(1, String.class, "bid", false, "BID");
        public final static Property Page = new Property(2, int.class, "page", false, "PAGE");
        public final static Property QuizIndex = new Property(3, int.class, "quizIndex", false, "quiz_index");
        public final static Property QuizAnswer = new Property(4, String.class, "quizAnswer", false, "quiz_answer");
    }


    public AnsweredDao(DaoConfig config) {
        super(config);
    }
    
    public AnsweredDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ANSWERED\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BID\" TEXT," + // 1: bid
                "\"PAGE\" INTEGER NOT NULL ," + // 2: page
                "\"quiz_index\" INTEGER NOT NULL ," + // 3: quizIndex
                "\"quiz_answer\" TEXT);"); // 4: quizAnswer
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_ANSWERED_BID_PAGE_quiz_index ON \"ANSWERED\"" +
                " (\"BID\" ASC,\"PAGE\" ASC,\"quiz_index\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ANSWERED\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Answered entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bid = entity.getBid();
        if (bid != null) {
            stmt.bindString(2, bid);
        }
        stmt.bindLong(3, entity.getPage());
        stmt.bindLong(4, entity.getQuizIndex());
 
        String quizAnswer = entity.getQuizAnswer();
        if (quizAnswer != null) {
            stmt.bindString(5, quizAnswer);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Answered entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bid = entity.getBid();
        if (bid != null) {
            stmt.bindString(2, bid);
        }
        stmt.bindLong(3, entity.getPage());
        stmt.bindLong(4, entity.getQuizIndex());
 
        String quizAnswer = entity.getQuizAnswer();
        if (quizAnswer != null) {
            stmt.bindString(5, quizAnswer);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Answered readEntity(Cursor cursor, int offset) {
        Answered entity = new Answered( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bid
            cursor.getInt(offset + 2), // page
            cursor.getInt(offset + 3), // quizIndex
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // quizAnswer
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Answered entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPage(cursor.getInt(offset + 2));
        entity.setQuizIndex(cursor.getInt(offset + 3));
        entity.setQuizAnswer(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Answered entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Answered entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Answered entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
