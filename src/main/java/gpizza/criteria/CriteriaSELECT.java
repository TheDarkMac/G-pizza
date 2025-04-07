package gpizza.criteria;

import java.util.ArrayList;
import java.util.List;

public class CriteriaSELECT {
    private String table;
    private List<String> fields = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private List<String> conditions = new ArrayList<>();
    private List<Object> parameters = new ArrayList<>();
    private List<String> orderBy = new ArrayList<>();
    private Integer limit;
    private Integer offset;

    public CriteriaSELECT(String table) {
        this.table = table;
        this.conditions.add("1=1");
    }

    public CriteriaSELECT select(String... fields) {
        this.fields.clear();
        for (String field : fields) {
            this.fields.add(field);
        }
        return this;
    }

    public CriteriaSELECT join(String joinType, String table, String condition) {
        joins.add(joinType + " JOIN " + table + " ON " + condition);
        return this;
    }

    public CriteriaSELECT and(String condition) {
        conditions.add("AND " + condition + " = ?");
        return this;
    }

    public CriteriaSELECT andBetween(String field, Object value1, Object value2) {
        conditions.add("AND " + field + " BETWEEN ? AND ?");
        parameters.add(value1);
        parameters.add(value2);
        return this;
    }

    public CriteriaSELECT or(String condition, Object... values) {
        conditions.add("OR " + condition);
        for (Object value : values) {
            parameters.add(value);
        }
        return this;
    }

    public CriteriaSELECT orderBy(String column, boolean asc) {
        orderBy.add(column + (asc ? " ASC" : " DESC"));
        return this;
    }

    public CriteriaSELECT limit(int limit) {
        this.limit = limit;
        return this;
    }

    public CriteriaSELECT offset(int offset) {
        this.offset = offset;
        return this;
    }

    public String build() {
        StringBuilder query = new StringBuilder("SELECT ");

        if (fields.isEmpty()) {
            query.append("*");
        } else {
            query.append(String.join(", ", fields));
        }

        query.append(" FROM ").append(table);

        if (!joins.isEmpty()) {
            query.append(" ").append(String.join(" ", joins));
        }

        query.append(" WHERE ").append(String.join(" ", conditions));

        if (!orderBy.isEmpty()) {
            query.append(" ORDER BY ").append(String.join(", ", orderBy));
        }

        if (limit != null) {
            query.append(" LIMIT ").append(limit);
        }

        if (offset != null) {
            query.append(" OFFSET ").append(offset);
        }

        return query.toString();
    }

    public List<Object> getParameters() {
        return parameters;
    }


}
