package coolpharaoh.tee.speicher.tea.timer.views.showtea;

import android.app.Application;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.models.repository.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.TeaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;


@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.*")
public class ShowTeaViewModelTest {
    private ShowTeaViewModel showTeaViewModel;

    @Mock
    Application application;
    @Mock
    Resources resources;
    @Mock
    TeaRepository teaRepository;
    @Mock
    InfusionRepository infusionRepository;
    @Mock
    NoteRepository noteRepository;
    @Mock
    CounterRepository counterRepository;
    @Mock
    ActualSettingsRepository actualSettingsRepository;

    private static final long TEA_ID = 1L;

    @Before
    public void setUp() throws Exception {
        mockRepositories();
        showTeaViewModel = new ShowTeaViewModel(TEA_ID, application);
    }

    private void mockRepositories() throws Exception {
        whenNew(TeaRepository.class).withAnyArguments().thenReturn(teaRepository);
        whenNew(InfusionRepository.class).withAnyArguments().thenReturn(infusionRepository);
        whenNew(NoteRepository.class).withAnyArguments().thenReturn(noteRepository);
        whenNew(CounterRepository.class).withAnyArguments().thenReturn(counterRepository);
        whenNew(ActualSettingsRepository.class).withAnyArguments().thenReturn(actualSettingsRepository);
    }

    @Test
    public void teaExist() {
        Tea tea = new Tea();
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        assertThat(showTeaViewModel.teaExists()).isTrue();
    }

    @Test
    public void teaDoesNotExist() {
        assertThat(showTeaViewModel.teaExists()).isFalse();
    }

    @Test
    public void getTeaId() {
        long teaIdBefore = 1L;

        Tea tea = new Tea();
        tea.setId(teaIdBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        long teaIdAfter = showTeaViewModel.getTeaId();

        assertThat(teaIdAfter).isEqualTo(teaIdBefore);
    }

    @Test
    public void getName(){
        String teaNameBefore = "TEA";

        Tea tea = new Tea();
        tea.setName(teaNameBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        String teaNameAfter = showTeaViewModel.getName();

        assertThat(teaNameAfter).isEqualTo(teaNameBefore);
    }

    @Test
    public void getVariety(){
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        int varietyIndex = 4;
        String varietyCode = varietyCodes[varietyIndex];
        String varietyBefore = varietyTeas[varietyIndex];

        Tea tea = new Tea();
        tea.setVariety(varietyCode);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(varietyCodes);
        when(resources.getStringArray(R.array.variety_teas)).thenReturn(varietyTeas);

        String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo(varietyBefore);
    }

    @Test
    public void getUnkownVariety(){
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        String varietyBefore = "VARIETY";

        Tea tea = new Tea();
        tea.setVariety(varietyBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(varietyCodes);
        when(resources.getStringArray(R.array.variety_teas)).thenReturn(varietyTeas);

        String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo(varietyBefore);
    }

    @Test
    public void getEmptyVariety(){
        String varietyBefore = "";

        Tea tea = new Tea();
        tea.setVariety(varietyBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo("-");
    }

    @Test
    public void getAmount(){
        int amountBefore = 1;

        Tea tea = new Tea();
        tea.setAmount(amountBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        int amountAfter = showTeaViewModel.getAmount();

        assertThat(amountAfter).isEqualTo(amountBefore);
    }

    @Test
    public void getAmountKind(){
        String amountKindBefore = "AMOUNT_KIND";

        Tea tea = new Tea();
        tea.setAmountKind(amountKindBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        String amountKindAfter = showTeaViewModel.getAmountKind();

        assertThat(amountKindAfter).isEqualTo(amountKindBefore);
    }

    @Test
    public void getColor(){
        int colorBefore = 1;

        Tea tea = new Tea();
        tea.setColor(colorBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        int colorAfter = showTeaViewModel.getColor();

        assertThat(colorAfter).isEqualTo(colorBefore);
    }

    @Test
    public void setCurrentDate(){
        Tea teaBefore = new Tea();
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(teaBefore);

        showTeaViewModel.setCurrentDate();

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((captor.capture()));
        Tea teaAfter = captor.getValue();
        Calendar teaDateAfter = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        teaDateAfter.setTime(teaAfter.getDate());

        Calendar currentDate = Calendar.getInstance();

        assertThat(teaDateAfter.get(Calendar.DAY_OF_MONTH)).isEqualTo(currentDate.get(Calendar.DAY_OF_MONTH));
        assertThat(teaDateAfter.get(Calendar.MONTH)).isEqualTo(currentDate.get(Calendar.MONTH));
        assertThat(teaDateAfter.get(Calendar.YEAR)).isEqualTo(currentDate.get(Calendar.YEAR));
    }

    @Test
    public void getLastInfusion() {
        int lastInfusionBefore = 0;

        Tea tea = new Tea();
        tea.setNextInfusion(lastInfusionBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        int lastInfusionAfter = showTeaViewModel.getNextInfusion();

        assertThat(lastInfusionAfter).isEqualTo(lastInfusionBefore);
    }

    @Test
    public void updateLastInfusion() {
        Tea tea = new Tea();
        tea.setNextInfusion(0);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(Arrays.asList(new Infusion(), new Infusion()));

        showTeaViewModel.updateNextInfusion();

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((captor.capture()));
        Tea lastInfusionAfter = captor.getValue();

        assertThat(lastInfusionAfter.getNextInfusion()).isEqualTo(1);
    }

    @Test
    public void updateLastInfusionBiggerOrEqual() {
        Tea tea = new Tea();
        tea.setNextInfusion(0);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(Collections.singletonList(new Infusion()));

        showTeaViewModel.updateNextInfusion();

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((captor.capture()));
        Tea lastInfusionAfter = captor.getValue();

        assertThat(lastInfusionAfter.getNextInfusion()).isEqualTo(0);
    }

    @Test
    public void navigateBetweenInfusions(){
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureUnit("Celsius");
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion1 = new Infusion(1L, 1, "1", "2", 1, 32);
        infusions.add(infusion1);

        Infusion infusion2 = new Infusion(1L, 2, "2:30", "5:30", 2, 33);
        infusions.add(infusion2);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions);

        assertThat(showTeaViewModel.getInfusionSize()).isEqualTo(infusions.size());
        assertThat(showTeaViewModel.getInfusionIndex()).isEqualTo(0);

        TimeHelper time1 = showTeaViewModel.getTime();
        TimeHelper cooldownTime1 = showTeaViewModel.getCooldowntime();
        int temperature1 = showTeaViewModel.getTemperature();

        assertThat(time1.time).isEqualTo(infusions.get(0).getTime());
        assertThat(time1.minutes).isEqualTo(1);
        assertThat(time1.seconds).isEqualTo(0);
        assertThat(cooldownTime1.time).isEqualTo(infusions.get(0).getCoolDownTime());
        assertThat(cooldownTime1.minutes).isEqualTo(2);
        assertThat(cooldownTime1.seconds).isEqualTo(0);
        assertThat(temperature1).isEqualTo(infusions.get(0).getTemperatureCelsius());

        showTeaViewModel.incrementInfusionIndex();
        assertThat(showTeaViewModel.getInfusionIndex()).isEqualTo(1);

        TimeHelper time2 = showTeaViewModel.getTime();
        TimeHelper cooldownTime2 = showTeaViewModel.getCooldowntime();
        int temperature2 = showTeaViewModel.getTemperature();

        assertThat(time2.time).isEqualTo(infusions.get(1).getTime());
        assertThat(time2.minutes).isEqualTo(2);
        assertThat(time2.seconds).isEqualTo(30);
        assertThat(cooldownTime2.time).isEqualTo(infusions.get(1).getCoolDownTime());
        assertThat(cooldownTime2.minutes).isEqualTo(5);
        assertThat(cooldownTime2.seconds).isEqualTo(30);
        assertThat(temperature2).isEqualTo(infusions.get(1).getTemperatureCelsius());

        actualSettings.setTemperatureUnit("Fahrenheit");
        showTeaViewModel.setInfusionIndex(0);
        assertThat(showTeaViewModel.getInfusionIndex()).isEqualTo(0);

        int temperature3 = showTeaViewModel.getTemperature();
        assertThat(temperature3).isEqualTo(infusions.get(0).getTemperatureFahrenheit());
    }

    @Test
    public void getEmptyTime(){
        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion1 = new Infusion(1L, 1, null, null, 1, 1);
        infusions.add(infusion1);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions);

        TimeHelper timeAfter = showTeaViewModel.getTime();

        assertThat( timeAfter.time).isNull();
        assertThat(timeAfter.minutes).isEqualTo(0);
        assertThat(timeAfter.seconds).isEqualTo(0);
    }

    @Test
    public void getNote(){
        Note noteBefore = new Note(1L, 1, "HEADER", "DESCRIPTION");
        when(noteRepository.getNoteByTeaId(TEA_ID)).thenReturn(noteBefore);

        Note noteAfter = showTeaViewModel.getNote();

        assertThat(noteAfter).isEqualTo(noteBefore);
    }

    @Test
    public void setNote(){
        String differentNote = "DIFFERENT_NOTE";

        Note noteBefore = new Note(1L, 1, "HEADER", "DESCRIPTION");
        when(noteRepository.getNoteByTeaId(TEA_ID)).thenReturn(noteBefore);

        showTeaViewModel.setNote(differentNote);

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).updateNote((captor.capture()));
        Note noteAfter = captor.getValue();

        assertThat(noteAfter.getDescription()).isEqualTo(differentNote);
    }

    @Test
    public void countCounter(){
        Date currentDate = Calendar.getInstance().getTime();
        Counter counterBefore = new Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate);
        when(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore);

        showTeaViewModel.countCounter();

        ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository).updateCounter((captor.capture()));
        Counter counterAfter = captor.getValue();

        assertThat(counterAfter.getDay()).isEqualTo(2);
        assertThat(counterAfter.getWeek()).isEqualTo(2);
        assertThat(counterAfter.getMonth()).isEqualTo(2);
        assertThat(counterAfter.getOverall()).isEqualTo(2);
    }

    @Test
    public void getCounter(){
        Date currentDate = Calendar.getInstance().getTime();
        Counter counterBefore = new Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate);
        when(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore);

        Counter counterAfter = showTeaViewModel.getCounter();

        assertThat(counterAfter).isEqualTo(counterBefore);
    }

    @Test
    public void isAnimation(){
        boolean animationBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setAnimation(animationBefore);
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        boolean animationAfter = showTeaViewModel.isAnimation();

        assertThat(animationAfter).isEqualTo(animationBefore);
    }

    @Test
    public void isShowTeaAlert(){
        boolean showTeaAlertBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setShowTeaAlert(showTeaAlertBefore);
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        boolean showTeaAlertAfter = showTeaViewModel.isShowteaalert();

        assertThat(showTeaAlertAfter).isEqualTo(showTeaAlertBefore);
    }

    @Test
    public void setShowTeaAlert(){
        boolean showTeaAlertBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        showTeaViewModel.setShowteaalert(showTeaAlertBefore);

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsRepository).updateSettings((captor.capture()));
        ActualSettings actualSettingsAfter = captor.getValue();

        assertThat(actualSettingsAfter.isShowTeaAlert()).isEqualTo(showTeaAlertBefore);
    }
}