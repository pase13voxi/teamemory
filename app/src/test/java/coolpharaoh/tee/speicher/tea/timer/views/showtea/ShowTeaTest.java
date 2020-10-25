package coolpharaoh.tee.speicher.tea.timer.views.showtea;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.main.Main;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ShowTeaTest {
    private static final String TEA_ID_EXTRA = "teaId";
    private static final long TEA_ID = 1L;
    private static final String CELSIUS = "Celsius";
    private static final String NOTE = "Any note.";

    Tea tea;
    List<Infusion> infusions;
    Counter counter;
    Note note;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    NoteDao noteDao;
    @Mock
    CounterDao counterDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    @Test
    public void launchActivityWithNoTeaIdAndExpectFailingDialog() {
        ActivityScenario<ShowTea> newTeaActivityScenario = ActivityScenario.launch(ShowTea.class);
        newTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogFail = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogFail, R.string.showtea_dialog_tea_missing_header, R.string.showtea_dialog_tea_missing_description);
            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            Intent expected = new Intent(showTea, Main.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void launchActivityWithNotExistingTeaIdExpectFailingDialog() {
        mockDB();
        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, 5L);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogFail = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogFail, R.string.showtea_dialog_tea_missing_header, R.string.showtea_dialog_tea_missing_description);
            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            Intent expected = new Intent(showTea, Main.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void launchActivityAndExpectDescriptionDialog() {
        mockDB();
        mockTea(TEA_ID);
        mockInfusions(TEA_ID, Collections.singletonList("1:00"), Collections.singletonList(null));
        mockActualSettings(CELSIUS, true);
        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> checkTitleAndMessageOfLatestDialog(showTea, getLatestAlertDialog(), R.string.showtea_dialog_description_header, null));
    }

    @Test
    public void launchActivityWithStandardValuesAndExpectFilledActivity() {
        mockDB();
        mockTea(TEA_ID);
        mockInfusions(TEA_ID, Collections.singletonList("1:00"), Collections.singletonList(null));
        mockActualSettings(CELSIUS, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonInfusionIndex = showTea.findViewById(R.id.toolbar_infusionindex);
            TextView textViewInfusionIndex = showTea.findViewById(R.id.toolbar_text_infusionindex);
            Button buttonNextInfusion = showTea.findViewById(R.id.toolbar_nextinfusion);
            TextView textViewName = showTea.findViewById(R.id.textViewName);
            TextView textViewVariety = showTea.findViewById(R.id.textViewVariety);
            Button buttonNote = showTea.findViewById(R.id.buttonNote);
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);
            TextView textViewAmount = showTea.findViewById(R.id.textViewAmount);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);

            assertThat(buttonInfusionIndex.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewInfusionIndex.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(buttonNextInfusion.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewName.getText()).isEqualTo(tea.getName());
            assertThat(textViewVariety.getText()).isEqualTo(tea.getVariety());
            assertThat(buttonNote.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(buttonExchange.isEnabled()).isFalse();
            assertThat(textViewTemperature.getText()).isEqualTo(infusions.get(0).getTemperatureCelsius() + " °C");
            assertThat(textViewAmount.getText()).isEqualTo(tea.getAmount() + " g/L");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void switchBetweenTimerAndCoolDownTimer() {
        mockDB();
        mockTea(TEA_ID);
        mockInfusions(TEA_ID, Collections.singletonList("1:00"), Collections.singletonList("4:00"));
        mockActualSettings(CELSIUS, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            Button buttonInfo = showTea.findViewById(R.id.buttonInfo);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);

            assertThat(buttonExchange.isEnabled()).isTrue();
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonExchange.performClick();
            assertThat(buttonInfo.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("04");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonInfo.performClick();
            AlertDialog dialogInfo = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogInfo, R.string.showtea_cooldown_header, R.string.showtea_cooldown_description);
            dialogInfo.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            buttonExchange.performClick();
            assertThat(buttonInfo.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void openAndChangeNotesViaButton() {
        mockDB();
        mockTea(TEA_ID);
        mockInfusions(TEA_ID, Collections.singletonList("1:00"), Collections.singletonList(null));
        mockNote(TEA_ID, NOTE);
        mockActualSettings(CELSIUS, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonNote = showTea.findViewById(R.id.buttonNote);

            assertThat(buttonNote.getVisibility()).isEqualTo(View.VISIBLE);

            buttonNote.performClick();
            AlertDialog dialogNote = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogNote, R.string.showtea_action_note, null);

            EditText editTextNote = dialogNote.findViewById(R.id.editTextNote);
            assertThat(editTextNote.getText()).hasToString(NOTE);
            editTextNote.setText("");
            dialogNote.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(buttonNote.getVisibility()).isEqualTo(View.INVISIBLE);
        });
    }

    @Test
    public void openAndChangeNotesViaMenu() {
        mockDB();
        mockTea(TEA_ID);
        mockInfusions(TEA_ID, Collections.singletonList("1:00"), Collections.singletonList(null));
        mockNote(TEA_ID, "");
        mockActualSettings(CELSIUS, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonNote = showTea.findViewById(R.id.buttonNote);

            assertThat(buttonNote.getVisibility()).isEqualTo(View.INVISIBLE);

            showTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_note));
            AlertDialog dialogNote = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogNote, R.string.showtea_action_note, null);

            EditText editTextNote = dialogNote.findViewById(R.id.editTextNote);
            assertThat(editTextNote.getText()).hasToString("");
            editTextNote.setText(NOTE);
            dialogNote.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(buttonNote.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void switchBetweenInfusions() {
        mockDB();
        mockTea(TEA_ID);
        mockInfusions(TEA_ID, Arrays.asList(new String[]{"1:00", "2:00", "3:00"}), Arrays.asList(new String[]{null, "5:00", null}));
        mockActualSettings(CELSIUS, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonInfusionIndex = showTea.findViewById(R.id.toolbar_infusionindex);
            TextView textViewInfusionIndex = showTea.findViewById(R.id.toolbar_text_infusionindex);
            Button buttonNextInfusion = showTea.findViewById(R.id.toolbar_nextinfusion);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);

            assertThat(buttonInfusionIndex.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewInfusionIndex.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(buttonNextInfusion.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(buttonExchange.isEnabled()).isFalse();
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonNextInfusion.performClick();
            assertThat(buttonExchange.isEnabled()).isTrue();
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("02");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonInfusionIndex.performClick();
            AlertDialog dialogInfusionIndex = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogInfusionIndex, R.string.showtea_dialog_infusion_count_title, null);

            ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialogInfusionIndex);
            shadowDialog.clickOnItem(2);
            assertThat(buttonExchange.isEnabled()).isFalse();
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("03");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    private void mockTea(long teaId) {
        tea = new Tea("name", "variety", 1, "Gr", 1, 1, CurrentDate.getDate());
        tea.setId(teaId);
        when(teaDao.getTeaById(teaId)).thenReturn(tea);
    }

    private void mockInfusions(long teaId, List<String> time, List<String> coolDownTime) {
        infusions = new ArrayList<>();
        for (int i = 0; i < time.size(); i++) {
            Infusion infusion = new Infusion(teaId, i, time.get(i), coolDownTime.get(i), 100, 212);
            infusion.setId((long) (i + 1));
            infusions.add(infusion);
        }
        when(infusionDao.getInfusionsByTeaId(teaId)).thenReturn(infusions);
    }

    private void mockCounter(long teaId) {
        counter = new Counter(teaId, 1, 1, 1, 1, CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
        counter.setId(1L);
        when(counterDao.getCounterByTeaId(teaId)).thenReturn(counter);
    }

    private void mockNote(long teaId, String description) {
        note = new Note(teaId, 1, "header", description);
        note.setId(1L);
        when(noteDao.getNoteByTeaId(teaId)).thenReturn(note);
    }

    private void mockActualSettings(String temperatureUnit, boolean dialog) {
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureUnit(temperatureUnit);
        actualSettings.setShowTeaAlert(dialog);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

    private void checkTitleAndMessageOfLatestDialog(ShowTea showTea, AlertDialog dialog, int title, Integer message) {
        ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog).isNotNull();
        assertThat(shadowDialog.getTitle()).isEqualTo(showTea.getString(title));
        if (message != null) {
            assertThat(shadowDialog.getMessage()).isEqualTo(showTea.getString(message));
        }
    }

}
