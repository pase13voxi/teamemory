package coolpharaoh.tee.speicher.tea.timer.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "settings")
public class ActualSettings {
    @PrimaryKey
    @NonNull
    private Long id;

    private String musicchoice;
    private String musicname;
    private boolean vibration;
    private boolean notification;
    private boolean animation;
    private String temperatureunit;
    private boolean showteaalert;
    private boolean mainproblemalert;
    private boolean mainratealert;
    private int mainratecounter;
    //0 = sort by Date, 1 = sort alphabethically, 2 = sort by variety
    private int sort;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getMusicchoice() {
        return musicchoice;
    }

    public void setMusicchoice(String musicchoice) {
        this.musicchoice = musicchoice;
    }

    public String getMusicname() {
        return musicname;
    }

    public void setMusicname(String musicname) {
        this.musicname = musicname;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public String getTemperatureunit() {
        return temperatureunit;
    }

    public void setTemperatureunit(String temperatureunit) {
        this.temperatureunit = temperatureunit;
    }

    public boolean isShowteaalert() {
        return showteaalert;
    }

    public void setShowteaalert(boolean showteaalert) {
        this.showteaalert = showteaalert;
    }

    public boolean isMainproblemalert() {
        return mainproblemalert;
    }

    public void setMainproblemalert(boolean mainproblemalert) {
        this.mainproblemalert = mainproblemalert;
    }

    public boolean isMainratealert() {
        return mainratealert;
    }

    public void setMainratealert(boolean mainratealert) {
        this.mainratealert = mainratealert;
    }

    public int getMainratecounter() {
        return mainratecounter;
    }

    public void setMainratecounter(int mainratecounter) {
        this.mainratecounter = mainratecounter;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
