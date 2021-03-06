package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayTemperatureUnitFactoryTest {
    @Test
    public void getDisplayTemperatureUnitFahrenheit() {
        final DisplayTemperatureUnitStrategy displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory.get(TemperatureUnit.FAHRENHEIT, null);
        assertThat(displayTemperatureUnitStrategy).isInstanceOf(DisplayTemperatureUnitStrategyFahrenheit.class);
    }

    @Test
    public void getDisplayTemperatureUnitCelsius() {
        final DisplayTemperatureUnitStrategy displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory.get(TemperatureUnit.CELSIUS, null);
        assertThat(displayTemperatureUnitStrategy).isInstanceOf(DisplayTemperatureUnitStrategyCelsius.class);
    }
}
