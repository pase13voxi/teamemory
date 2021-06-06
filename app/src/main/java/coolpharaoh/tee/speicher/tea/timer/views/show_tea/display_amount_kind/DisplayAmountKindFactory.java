package coolpharaoh.tee.speicher.tea.timer.views.show_tea.display_amount_kind;

import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;

public class DisplayAmountKindFactory {

    private DisplayAmountKindFactory() {
    }

    public static DisplayAmountKind getDisplayAmountKind(final AmountKind amountKind) {
        switch (amountKind) {
            case GRAM:
                return new DisplayAmountKindGram();
            case TEA_BAG:
                return new DisplayAmountKindTeaBag();
            default:
                return new DisplayAmountKindTeaSpoon();
        }
    }
}
