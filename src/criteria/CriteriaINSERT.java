package criteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CriteriaINSERT extends BaseCriteria {
    private List<String> fields = new ArrayList<>();
    private List<Object> values = new ArrayList<>();

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

    @Override
    public String build() {
        if (fields.isEmpty() || values.isEmpty()) {
            throw new IllegalStateException("Les champs et les valeurs doivent être définis.");
        }

        String placeholders = String.join(", ", Collections.nCopies(fields.size(), "?"));
        return "INSERT INTO " + table + " (" + String.join(", ", fields) + ") VALUES (" + placeholders + ")";
    }
}
