package torn.ando.gpizzasb.gpizza.dao;

import java.util.List;

public interface DAOSchema {
    public <T> List<T> saveAll(List<T> list);
    public <T> List<T> findAll();
    public <T> T findById(double id);
    public <T> T deleteById(double id);
    public <T> List<T> deleteAll(List<T> list);
    public <T> List<T> updateAll(List<T> t);
}
