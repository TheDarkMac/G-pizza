package criteria;

import java.util.ArrayList;
import java.util.List;

public class Criteria {
    private String table;
    private List<String> fields = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private List<String> conditions = new ArrayList<>();
    private List<Object> parameters = new ArrayList<>();
    private List<String> orderBy = new ArrayList<>();
    private Integer limit;
    private Integer offset;

    public Criteria(String table) {
        this.table = table;
        this.conditions.add("1=1");
    }

    public Criteria select(String... fields) {
        this.fields.clear();
        for (String field : fields) {
            this.fields.add(field);
        }
        return this;
    }

    public Criteria join(String joinType, String table, String condition) {
        joins.add(joinType + " JOIN " + table + " ON " + condition);
        return this;
    }

    public Criteria and(String condition, Object... values) {
        conditions.add("AND " + condition);
        for (Object value : values) {
            parameters.add(value);
        }
        return this;
    }

    public Criteria or(String condition, Object... values) {
        conditions.add("OR " + condition);
        for (Object value : values) {
            parameters.add(value);
        }
        return this;
    }

    public Criteria orderBy(String column, boolean asc) {
        orderBy.add(column + (asc ? " ASC" : " DESC"));
        return this;
    }

    public Criteria limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Criteria offset(int offset) {
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
