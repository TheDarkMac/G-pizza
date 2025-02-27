package datasource;

import java.util.List;
import java.util.Map;

public interface DAOSchema {
    public <T> boolean create(T object);
    public <T> boolean update(T object);
    public <T> boolean delete(int object);
    public <T> List<T> findAll(int limit, int page);
    public <T> T findByName(String name, Map<String, Object> criterias);
}
