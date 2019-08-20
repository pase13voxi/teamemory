package coolpharaoh.tee.speicher.tea.timer.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "note",foreignKeys =
@ForeignKey(entity = Tea.class,parentColumns = "tea_id",childColumns = "tea_id", onDelete = ForeignKey.CASCADE), indices = {@Index("tea_id")})
public class Note {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "note_id")
    private Long id;
    @ColumnInfo(name = "tea_id")
    private long teaId;

    private int position;
    private String header;
    private String description;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public long getTeaId() {
        return teaId;
    }

    public void setTeaId(long teaId) {
        this.teaId = teaId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}