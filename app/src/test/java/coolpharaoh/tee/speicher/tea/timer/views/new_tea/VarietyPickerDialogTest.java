package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;

import static android.os.Looper.getMainLooper;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.OTHER;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.YELLOW_TEA;
import static coolpharaoh.tee.speicher.tea.timer.views.new_tea.AmountPickerDialog.TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class VarietyPickerDialogTest {
    private static final String YELLOW_TEA_TEXT = "Yellow tea";
    private static final String CUSTOM_VARIETY = "Custom Variety";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    NewTeaViewModel newTeaViewModel;

    private VarietyPickerDialog dialogFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        final FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        dialogFragment = new VarietyPickerDialog(newTeaViewModel);
    }

    @Test
    public void showDialogAndExpectTitle() {
        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_variety_header));
    }

    @Test
    public void selectYellowTeaAndExpectSavedYellowTea() {
        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final List<RadioButton> radioButtons = getRadioButtons(dialog);

        radioButtons.get(YELLOW_TEA.getChoice()).performClick();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        shadowOf(getMainLooper()).idle();

        verify(newTeaViewModel).setVariety(YELLOW_TEA_TEXT);
    }

    @Test
    public void selectYellowTeaAndExpectSavedColorForYellowTea() {
        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final List<RadioButton> radioButtons = getRadioButtons(dialog);

        radioButtons.get(YELLOW_TEA.getChoice()).performClick();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        shadowOf(getMainLooper()).idle();

        verify(newTeaViewModel).setColor(-15797);
    }

    @Test
    public void showAndHideCustomVarityInputField() {
        when(newTeaViewModel.getVarietyAsText()).thenReturn(YELLOW_TEA_TEXT);
        when(newTeaViewModel.getVariety()).thenReturn(YELLOW_TEA);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final EditText editTextCustomVariety = dialog.findViewById(R.id.edit_text_new_tea_custom_variety);
        assertThat(editTextCustomVariety.getVisibility()).isEqualTo(View.GONE);

        final List<RadioButton> radioButtons = getRadioButtons(dialog);
        radioButtons.get(OTHER.getChoice()).performClick();
        assertThat(editTextCustomVariety.getVisibility()).isEqualTo(View.VISIBLE);

        radioButtons.get(YELLOW_TEA.getChoice()).performClick();
        assertThat(editTextCustomVariety.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void inputCustomVarietyAndExpectSavedCustomVariety() {
        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final List<RadioButton> radioButtons = getRadioButtons(dialog);

        radioButtons.get(OTHER.getChoice()).performClick();

        final EditText editTextCustomVariety = dialog.findViewById(R.id.edit_text_new_tea_custom_variety);
        editTextCustomVariety.setText(CUSTOM_VARIETY);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        shadowOf(getMainLooper()).idle();

        verify(newTeaViewModel).setVariety(CUSTOM_VARIETY);
    }

    @Test
    public void showExistingVarietyConfiguration() {
        when(newTeaViewModel.getVarietyAsText()).thenReturn(YELLOW_TEA_TEXT);
        when(newTeaViewModel.getVariety()).thenReturn(YELLOW_TEA);

        dialogFragment.show(fragmentManager, TemperaturePickerDialog.TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();

        final List<RadioButton> radioButtons = getRadioButtons(dialog);
        assertThat(radioButtons.get(YELLOW_TEA.getChoice()).isChecked()).isTrue();
    }

    @Test
    public void showExistingCustomVarietyConfiguration() {
        when(newTeaViewModel.getVariety()).thenReturn(OTHER);
        when(newTeaViewModel.getVarietyAsText()).thenReturn(CUSTOM_VARIETY);

        dialogFragment.show(fragmentManager, TemperaturePickerDialog.TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();

        final List<RadioButton> radioButtons = getRadioButtons(dialog);
        assertThat(radioButtons.get(OTHER.getChoice()).isChecked()).isTrue();

        final EditText editTextCustomVariety = dialog.findViewById(R.id.edit_text_new_tea_custom_variety);
        assertThat(editTextCustomVariety.getText()).hasToString(CUSTOM_VARIETY);
    }

    private List<RadioButton> getRadioButtons(AlertDialog dialog) {
        final RadioGroup radioGroup = dialog.findViewById(R.id.radio_group_new_tea_variety_input);
        final ArrayList<RadioButton> listRadioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }
}
