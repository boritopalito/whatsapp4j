package nl.xx1.whatsapp4j;

import java.util.List;

public enum AppState {
    OPENING,
    UNLAUNCHED,
    PAIRING,
    UNPAIRED,
    UNPAIRED_IDLE,
    CONNECTED;

    public static List<AppState> getOpeningStates() {
        return List.of(OPENING, PAIRING, UNLAUNCHED);
    }

    public static List<AppState> getUnpairedStates() {
        return List.of(UNPAIRED, UNPAIRED_IDLE);
    }
}
