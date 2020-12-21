package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.tooltip.Tooltip;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;


public class NewTea extends AppCompatActivity implements View.OnLongClickListener, Printer {

    private enum Variety {
        BLACK_TEA, GREEN_TEA, YELLOW_TEA, WHITE_TEA, OOLONG_TEA, PU_ERH_TEA,
        HERBAL_TEA, FRUIT_TEA, ROOIBUS_TEA, OTHER
    }

    private Variety variety = Variety.BLACK_TEA;
    private String amountUnit = "Ts";


    private TextView textViewTeaSort;
    private Spinner spinnerTeaVariety;
    private CheckBox checkboxTeaSort;
    private EditText editTextTeaSort;
    private Button buttonColor;
    private ButtonColorShape buttonColorShape;
    private EditText editTextName;
    private EditText editTextTemperature;
    private ImageButton buttonShowCoolDowntime;
    private EditText editTextCoolDownTime;
    private ImageButton buttonAutofillCoolDownTime;
    private EditText editTextSteepingTime;
    private EditText editTextAmount;
    private Spinner spinnerAmount;
    private TextView textViewInfusion;
    private ImageButton leftArrow;
    private ImageButton rightArrow;
    private ImageButton deleteInfusion;
    private ImageButton addInfusion;

    private NewTeaViewModel newTeaViewModel;
    private InputValidator inputValidator;
    private long teaId;
    private boolean showTea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tea);
        hideKeyboardAtFirst();
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        declareViewElements();
        initializeSpinnerWithBigCharacters();

        initializeNewOrEditTea();

        spinnerTeaVariety.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                varietyChanged(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //the method needs to be overwritten but shouldn't do anything
            }
        });

        checkboxTeaSort.setOnCheckedChangeListener((buttonView, isChecked) -> changeVarietyByHand(isChecked));

        buttonColor.setOnClickListener(view -> createColorPicker());
        buttonColor.setOnLongClickListener(this);

        spinnerAmount.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                amountUnitChanged(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //the method needs to be overwritten but shouldn't do anything
            }
        });

        leftArrow.setOnClickListener(v -> navigateToPreviousInfusion());
        leftArrow.setOnLongClickListener(this);

        rightArrow.setOnClickListener(v -> navigateToNextInfusion());
        rightArrow.setOnLongClickListener(this);

        deleteInfusion.setOnClickListener(v -> deleteInfusion());
        deleteInfusion.setOnLongClickListener(this);

        addInfusion.setOnClickListener(v -> addInfusion());
        addInfusion.setOnLongClickListener(this);

        buttonShowCoolDowntime.setOnClickListener(v -> visualizeCoolDownTimeInput());
        buttonShowCoolDowntime.setOnLongClickListener(this);

        buttonAutofillCoolDownTime.setOnClickListener(v -> autofillCoolDownTime());
        buttonAutofillCoolDownTime.setOnLongClickListener(this);

        //showTea wird übergeben, falls die Navigation von showTea erfolgt
        showTea = this.getIntent().getBooleanExtra("showTea", false);
        inputValidator = new InputValidator(getApplication(), this);
    }

    private void hideKeyboardAtFirst() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.newtea_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void declareViewElements() {
        textViewTeaSort = findViewById(R.id.textViewTeaVariety);
        spinnerTeaVariety = findViewById(R.id.spinnerTeaVariety);
        checkboxTeaSort = findViewById(R.id.checkBoxSelfInput);
        editTextTeaSort = findViewById(R.id.editTextSelfInput);
        buttonColor = findViewById(R.id.buttonColor);
        buttonColorShape = new ButtonColorShape(buttonColor.getBackground(), getApplication());
        editTextName = findViewById(R.id.editTextName);
        editTextTemperature = findViewById(R.id.editTextTemperature);
        buttonShowCoolDowntime = findViewById(R.id.buttonShowCoolDownTime);
        editTextCoolDownTime = findViewById(R.id.editTextCoolDownTime);
        buttonAutofillCoolDownTime = findViewById(R.id.buttonAutofillCoolDownTime);
        editTextSteepingTime = findViewById(R.id.editTextTime);
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerAmount = findViewById(R.id.spinnerAmountUnit);
        textViewInfusion = findViewById(R.id.textViewCountInfusion);
        leftArrow = findViewById(R.id.buttonArrowLeft);
        //workaround enable=false didn't work for imageButton in xml
        leftArrow.setEnabled(false);
        rightArrow = findViewById(R.id.buttonArrowRight);
        //workaround enable=false didn't work for imageButton in xml
        rightArrow.setEnabled(false);
        deleteInfusion = findViewById(R.id.buttonDeleteInfusion);
        addInfusion = findViewById(R.id.buttonAddInfusion);
    }

    private void initializeSpinnerWithBigCharacters() {
        initializeSpinnerWithBigCharacters(R.array.variety_teas, R.layout.spinner_item_varietyoftea, R.layout.spinner_dropdown_item_varietyoftea, spinnerTeaVariety);

        initializeSpinnerWithBigCharacters(R.array.newtea_amount, R.layout.spinner_item_amount_unit, R.layout.spinner_dropdown_item_amount_unit, spinnerAmount);
    }

    private void initializeSpinnerWithBigCharacters(int texts, int spinnerItemLayout, int dropdownItemLayout, Spinner spinner) {
        ArrayAdapter<CharSequence> spinnerVarietyAdapter = ArrayAdapter.createFromResource(
                this, texts, spinnerItemLayout);

        spinnerVarietyAdapter.setDropDownViewResource(dropdownItemLayout);
        spinner.setAdapter(spinnerVarietyAdapter);
    }

    private void initializeNewOrEditTea() {
        teaId = this.getIntent().getLongExtra("teaId", 0);
        if (teaId == 0) {
            initializeNewTea();
        } else {
            initializeEditTea();
        }
    }

    private void initializeNewTea() {
        newTeaViewModel = new NewTeaViewModel(getApplication());
    }

    private void initializeEditTea() {
        newTeaViewModel = new NewTeaViewModel(teaId, getApplication());

        fillVarietyInputFields();

        buttonColorShape.setColor(newTeaViewModel.getColor());

        editTextName.setText(newTeaViewModel.getName());

        fillAmountUnitInput();

        fillAmount();

        refreshInfusionBar();
    }

    private void fillVarietyInputFields() {
        // get the right spinner id
        int spinnerId = -1;
        String[] spinnerElements = getResources().getStringArray(R.array.variety_codes);

        for (int i = 0; i < spinnerElements.length; i++) {
            if (spinnerElements[i].equals(newTeaViewModel.getVariety())) {
                spinnerId = i;
                break;
            }
        }

        // if it is not a standard variety
        if (spinnerId == -1) {
            spinnerTeaVariety.setVisibility(View.INVISIBLE);
            spinnerTeaVariety.setSelection(spinnerElements.length - 1);
            textViewTeaSort.setVisibility(View.INVISIBLE);
            checkboxTeaSort.setVisibility(View.VISIBLE);
            checkboxTeaSort.setChecked(true);
            editTextTeaSort.setVisibility(View.VISIBLE);
            editTextTeaSort.setText(newTeaViewModel.getVariety());
        } else {
            spinnerTeaVariety.setSelection(spinnerId);
        }
    }

    private void fillAmountUnitInput() {
        amountUnit = newTeaViewModel.getAmountkind();
        if ("Ts".equals(amountUnit)) {
            spinnerAmount.setSelection(0);
        } else if ("Gr".equals(amountUnit)) {
            spinnerAmount.setSelection(1);
        }
    }

    private void fillAmount() {
        if (newTeaViewModel.getAmount() != -500) {
            editTextAmount.setText(String.valueOf(newTeaViewModel.getAmount()));
        }
    }

    private void refreshInfusionBar() {
        textViewInfusion.setText(getResources().getString(R.string.newtea_count_infusion, newTeaViewModel.getInfusionIndex() + 1));

        refreshInfusionBarInputFields();

        showDeleteInfusion();
        showAddInfusion();
        showPreviousInfusion();
        showNextInfusion();
    }

    private void refreshInfusionBarInputFields() {
        fillTemperatureInput();

        fillCoolDownTimeInput();

        fillInfusionTimeInput();
    }

    private void fillTemperatureInput() {
        if (newTeaViewModel.getInfusionTemperature() != -500) {
            editTextTemperature.setText(String.valueOf(newTeaViewModel.getInfusionTemperature()));
        } else {
            editTextTemperature.setText("");
        }
    }

    private void fillCoolDownTimeInput() {
        if (newTeaViewModel.getInfusionCooldowntime() != null) {
            editTextCoolDownTime.setText(newTeaViewModel.getInfusionCooldowntime());
        } else {
            editTextCoolDownTime.setText("");
        }
    }

    private void fillInfusionTimeInput() {
        if (newTeaViewModel.getInfusionTime() != null) {
            editTextSteepingTime.setText(newTeaViewModel.getInfusionTime());
        } else {
            editTextSteepingTime.setText("");
        }
    }

    private void showDeleteInfusion() {
        if (newTeaViewModel.getInfusionSize() > 1) {
            deleteInfusion.setVisibility(View.VISIBLE);
        } else {
            deleteInfusion.setVisibility(View.GONE);
        }
    }

    private void showAddInfusion() {
        if (((newTeaViewModel.getInfusionIndex() + 1) == newTeaViewModel.getInfusionSize()) && (newTeaViewModel.getInfusionSize() < 20)) {
            addInfusion.setVisibility(View.VISIBLE);
        } else {
            addInfusion.setVisibility(View.GONE);
        }
    }

    private void showPreviousInfusion() {
        leftArrow.setEnabled(newTeaViewModel.getInfusionIndex() != 0);
    }

    private void showNextInfusion() {
        rightArrow.setEnabled((newTeaViewModel.getInfusionIndex() + 1) != newTeaViewModel.getInfusionSize());
    }

    private void varietyChanged(int position) {
        variety = Variety.values()[position];

        buttonColorShape.setColorByVariety(variety.ordinal());

        if (Variety.OTHER.equals(variety)) {
            checkboxTeaSort.setVisibility(View.VISIBLE);
        } else {
            checkboxTeaSort.setVisibility(View.INVISIBLE);
        }
        sethints();
    }

    private void changeVarietyByHand(boolean isChecked) {
        if (isChecked) {
            textViewTeaSort.setVisibility(View.INVISIBLE);
            spinnerTeaVariety.setVisibility(View.INVISIBLE);
            editTextTeaSort.setVisibility(View.VISIBLE);
        } else {
            textViewTeaSort.setVisibility(View.VISIBLE);
            spinnerTeaVariety.setVisibility(View.VISIBLE);
            editTextTeaSort.setVisibility(View.INVISIBLE);
        }
    }

    private void createColorPicker() {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(NewTea.this, buttonColorShape.getColor());
        colorPickerDialog.setTitle(getResources().getString(R.string.newtea_color_dialog_title));
        colorPickerDialog.setOnColorChangedListener(color -> buttonColorShape.setColor(color));
        colorPickerDialog.show();
    }

    private void amountUnitChanged(int position) {
        if (position == 0) {
            amountUnit = "Ts";
        } else if (position == 1) {
            amountUnit = "Gr";
        }
        sethints();
    }

    private void navigateToPreviousInfusion() {
        if (changeInfusions()) {
            newTeaViewModel.previousInfusion();
            refreshInfusionBar();
        }
    }

    private void navigateToNextInfusion() {
        if (changeInfusions()) {
            newTeaViewModel.nextInfusion();
            refreshInfusionBar();
        }
    }

    private void deleteInfusion() {
        newTeaViewModel.deleteInfusion();
        refreshInfusionBar();
    }

    private void addInfusion() {
        if (changeInfusions()) {
            newTeaViewModel.addInfusion();
            refreshInfusionBar();
        }
    }

    private void visualizeCoolDownTimeInput() {
        if (editTextCoolDownTime.getVisibility() == View.VISIBLE) {
            buttonShowCoolDowntime.setImageResource(R.drawable.arrowdown);
            editTextCoolDownTime.setVisibility(View.GONE);
            buttonAutofillCoolDownTime.setVisibility(View.GONE);
        } else {
            buttonShowCoolDowntime.setImageResource(R.drawable.arrowup);
            editTextCoolDownTime.setVisibility(View.VISIBLE);
            buttonAutofillCoolDownTime.setVisibility(View.VISIBLE);
        }
    }

    private void autofillCoolDownTime() {
        String temperatureInput = editTextTemperature.getText().toString();
        String temperatureUnit = newTeaViewModel.getTemperatureunit();

        //Ist die Temperatur nicht gesetzt, so ist sie -500
        int temperatureCelsius = -500;
        boolean temperatureValid = inputValidator.temperatureInputIsValid(temperatureInput, temperatureUnit);
        if (temperatureValid && !"".equals(temperatureInput)) {
            temperatureCelsius = Integer.parseInt(temperatureInput);
        }
        //Falls nötig in Celsius umwandeln
        if ("Fahrenheit".equals(temperatureUnit)) {
            temperatureCelsius = TemperatureConversation.fahrenheitToCelsius(temperatureCelsius);
        }
        if (temperatureCelsius != -500 && temperatureCelsius != 100) {
            editTextCoolDownTime.setText(TemperatureConversation.celsiusToCoolDownTime(temperatureCelsius));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_auto_cooldown_time, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_tea, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            addTea();
        } else if (id == android.R.id.home) {
            if (showTea) {
                navigateToShowTeaActivity();
                return true;
            } else {
                NavUtils.navigateUpFromSameTask(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTea() {
        String nameInput = editTextName.getText().toString();
        String varietyInput = getVarietyInput();
        String amountInput = editTextAmount.getText().toString();

        if (inputIsValid(nameInput, varietyInput, amountInput)) {
            createOrEditTea(varietyInput, nameInput, parseInteger(amountInput));
            navigateToMainOrShowTea();
        }
    }

    private String getVarietyInput() {
        if (checkboxTeaSort.isChecked()) {
            return editTextTeaSort.getText().toString();
        } else {
            return (String) spinnerTeaVariety.getSelectedItem();
        }
    }

    private boolean inputIsValid(String nameInput, String varietyInput, String amountInput) {
        return inputValidator.nameIsNotEmpty(nameInput)
                && inputValidator.nameIsValid(nameInput)
                && inputValidator.varietyIsValid(varietyInput)
                && inputValidator.amountIsValid(amountInput)
                && changeInfusions();
    }

    private int parseInteger(String amountInput) {
        if ("".equals(amountInput)) {
            return -500;
        } else {
            return Integer.parseInt(amountInput);
        }
    }

    private void createOrEditTea(String sortOfTea, String name, int amount) {
        if (teaId != 0) {
            newTeaViewModel.editTea(name, sortOfTea, amount, amountUnit, buttonColorShape.getColor());
        } else {
            newTeaViewModel.createNewTea(name, sortOfTea, amount, amountUnit, buttonColorShape.getColor());
        }
    }

    private void navigateToMainOrShowTea() {
        if (!showTea) {
            finish();
        } else {
            navigateToShowTeaActivity();
        }
    }

    private void navigateToShowTeaActivity() {
        Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
        showteaScreen.putExtra("teaId", newTeaViewModel.getTeaId());
        startActivity(showteaScreen);
        finish();
    }

    private void sethints() {
        //set Hint for variety
        editTextTemperature.setHint(HintConversation.getHintTemperature(variety.ordinal(), newTeaViewModel.getTemperatureunit(), getApplicationContext()));
        editTextAmount.setHint(HintConversation.getHintAmount(variety.ordinal(), amountUnit, getApplicationContext()));
        editTextSteepingTime.setHint(HintConversation.getHintTime(variety.ordinal(), getApplicationContext()));
    }

    private boolean changeInfusions() {
        String temperatureInput = editTextTemperature.getText().toString();
        String temperatureUnit = newTeaViewModel.getTemperatureunit();
        String coolDownTimeInput = editTextCoolDownTime.getText().toString();
        String timeInput = editTextSteepingTime.getText().toString();

        if (inputValidator.infusionIsValid(temperatureInput, temperatureUnit, coolDownTimeInput, timeInput)) {
            newTeaViewModel.takeInfusionInformation(parseTextInput(timeInput), parseTextInput(coolDownTimeInput), parseInteger(temperatureInput));
            return true;
        }
        return false;
    }

    private String parseTextInput(String textInput) {
        if ("".equals(textInput)) {
            return null;
        }
        return textInput;
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.buttonColor) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_choosecolor));
        } else if (view.getId() == R.id.buttonArrowLeft) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_arrowleft));
        } else if (view.getId() == R.id.buttonArrowRight) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_arrowright));
        } else if (view.getId() == R.id.buttonAddInfusion) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_addinfusion));
        } else if (view.getId() == R.id.buttonDeleteInfusion) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_deleteinfusion));
        } else if (view.getId() == R.id.buttonShowCoolDownTime) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_showcooldowntime));
        } else if (view.getId() == R.id.buttonAutofillCoolDownTime) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.newtea_tooltip_autofillcooldowntime));
        }
        return true;
    }

    private void showTooltip(View v, int gravity, String text) {
        new Tooltip.Builder(v)
                .setText(text)
                .setTextColor(getResources().getColor(R.color.white))
                .setGravity(gravity)
                .setCornerRadius(8f)
                .setCancelable(true)
                .setDismissOnClick(true)
                .show();
    }

    @Override
    public void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
