package torn.ando.gpizzasb.gpizza.criteria;

import java.util.*;

public class CriteriaINSERT extends BaseCriteria {
    private List<String> fields = new ArrayList<>();
    private List<Object> values = new ArrayList<>();
    private List<String> conflictColumns = new ArrayList<>();
    private LinkedHashMap<String, Object> updateFields = new LinkedHashMap<>();
    private List<String> returningFields = new ArrayList<>();

    public CriteriaINSERT(String table) {
        super(table);
    }

    public CriteriaINSERT insert(String... columns) {
        Collections.addAll(fields, columns);
        return this;
    }

    public CriteriaINSERT values(Object... vals) {
        if (fields.size() != vals.length) {
            throw new IllegalStateException("Nombre de champs et de valeurs incohérent.");
        }
        Collections.addAll(values, vals);
        Collections.addAll(parameters, vals);
        return this;
    }

    public CriteriaINSERT onConflict(String... columns) {
        Collections.addAll(conflictColumns, columns);
        return this;
    }

    public CriteriaINSERT doUpdate(String column, Object value) {
        updateFields.put(column, value);
        parameters.add(value);
        return this;
    }

    public CriteriaINSERT returning(String... columns) {
        Collections.addAll(returningFields, columns);
        return this;
    }

    @Override
    public String build() {
        if (fields.isEmpty() || values.isEmpty()) {
            throw new IllegalStateException("Les champs et les valeurs doivent être définis.");
        }

        String placeholders = String.join(", ", Collections.nCopies(fields.size(), "?"));
        String sql = "INSERT INTO " + table + " (" + String.join(", ", fields) + ") VALUES (" + placeholders + ")";

        if (!conflictColumns.isEmpty()) {
            sql += " ON CONFLICT (" + String.join(", ", conflictColumns) + ")";
            if (!updateFields.isEmpty()) {
                sql += " DO UPDATE SET " + String.join(", ", updateFields.keySet().stream().map(col -> col + " = ?").toList());
            } else {
                sql += " DO NOTHING";
            }
        }


        if (!returningFields.isEmpty()) {
            sql += " RETURNING " + String.join(", ", returningFields);
        }

        return sql;
    }
}
