package gpizza.criteria;

import java.util.ArrayList;
import java.util.List;

public class CriteriaDELETE extends BaseCriteria {
    private List<String> conditions = new ArrayList<>();

    public CriteriaDELETE(String table) {
        super(table);
    }

    public CriteriaDELETE where(String condition, Object value) {
        conditions.add(condition + " = ?");
        parameters.add(value);
        return this;
    }

    @Override
    public String build() {
        if (conditions.isEmpty()) {
            throw new IllegalStateException("Une condition est nécessaire pour un DELETE.");
        }

        return "DELETE FROM " + table + " WHERE " + String.join(" AND ", conditions);
    }
}
