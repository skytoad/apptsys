package apptsys.DAO;

import javafx.collections.ObservableList;

/**
 * interface to define structure of DAOs. However, due to project requirement, the only class implementing this is AppointmentDAO. Thus, this code is not necessary, but I left it in case it will be useful for future development.
 * @param <T> the type of the interface (in our case only implemented as Appointment)
 */
public interface DAO<T> {

    ObservableList<T> getAll();

    void update(T t);

    void delete(T t);

    void add(T t);

}
