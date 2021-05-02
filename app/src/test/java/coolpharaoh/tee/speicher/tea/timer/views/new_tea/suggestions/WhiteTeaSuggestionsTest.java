package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions;

import android.app.Application;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WhiteTeaSuggestionsTest {

    private Suggestions whiteTeaSuggestions;

    @Mock
    Application application;
    @Mock
    Resources resources;

    @Before
    public void setUp() {
        when(application.getResources()).thenReturn(resources);
        whiteTeaSuggestions = new WhiteTeaSuggestions(application);
    }

    @Test
    public void getAmountTsHint() {
        final int[] arrayTs = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_white_tea_amount_ts)).thenReturn(arrayTs);

        assertThat(whiteTeaSuggestions.getAmountTsSuggestions()).isEqualTo(arrayTs);
    }

    @Test
    public void getAmountGrHint() {
        final int[] arrayGr = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_white_tea_amount_gr)).thenReturn(arrayGr);

        assertThat(whiteTeaSuggestions.getAmountGrSuggestions()).isEqualTo(arrayGr);
    }

    @Test
    public void getTemperatureCelsiusHint() {
        final int[] arrayCelsius = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_white_tea_temperature_celsius)).thenReturn(arrayCelsius);

        assertThat(whiteTeaSuggestions.getTemperatureCelsiusSuggestions()).isEqualTo(arrayCelsius);
    }

    @Test
    public void getTemperatureFahrenheitHint() {
        final int[] arrayFahrenheit = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_white_tea_temperature_fahrenheit)).thenReturn(arrayFahrenheit);

        assertThat(whiteTeaSuggestions.getTemperatureFahrenheitSuggestions()).isEqualTo(arrayFahrenheit);
    }

    @Test
    public void getSteepingTimeHint() {
        final String[] arrayTime = new String[]{"1:00", "2:30"};
        when(resources.getStringArray(R.array.new_tea_suggestions_white_tea_time)).thenReturn(arrayTime);

        assertThat(whiteTeaSuggestions.getTimeSuggestions()).isEqualTo(arrayTime);
    }
}