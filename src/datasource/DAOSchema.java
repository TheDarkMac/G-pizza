package datasource;

import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DAOSchema {
    public <T> boolean create(T object);
    public <T> boolean update(T object);
    public <T> boolean delete(int object);
    public <T> List<T> findAll(int limit, int page);
    public <T> T findByName(String name,@Nullable Optional<LocalDateTime> date);
}
