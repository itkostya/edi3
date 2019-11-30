package com.edi3.web.model;

//import categories.*;
import com.edi3.core.categories.User;
import com.edi3.core.documents.Memorandum;
import com.edi3.core.documents.Message;
import com.edi3.core.enumerations.FolderStructure;
import com.edi3.web.tools.CommonModule;
//import documents.Memorandum;
//import documents.Message;
//import enumerations.FolderStructure;
//import impl.business_processes.ExecutorTaskFolderStructureServiceImpl;
//import tools.CommonModule;

import java.util.*;

/*
 * Created by kostya on 3/2/2017.
 */
public class UserSettings {

    private User user;

    // Example for sorting in all tables: "default", "0.+", "1.-", "2.n"
    // where "0,1,2" - column numbers, "+,n" - ascending, "-" - descending
    private final Map<String, Map<String, String>> mapSort = new HashMap<String, Map<String, String>>(){{
        put("MainPanelServlet", new HashMap<String, String>() {{
            put("bookMark", "reviewTasksList");     // Current table - "reviewTasksList", "controlledTasksList" or "markedTasksList"
            put("reviewTasksList", "default");      // Sorting ("Тип процесса", "Срок", "Дата отправки")
            put("controlledTasksList", "default");  // Sorting ("Тип процесса", "Исполнить до", "Дата")
            put("markedTasksList", "default");      // Sorting ("Выполнена", "Дата")
        }});
        for (String mapName: Arrays.asList(Memorandum.class.getName()+"Journal", Message.class.getName()+"Journal"))
            put(mapName, new HashMap<String, String>() {{
                put("bookMark1", "tasksListByGroup");   // Current table - "tasksListByGroup" or "fullTasksList"
                put("bookMark2", "INBOX");              // FolderStructure - INBOX, ..., TRASH
                put("groupBy", "author");               // GroupBy (author, sender)
                put("tasksListByGroup", "default");     // Sorting (completed asc, date desc)
                put("fullTasksList", "default");        // Sorting (completed asc, date desc)
            }});
//        for (String mapName: Arrays.asList(Contractor.class.getName(), CostItem.class.getName(), Currency.class.getName(), Department.class.getName(),
//                LegalOrganization.class.getName(), PlanningPeriod.class.getName(), Position.class.getName(), ProposalTemplate.class.getName(), User.class.getName()))
//            put(mapName, new HashMap<String, String>() {{
//                put("categoryChoice", "default");
//                put("categoryJournal", "default");
//            }});
    }};

    private final Map<String, Map<String, String>> mapFilter = new HashMap<String, Map<String, String>>(){{
        put("MainPanelServlet", new HashMap<String, String>() {{
            put("reviewTasksList", "");
            put("controlledTasksList", "");
            put("markedTasksList", "");
            put("coworkersList", "");
        }});
        for (String mapName: Arrays.asList(Memorandum.class.getName()+"Journal", Message.class.getName()+"Journal"))
            put(mapName, new HashMap<String, String>() {{
                put("tasksListByGroup", "");
                put("fullTasksList", "");
            }});
//        for (String mapName: Arrays.asList(Contractor.class.getName(), CostItem.class.getName(), Currency.class.getName(), Department.class.getName(),
//                LegalOrganization.class.getName(), PlanningPeriod.class.getName(), Position.class.getName(), ProposalTemplate.class.getName(), User.class.getName()))
//            put(mapName, new HashMap<String, String>() {{
//                put("categoryChoice", "");
//                put("categoryJournal", "");
//            }});
    }};

    private final Map<Long, SessionDataElement> sessionDataMap = new TreeMap<>(); // Creating new documents

    private final Map<String, Map<FolderStructure, Integer>> documentPropertyMap = new HashMap<String, Map<FolderStructure, Integer>>(){{
        for (String mapName: Arrays.asList(Memorandum.class.getName(), Message.class.getName()))
            put(mapName, new HashMap<FolderStructure, Integer>() {{
              for (FolderStructure folderStructure: FolderStructure.values()) put(folderStructure, 0);
            }});
    }};

    public User getUser() {
        return user;
    }

    @SuppressWarnings("unused")
    public void setUser(User user) {
        this.user = user;
    }

    private Map<String, String> getMapSort(String mapName) {
        return mapSort.get(mapName);
    }

    public String getMapSortParameter(String mapName, String mapParameter){
        return getMapSort(mapName).get(mapParameter);
    }

    public void setMapSortParameter(String mapName, String mapParameter, String newValue){
        getMapSort(mapName).put(mapParameter, newValue);
    }

    public void setMapSortParameterChanged(String mapName, String mapParameter, StringBuilder sortColumnNumber){

        if (Objects.nonNull(sortColumnNumber) && sortColumnNumber.length() > 0 && (sortColumnNumber.length() <= 4) && Objects.nonNull(mapSort.get(mapName))) {

            if (sortColumnNumber.charAt(0) == mapSort.get(mapName).get(mapParameter).charAt(0)){
                setNextItem(sortColumnNumber, mapSort.get(mapName).get(mapParameter).charAt(mapSort.get(mapName).get(mapParameter).length()-1) );
            }else
                {
                    if ( mapSort.get(mapName).get(mapParameter).equals("default"))
                    {
                        setNextItem( sortColumnNumber, sortColumnNumber.charAt(sortColumnNumber.length()-1) );
                    }
                }
            setMapSortParameter(mapName, mapParameter, sortColumnNumber.toString());
        }

    }

    private void setNextItem(StringBuilder sortColumnNumber, char currentSort) {

        switch ( currentSort) {
            case 'n':
                sortColumnNumber.replace(sortColumnNumber.indexOf(".")+1, sortColumnNumber.length(), "-");
                break;
            case '+':
                sortColumnNumber.replace(sortColumnNumber.indexOf(".")+1, sortColumnNumber.length(), "-");
                break;
            case '-':
                sortColumnNumber.replace(sortColumnNumber.indexOf(".")+1, sortColumnNumber.length(), "+");
                break;
        }

    }

    public Map<String, String> getMapFilter(String mapName) {
        return mapFilter.get(mapName);
    }

    public String getMapFilterParameter(String mapName, String mapParameter){
        return getMapFilter(mapName).get(mapParameter);
    }

    public void setMapFilterParameter(String mapName, String bookmark, String filterString) {
        filterString = CommonModule.getCorrectStringForWeb(filterString);
        getMapFilter(mapName).put(bookmark, filterString);
    }

    @SuppressWarnings("unused")
    public Map<Long, SessionDataElement> getSessionDataMap() {
        return sessionDataMap;
    }

    public SessionDataElement getSessionDataElement(Long tempId) {

        if (!sessionDataMap.containsKey(tempId))
            sessionDataMap.put(tempId, new SessionDataElement(tempId, ElementStatus.CREATE));

        return sessionDataMap.get(tempId);

    }

    public Map<FolderStructure, Integer> getDocumentPropertyMap(String documentName) {

        return documentPropertyMap.get(documentName);
    }

    public Integer getMapDocumentPropertyParameter(String documentName, FolderStructure folderStructure){
        return getDocumentPropertyMap(documentName).get(folderStructure);
    }

    @SuppressWarnings("unused")
    public void setMapDocumentPropertyParameter(String documentName, FolderStructure folderStructure, Integer newValue){
        getDocumentPropertyMap(documentName).put(folderStructure, newValue);
    }

    public void setDocumentPropertyMap(String documentName) {

        if (documentName.equals(Memorandum.class.getName())) {
            // TODO:
//            documentPropertyMap.put(Memorandum.class.getName(), ExecutorTaskFolderStructureServiceImpl.INSTANCE.getTaskCountByFolders(user, Memorandum.class));
        } else if (documentName.equals(Message.class.getName())) {
            // TODO:
//            documentPropertyMap.put(Message.class.getName(), ExecutorTaskFolderStructureServiceImpl.INSTANCE.getTaskCountByFolders(user, Message.class));
        }
    }

    @SuppressWarnings("unused")
    public UserSettings() {
    }

    public UserSettings(User user) {
        this.user = user;
    }
}
