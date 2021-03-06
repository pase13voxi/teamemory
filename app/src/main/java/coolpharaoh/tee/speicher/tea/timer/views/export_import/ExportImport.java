package coolpharaoh.tee.speicher.tea.timer.views.export_import;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOFactory;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.DatabaseJsonTransformer;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class ExportImport extends AppCompatActivity implements Printer {
    private boolean keepStoredTeas = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_import);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        final Button buttonExport = findViewById(R.id.button_export_import_export);
        buttonExport.setOnClickListener(v -> chooseExportFolder());

        final Button buttonImport = findViewById(R.id.button_export_import_import);
        buttonImport.setOnClickListener(v -> dialogImportDecision());

        showWarning();
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView mToolbarCustomTitle = findViewById(R.id.tool_bar_title);
        mToolbarCustomTitle.setText(R.string.export_import_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void chooseExportFolder() {
        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, DatabaseJsonTransformer.WRITE_REQUEST_CODE);
    }

    private void dialogImportDecision() {
        if (CurrentSdk.getSdkVersion() >= Build.VERSION_CODES.O) {
            final ViewGroup parent = findViewById(R.id.exportimport_parent);

            final LayoutInflater inflater = getLayoutInflater();
            final View layoutDialogImport = inflater.inflate(R.layout.dialog_import, parent, false);

            final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.dialog_theme).create();
            alertDialog.setView(layoutDialogImport);
            alertDialog.setTitle(R.string.export_import_import_dialog_header);

            final Button buttonImportDelete = layoutDialogImport.findViewById(R.id.button_export_import_import_delete);
            buttonImportDelete.setOnClickListener(view -> chooseImportFile(alertDialog, false));
            final Button buttonImportKeep = layoutDialogImport.findViewById(R.id.button_export_import_import_keep);
            buttonImportKeep.setOnClickListener(view -> chooseImportFile(alertDialog, true));

            alertDialog.show();
        }
    }

    private void chooseImportFile(AlertDialog alertDialog, boolean keepTeas) {
        dialogChooseImportFile();
        keepStoredTeas = keepTeas;
        alertDialog.cancel();
    }

    private void dialogChooseImportFile() {
        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent,
                getApplicationContext().getResources().getString(R.string.export_import_import_choose_file)), DatabaseJsonTransformer.READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
        if (requestCode == DatabaseJsonTransformer.WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
            exportFile(resultData.getData());
        } else if (requestCode == DatabaseJsonTransformer.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
            importFile(resultData.getData());
        }

        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private void exportFile(final Uri folderPath) {
        JsonIOAdapter.init(getApplication(), this);
        if (JsonIOAdapter.write(DataIOFactory.getDataIO(getApplication(), this, folderPath))) {
            dialogExportLocation();
        } else {
            dialogExportFailed();
        }
    }

    private void importFile(final Uri filePath) {
        JsonIOAdapter.init(getApplication(), this);
        if (JsonIOAdapter.read(DataIOFactory.getDataIO(getApplication(), this, filePath), keepStoredTeas)) {
            dialogImportComplete();
        } else {
            dialogImportFailed();
        }
    }

    private void dialogExportLocation() {
        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.export_import_location_dialog_header)
                .setMessage(getString(R.string.export_import_location_dialog_description))
                .setPositiveButton(R.string.export_import_location_dialog_ok, null)
                .show();
    }

    private void dialogExportFailed() {
        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.export_import_export_failed_dialog_header)
                .setMessage(R.string.export_import_export_failed_dialog_description)
                .setPositiveButton(R.string.export_import_export_failed_dialog_ok, null)
                .show();
    }

    private void dialogImportComplete() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.export_import_import_complete_dialog_header);
        if (keepStoredTeas) {
            builder.setMessage(R.string.export_import_import_complete_keep_dialog_description).setPositiveButton(R.string.export_import_import_complete_dialog_ok, null).show();
        } else {
            builder.setMessage(R.string.export_import_import_complete_delete_dialog_description).setPositiveButton(R.string.export_import_import_complete_dialog_ok, null).show();
        }
    }

    private void dialogImportFailed() {
        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.export_import_import_failed_dialog_header)
                .setMessage(R.string.export_import_import_failed_dialog_description)
                .setPositiveButton(R.string.export_import_import_failed_dialog_ok, null)
                .show();
    }

    private void showWarning() {
        if (CurrentSdk.getSdkVersion() < Build.VERSION_CODES.O) {
            final TextView textViewWarning = findViewById(R.id.text_view_export_import_warning);
            textViewWarning.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
