package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

// sonar does not recognize the usage because of lombok
@SuppressWarnings("java:S1068")
@Getter
@Setter
public class TeaPOJO {
    private String name;
    private String variety;
    private double amount;
    private String amountKind;
    private int color;
    private int rating;
    private boolean favorite;
    private int nextInfusion;
    private Date date;
    private List<InfusionPOJO> infusions;
    private List<CounterPOJO> counters;
    private List<NotePOJO> notes;
}
