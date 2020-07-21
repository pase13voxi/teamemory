package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.models.repository.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.TeaRepository;

class DataTransferViewModel extends ViewModel {

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final CounterRepository counterRepository;
    private final NoteRepository noteRepository;

    DataTransferViewModel(Application application) {
        teaRepository = new TeaRepository(application);
        infusionRepository = new InfusionRepository(application);
        counterRepository = new CounterRepository(application);
        noteRepository = new NoteRepository(application);
    }

    //Teas
    List<Tea> getTeaList() {
        return teaRepository.getTeas();
    }

    long insertTea(Tea tea) {
        return teaRepository.insertTea(tea);
    }

    void deleteAllTeas() {
        teaRepository.deleteAllTeas();
    }

    //Infusions
    List<Infusion> getInfusionList() {
        return infusionRepository.getInfusions();
    }

    public void insertInfusion(Infusion infusion){
        infusionRepository.insertInfusion(infusion);
    }

    //Counters
    List<Counter> getCounterList() {
        return counterRepository.getCounters();
    }

    void insertCounter(Counter counter) {
        counterRepository.insertCounter(counter);
    }

    //Notes
    List<Note> getNoteList() {
        return noteRepository.getNotes();
    }

    void insertNote(Note note) {
        noteRepository.insertNote(note);

    }

}
