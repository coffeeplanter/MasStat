package ru.coffeeplanter.masstat.dao;

import android.support.annotation.Nullable;

import java.util.List;

import ru.coffeeplanter.masstat.entities.Person;
import ru.coffeeplanter.masstat.entities.Site;

/**
 * Interface for manage data in local storage.
 */

public interface LocalStorage {

    public void saveSites(String jsonString);

    @Nullable
    public List<Site> readSites();

    public void savePersons(String jsonString);

    @Nullable
    public List<Person> readPersons();

}
