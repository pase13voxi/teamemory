package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;

class WhiteTeaSuggestions implements Suggestions {

    private final Application application;

    WhiteTeaSuggestions(final Application application) {
        this.application = application;
    }

    @Override
    public int[] getAmountTsSuggestions() {
        return application.getResources().getIntArray(R.array.new_tea_suggestions_white_tea_amount_ts);
    }

    @Override
    public int[] getAmountGrSuggestions() {
        return application.getResources().getIntArray(R.array.new_tea_suggestions_white_tea_amount_gr);
    }

    @Override
    public int[] getAmountTbSuggestions() {
        return application.getResources().getIntArray(R.array.new_tea_suggestions_white_tea_amount_tb);
    }

    @Override
    public int[] getTemperatureCelsiusSuggestions() {
        return application.getResources().getIntArray(R.array.new_tea_suggestions_white_tea_temperature_celsius);
    }

    @Override
    public int[] getTemperatureFahrenheitSuggestions() {
        return application.getResources().getIntArray(R.array.new_tea_suggestions_white_tea_temperature_fahrenheit);
    }

    @Override
    public String[] getTimeSuggestions() {
        return application.getResources().getStringArray(R.array.new_tea_suggestions_white_tea_time);
    }
}
