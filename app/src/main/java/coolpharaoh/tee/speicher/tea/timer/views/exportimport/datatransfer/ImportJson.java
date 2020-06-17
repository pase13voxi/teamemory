package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.ExportImportViewModel;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.TeaPOJO;

public class ImportJson {
    public static final int READ_REQUEST_CODE = 8777;

    private final Uri fileUri;
    private String json;

    public ImportJson(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public boolean read(Context context, ExportImportViewModel exportImportViewModel, boolean keepStoredTeas) {
        json = readJsonFile(context);
        List<TeaPOJO> teaList = createTeaListFromJson(context);
        if(teaList == null){
            return false;
        }
        POJOToDatabase pojoToDatabase = new POJOToDatabase(exportImportViewModel);
        pojoToDatabase.fillDatabaseWithTeaList(teaList, keepStoredTeas);
        Toast.makeText(context, R.string.exportimport_teas_imported, Toast.LENGTH_LONG).show();
        return true;
    }

    private String readJsonFile(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     context.getContentResolver().openInputStream(fileUri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private List<TeaPOJO> createTeaListFromJson(Context context) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        Type listType = new TypeToken<ArrayList<TeaPOJO>>() {
        }.getType();

        try{
            return gson.fromJson(json, listType);
        }catch(JsonSyntaxException e){
            Toast.makeText(context, R.string.exportimport_import_parse_teas_failed, Toast.LENGTH_LONG).show();
            return null;
        }
    }
}