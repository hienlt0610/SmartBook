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
 * DAO for table "BOOKMARKED".
 */
public class BookmarkedDao extends AbstractDao<Bookmarked, Long> {

    public static final String TABLENAME = "BOOKMARKED";

    /**
     * Properties of entity Bookmarked.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Bid = new Property(1, String.class, "bid", false, "BID");
        public final static Property Page = new Property(2, int.class, "page", false, "PAGE");
    }


    public BookmarkedDao(DaoConfig config) {
        super(config);
    }

    public BookmarkedDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOKMARKED\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BID\" TEXT," + // 1: bid
                "\"PAGE\" INTEGER NOT NULL );"); // 2: page
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_BOOKMARKED_BID_PAGE ON \"BOOKMARKED\"" +
                " (\"BID\" ASC,\"PAGE\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOKMARKED\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Bookmarked entity) {
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
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Bookmarked entity) {
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
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Bookmarked readEntity(Cursor cursor, int offset) {
        Bookmarked entity = new Bookmarked( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bid
                cursor.getInt(offset + 2) // page
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Bookmarked entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPage(cursor.getInt(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Bookmarked entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Bookmarked entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Bookmarked entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
