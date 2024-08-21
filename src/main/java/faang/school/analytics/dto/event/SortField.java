package faang.school.analytics.dto.event;

public enum SortField {

    RECEIVED_AT("receivedAt");

    private String value;

    SortField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
