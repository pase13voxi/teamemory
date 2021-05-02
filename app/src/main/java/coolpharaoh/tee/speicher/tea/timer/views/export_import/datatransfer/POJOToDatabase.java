package coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo.CounterPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo.InfusionPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo.NotePOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo.TeaPOJO;

class POJOToDatabase {
    private final DataTransferViewModel dataTransferViewModel;

    POJOToDatabase(DataTransferViewModel dataTransferViewModel) {
        this.dataTransferViewModel = dataTransferViewModel;
    }

    void fillDatabaseWithTeaList(List<TeaPOJO> teaList, boolean keepStoredTeas) {
        if (!keepStoredTeas) {
            deleteStoredTeas();
        }
        for (TeaPOJO teaPOJO : teaList) {
            // insert Tea first
            long teaId = insertTea(teaPOJO);
            insertInfusions(teaId, teaPOJO.getInfusions());
            insertCounters(teaId, teaPOJO.getCounters());
            insertNotes(teaId, teaPOJO.getNotes());
        }
    }

    private void deleteStoredTeas() {
        dataTransferViewModel.deleteAllTeas();
    }

    private long insertTea(TeaPOJO teaPOJO) {
        final Tea tea = new Tea(teaPOJO.getName(), teaPOJO.getVariety(),
                teaPOJO.getAmount(), teaPOJO.getAmountKind(), teaPOJO.getColor(),
                teaPOJO.getNextInfusion(), teaPOJO.getDate());
        tea.setRating(teaPOJO.getRating());
        tea.setFavorite(teaPOJO.isFavorite());

        return dataTransferViewModel.insertTea(tea);
    }

    private void insertInfusions(long teaId, List<InfusionPOJO> infusionList) {
        for (InfusionPOJO infusionPOJO : infusionList) {
            dataTransferViewModel.insertInfusion(new Infusion(teaId, infusionPOJO.getInfusionindex(),
                    infusionPOJO.getTime(), infusionPOJO.getCooldowntime(),
                    infusionPOJO.getTemperaturecelsius(), infusionPOJO.getTemperaturefahrenheit()));
        }
    }

    private void insertCounters(long teaId, List<CounterPOJO> counterList) {
        for (CounterPOJO counterPOJO : counterList) {
            dataTransferViewModel.insertCounter(new Counter(teaId, counterPOJO.getDay(),
                    counterPOJO.getWeek(), counterPOJO.getMonth(), counterPOJO.getOverall(),
                    counterPOJO.getDaydate(), counterPOJO.getWeekdate(), counterPOJO.getMonthdate()));
        }
    }

    private void insertNotes(long teaId, List<NotePOJO> noteList) {
        for (NotePOJO notePOJO : noteList) {
            dataTransferViewModel.insertNote(new Note(teaId, notePOJO.getPosition(),
                    notePOJO.getHeader(), notePOJO.getDescription()));
        }
    }
}