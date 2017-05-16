package ru.coffeeplanter.masstat.dao;

import java.util.List;

import ru.coffeeplanter.masstat.entities.Person;
import ru.coffeeplanter.masstat.entities.Site;
import ru.coffeeplanter.masstat.net.NetCallback;

/**
 * Interface for data management.
 * Makes REST requests to the server and keeps actual data locally.
 */

public interface AdminDao {

    public void getSites(NetCallback callback);

    public void addSite(Site site, NetCallback callback);

    public void saveSite(Site site, NetCallback callback);

    public void deleteSite(int siteId, NetCallback callback);

    public void getPersons(NetCallback callback, boolean loadKeywords);

    public void addPerson(Person person, NetCallback callback);

    public void savePerson(Person person, NetCallback callback);

    public void deletePerson(int personId, NetCallback callback);

}
