package Weather.controller.persistence;

import Weather.model.Place;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceAccess {

    private String VALID_ACCOUNTS_LOCATION = System.getProperty("user.home") + File.separator + "Places.ser";

    public List<Place> loadFromPersistence(){

        List<Place> resultList = new ArrayList<Place>();
        try{
            FileInputStream fileInputStream = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<Place> persistedList = (List<Place>) objectInputStream.readObject();
            resultList.addAll(persistedList);
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }

    public void saveToPersistence(List<Place> validAccounts){
        try{
            File file = new File(VALID_ACCOUNTS_LOCATION);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(validAccounts);
            fileOutputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
