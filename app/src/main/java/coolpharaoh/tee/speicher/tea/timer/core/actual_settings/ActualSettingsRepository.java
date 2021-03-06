package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

public class ActualSettingsRepository {
    private final ActualSettingsDao actualSettingsDao;

    public ActualSettingsRepository(Application application) {
        TeaMemoryDatabase teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application);
        actualSettingsDao = teaMemoryDatabase.getActualSettingsDao();
    }

    public void insertSettings(ActualSettings actualSettings) {
        actualSettingsDao.insert(actualSettings);
    }

    public void updateSettings(ActualSettings actualSettings) {
        actualSettingsDao.update(actualSettings);
    }

    public ActualSettings getSettings() {
        return actualSettingsDao.getSettings();
    }

    public int getCountItems() {
        return actualSettingsDao.getCountItems();
    }
}
