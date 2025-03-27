package gpizza.criteria;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCriteria {
    protected String table;
    protected List<Object> parameters = new ArrayList<>();

    public BaseCriteria(String table) {
        this.table = table;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public abstract String build();
}
