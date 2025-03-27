package gpizza.dataSource;

import java.util.List;

public interface DAOSchema {
    public <T> List<T> saveAll(List<T> list);
    public <T> List<T> findAll();
    public <T> T findById(double id);
    public <T> T deleteById(double id);
    public <T> List<T> deleteAll(List<T> list);
    public <T> List<T> update(List<T> t);
}
