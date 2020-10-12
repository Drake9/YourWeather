package Weather.controller.persistence;

import Weather.model.Place;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersistenceAccess {

    private final String VALID_ACCOUNTS_LOCATION = System.getProperty("user.home") + File.separator + "Places.json";

    public List<Place> loadFromPersistence() {

        List<Place> resultList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Place> persistedList = Arrays.asList(mapper.readValue(Paths.get(VALID_ACCOUNTS_LOCATION).toFile(), Place[].class));
            resultList.addAll(persistedList);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultList;

    }

    public void saveToPersistence(List<Place> validAccounts) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(Paths.get(VALID_ACCOUNTS_LOCATION).toFile(), validAccounts);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
