package coolpharaoh.tee.speicher.tea.timer.views.exportimport;

import androidx.lifecycle.ViewModel;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

public class ExportImportViewModel extends ViewModel {


    private final TeaDAO teaDAO;
    private final InfusionDAO infusionDAO;
    private final CounterDAO counterDAO;
    private final NoteDAO noteDAO;

    public ExportImportViewModel(TeaMemoryDatabase database) {
        teaDAO = database.getTeaDAO();
        infusionDAO = database.getInfusionDAO();
        counterDAO = database.getCounterDAO();
        noteDAO = database.getNoteDAO();
    }

    //Teas
    public List<Tea> getTeaList(){
        return teaDAO.getTeas();
    }

    public long insertTea(Tea tea){
        return teaDAO.insert(tea);
    }

    public void deleteAllTeas(){
        teaDAO.deleteAll();
    }

    //Infusions
    public List<Infusion> getInfusionList(){
        return infusionDAO.getInfusions();
    }

    public void insertInfusion(Infusion infusion){
        infusionDAO.insert(infusion);
    }

    //Counters
    public List<Counter> getCounterList(){
        return counterDAO.getCounters();
    }

    public void insertCounter(Counter counter){
        counterDAO.insert(counter);
    }

    //Notes
    public List<Note> getNoteList(){
        return noteDAO.getNotes();
    }

    public void insertNote(Note note){
        noteDAO.insert(note);

    }

}