package torn.ando.gpizzasb.gpizza.criteria;

import java.util.ArrayList;
import java.util.List;

public class CriteriaUPDATE {
    private String table;
    private List<String> setClauses = new ArrayList<>();
    private List<Object> parameters = new ArrayList<>();
    private List<String> conditions = new ArrayList<>();

    public CriteriaUPDATE(String table) {
        this.table = table;
        this.conditions.add("1=1");
    }

    public CriteriaUPDATE set(String field, Object value) {
        setClauses.add(field + " = ?");
        parameters.add(value);
        return this;
    }

    public CriteriaUPDATE and(String condition, Object value) {
        conditions.add("AND " + condition + " = ?");
        parameters.add(value);
        return this;
    }

    public CriteriaUPDATE or(String condition, Object value) {
        conditions.add("OR " + condition + " = ?");
        parameters.add(value);
        return this;
    }

    public String build() {
        if (setClauses.isEmpty()) {
            throw new IllegalStateException("No SET clauses defined for UPDATE query");
        }

        StringBuilder query = new StringBuilder("UPDATE ").append(table).append(" SET ");
        query.append(String.join(", ", setClauses));

        if (!conditions.isEmpty()) {
            query.append(" WHERE ").append(String.join(" ", conditions));
        }

        return query.toString();
    }

    public List<Object> getParameters() {
        return parameters;
    }
}
