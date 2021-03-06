package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.OTHER;

public class VarietyPickerDialog extends DialogFragment {
    public static final String TAG = "VarietyPickerDialog";

    private final NewTeaViewModel newTeaViewModel;
    private View dialogView;

    public VarietyPickerDialog(final NewTeaViewModel newTeaViewModel) {
        this.newTeaViewModel = newTeaViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstancesState) {
        Activity activity = requireActivity();
        ViewGroup parent = activity.findViewById(R.id.new_tea_parent);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_variety_picker, parent, false);

        defineVarietyRadioGroup();

        return new AlertDialog.Builder(activity, R.style.dialog_theme)
                .setView(dialogView)
                .setTitle(R.string.new_tea_dialog_variety_header)
                .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
                .setPositiveButton(R.string.new_tea_dialog_picker_positive, (dialog, which) -> {
                    persistVariety();
                    persistColor();
                })
                .create();
    }

    private void defineVarietyRadioGroup() {
        final String[] varietyList = getResources().getStringArray(R.array.new_tea_variety_teas);
        final RadioGroup varietyRadioGroup = dialogView.findViewById(R.id.radio_group_new_tea_variety_input);

        for (final String variety : varietyList) {
            final RadioButton varietyRadioButton = createRadioButton(variety);
            varietyRadioGroup.addView(varietyRadioButton);
        }

        varietyRadioGroup.setOnCheckedChangeListener(this::showCustomVariety);

        setConfiguredValues(varietyRadioGroup, varietyList);
    }

    private void setConfiguredValues(final RadioGroup varietyRadioGroup, final String[] varietyList) {
        final String varietyAsText = newTeaViewModel.getVarietyAsText();
        final int varietyIndex = Arrays.asList(varietyList).indexOf(varietyAsText);
        final List<RadioButton> radioButtons = getRadioButtons(varietyRadioGroup);

        if (varietyIndex == -1) {
            radioButtons.get(OTHER.getChoice()).setChecked(true);
            final EditText editTextCustomVariety = dialogView.findViewById(R.id.edit_text_new_tea_custom_variety);
            editTextCustomVariety.setText(varietyAsText);
        } else {
            final Variety variety = newTeaViewModel.getVariety();
            radioButtons.get(variety.getChoice()).setChecked(true);
        }
    }

    private List<RadioButton> getRadioButtons(final RadioGroup radioGroup) {
        final ArrayList<RadioButton> listRadioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }

    private RadioButton createRadioButton(final String variety) {
        final RadioButton varietyRadioButton = new RadioButton(getActivity());
        varietyRadioButton.setText(variety);

        final ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}  // checked
                },
                new int[]{
                        ContextCompat.getColor(getActivity().getApplication(), R.color.background_grey),
                        ContextCompat.getColor(getActivity().getApplication(), R.color.background_green)
                }
        );
        varietyRadioButton.setButtonTintList(colorStateList);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dpToPixel(20), dpToPixel(10), 0, 0);
        varietyRadioButton.setLayoutParams(params);
        varietyRadioButton.setPadding(dpToPixel(15), 0, 0, 0);
        varietyRadioButton.setTextSize(16);
        return varietyRadioButton;
    }

    private int dpToPixel(int dpValue) {
        final float density = getActivity().getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * density); // margin in pixels
    }

    private void showCustomVariety(final RadioGroup radioGroup, final int checkedId) {
        final String[] varietyList = getResources().getStringArray(R.array.new_tea_variety_teas);
        final RadioButton radioButton = radioGroup.findViewById(checkedId);
        final EditText editTextCustomVariety = dialogView.findViewById(R.id.edit_text_new_tea_custom_variety);

        if (varietyList[OTHER.getChoice()].equals(radioButton.getText().toString())) {
            editTextCustomVariety.setVisibility(View.VISIBLE);
        } else {
            editTextCustomVariety.setVisibility(View.GONE);
        }
    }

    private void persistVariety() {
        final EditText editTextCustomVariety = dialogView.findViewById(R.id.edit_text_new_tea_custom_variety);

        if (editTextCustomVariety.getVisibility() == View.VISIBLE &&
                !editTextCustomVariety.getText().toString().isEmpty()) {
            newTeaViewModel.setVariety(editTextCustomVariety.getText().toString());
        } else {
            final RadioGroup varietyRadioGroup = dialogView.findViewById(R.id.radio_group_new_tea_variety_input);
            final RadioButton radioButton = varietyRadioGroup.findViewById(varietyRadioGroup.getCheckedRadioButtonId());

            newTeaViewModel.setVariety(radioButton.getText().toString());
        }
    }

    private void persistColor() {
        final String[] varietyList = getResources().getStringArray(R.array.new_tea_variety_teas);
        final RadioGroup varietyRadioGroup = dialogView.findViewById(R.id.radio_group_new_tea_variety_input);
        final RadioButton radioButton = varietyRadioGroup.findViewById(varietyRadioGroup.getCheckedRadioButtonId());

        final int varietyIndex = Arrays.asList(varietyList).indexOf(radioButton.getText().toString());
        final int varietyColor = ContextCompat.getColor(getActivity().getApplication(), Variety.fromChoice(varietyIndex).getColor());

        newTeaViewModel.setColor(varietyColor);
    }
}
