package coolpharaoh.tee.speicher.tea.timer.views.show_tea;

import android.app.Application;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter;
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.FAHRENHEIT;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.TEA_SPOON;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.OOLONG_TEA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowTeaViewModelTest {
    public static final String CURRENT_DATE = "2020-08-19T10:15:30Z";
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
    CounterRepository counterRepository;
    @Mock
    ActualSettingsRepository actualSettingsRepository;
    @Mock
    DateUtility dateUtility;

    private static final long TEA_ID = 1L;

    @Before
    public void setUp() {
        showTeaViewModel = new ShowTeaViewModel(TEA_ID, application, teaRepository, infusionRepository,
                counterRepository, actualSettingsRepository);
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
    public void getName() {
        String teaNameBefore = "TEA";

        Tea tea = new Tea();
        tea.setName(teaNameBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        String teaNameAfter = showTeaViewModel.getName();

        assertThat(teaNameAfter).isEqualTo(teaNameBefore);
    }

    @Test
    public void getVariety() {
        String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        Tea tea = new Tea();
        tea.setVariety(OOLONG_TEA.getCode());
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varietyTeas);

        String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo(varietyTeas[OOLONG_TEA.getChoice()]);
    }

    @Test
    public void getUnkownVariety() {
        String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        String varietyBefore = "VARIETY";

        Tea tea = new Tea();
        tea.setVariety(varietyBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varietyTeas);

        String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo(varietyBefore);
    }

    @Test
    public void getEmptyVariety() {
        String varietyBefore = "";

        Tea tea = new Tea();
        tea.setVariety(varietyBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        String varietyAfter = showTeaViewModel.getVariety();

        assertThat(varietyAfter).isEqualTo("-");
    }

    @Test
    public void getAmount() {
        double amountBefore = 1;

        Tea tea = new Tea();
        tea.setAmount(amountBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        double amountAfter = showTeaViewModel.getAmount();

        assertThat(amountAfter).isEqualTo(amountBefore);
    }

    @Test
    public void getAmountKind() {
        final Tea tea = new Tea();
        tea.setAmountKind(TEA_SPOON.getText());
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final AmountKind amountKind = showTeaViewModel.getAmountKind();

        assertThat(amountKind).isEqualTo(TEA_SPOON);
    }

    @Test
    public void getColor() {
        int colorBefore = 1;

        Tea tea = new Tea();
        tea.setColor(colorBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        int colorAfter = showTeaViewModel.getColor();

        assertThat(colorAfter).isEqualTo(colorBefore);
    }

    @Test
    public void setCurrentDate() {
        Date fixedDate = mockFixedDate();
        Tea teaBefore = new Tea();
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(teaBefore);

        showTeaViewModel.setCurrentDate();

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((captor.capture()));
        Tea teaAfter = captor.getValue();

        assertThat(teaAfter.getDate()).isEqualTo(fixedDate);
    }

    @Test
    public void getNextInfusion() {
        int nextInfusionBefore = 0;

        Tea tea = new Tea();
        tea.setNextInfusion(nextInfusionBefore);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        int nextInfusionAfter = showTeaViewModel.getNextInfusion();

        assertThat(nextInfusionAfter).isEqualTo(nextInfusionBefore);
    }

    @Test
    public void updateNextInfusion() {
        Tea tea = new Tea();
        tea.setNextInfusion(0);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(Arrays.asList(new Infusion(), new Infusion()));

        showTeaViewModel.updateNextInfusion();

        ArgumentCaptor<Tea> teaCaptor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((teaCaptor.capture()));
        Tea updatedTea = teaCaptor.getValue();

        assertThat(updatedTea.getNextInfusion()).isEqualTo(1);
    }

    @Test
    public void resetNextInfusion() {
        Tea tea = new Tea();
        tea.setNextInfusion(2);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        showTeaViewModel.resetNextInfusion();

        ArgumentCaptor<Tea> teaCaptor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea((teaCaptor.capture()));
        Tea updatedTea = teaCaptor.getValue();

        assertThat(updatedTea.getNextInfusion()).isZero();
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

        assertThat(lastInfusionAfter.getNextInfusion()).isZero();
    }

    @Test
    public void navigateBetweenInfusions() {
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
        assertThat(showTeaViewModel.getInfusionIndex()).isZero();

        TimeConverter time1 = showTeaViewModel.getTime();
        TimeConverter cooldownTime1 = showTeaViewModel.getCoolDownTime();
        int temperature1 = showTeaViewModel.getTemperature();

        assertThat(time1.getTime()).isEqualTo(infusions.get(0).getTime());
        assertThat(time1.getMinutes()).isEqualTo(1);
        assertThat(time1.getSeconds()).isZero();
        assertThat(cooldownTime1.getTime()).isEqualTo(infusions.get(0).getCoolDownTime());
        assertThat(cooldownTime1.getMinutes()).isEqualTo(2);
        assertThat(cooldownTime1.getSeconds()).isZero();
        assertThat(temperature1).isEqualTo(infusions.get(0).getTemperatureCelsius());

        showTeaViewModel.incrementInfusionIndex();
        assertThat(showTeaViewModel.getInfusionIndex()).isEqualTo(1);

        TimeConverter time2 = showTeaViewModel.getTime();
        TimeConverter coolDownTime2 = showTeaViewModel.getCoolDownTime();
        int temperature2 = showTeaViewModel.getTemperature();

        assertThat(time2.getTime()).isEqualTo(infusions.get(1).getTime());
        assertThat(time2.getMinutes()).isEqualTo(2);
        assertThat(time2.getSeconds()).isEqualTo(30);
        assertThat(coolDownTime2.getTime()).isEqualTo(infusions.get(1).getCoolDownTime());
        assertThat(coolDownTime2.getMinutes()).isEqualTo(5);
        assertThat(coolDownTime2.getSeconds()).isEqualTo(30);
        assertThat(temperature2).isEqualTo(infusions.get(1).getTemperatureCelsius());

        actualSettings.setTemperatureUnit(FAHRENHEIT.getText());
        showTeaViewModel.setInfusionIndex(0);
        assertThat(showTeaViewModel.getInfusionIndex()).isZero();

        int temperature3 = showTeaViewModel.getTemperature();
        assertThat(temperature3).isEqualTo(infusions.get(0).getTemperatureFahrenheit());
    }

    @Test
    public void getEmptyTime() {
        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion1 = new Infusion(1L, 1, null, null, 1, 1);
        infusions.add(infusion1);

        when(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions);

        TimeConverter timeAfter = showTeaViewModel.getTime();

        assertThat(timeAfter.getTime()).isNull();
        assertThat(timeAfter.getMinutes()).isZero();
        assertThat(timeAfter.getSeconds()).isZero();
    }

    @Test
    public void countCounter() {
        Date currentDate = mockFixedDate();
        Counter counterBefore = new Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate);
        when(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore);

        showTeaViewModel.countCounter();

        ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository).updateCounter((captor.capture()));
        Counter counterAfter = captor.getValue();

        assertThat(counterAfter)
                .extracting(Counter::getDay, Counter::getWeek, Counter::getMonth, Counter::getOverall)
                .containsExactly(2, 2, 2, 2L);
    }

    @Test
    public void countCounterAndCounterIsNull() {
        showTeaViewModel.countCounter();

        ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository).updateCounter((captor.capture()));
        Counter counterAfter = captor.getValue();

        assertThat(counterAfter)
                .extracting(Counter::getDay, Counter::getWeek, Counter::getMonth, Counter::getOverall)
                .containsExactly(1, 1, 1, 1L);
    }

    @Test
    public void isAnimation() {
        boolean animationBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setAnimation(animationBefore);
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        boolean animationAfter = showTeaViewModel.isAnimation();

        assertThat(animationAfter).isEqualTo(animationBefore);
    }

    @Test
    public void isShowTeaAlert() {
        boolean showTeaAlertBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setShowTeaAlert(showTeaAlertBefore);
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        boolean showTeaAlertAfter = showTeaViewModel.isShowteaAlert();

        assertThat(showTeaAlertAfter).isEqualTo(showTeaAlertBefore);
    }

    @Test
    public void setShowTeaAlert() {
        boolean showTeaAlertBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        showTeaViewModel.setShowteaAlert(showTeaAlertBefore);

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsRepository).updateSettings((captor.capture()));
        ActualSettings actualSettingsAfter = captor.getValue();

        assertThat(actualSettingsAfter.isShowTeaAlert()).isEqualTo(showTeaAlertBefore);
    }

    private Date mockFixedDate() {
        Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        Instant now = Instant.now(clock);
        Date fixedDate = Date.from(now);

        CurrentDate.setFixedDate(dateUtility);
        when(dateUtility.getDate()).thenReturn(fixedDate);
        return fixedDate;
    }
}