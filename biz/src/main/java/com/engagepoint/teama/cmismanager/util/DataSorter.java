package com.engagepoint.teama.cmismanager.util;

import com.engagepoint.teama.cmismanager.FileStatusReport;
import com.engagepoint.teama.cmismanager.exceptions.ValidationException;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Collection;

public final class DataSorter {

    public static final Logger LOG = Logger.getLogger(DataSorter.class);
    public static final String FILES = " files.";

    public static final String SAME_ID_ERROR = "Same ID";

    /**
     * This method validates typeDefinitions from map, then sort them.
     * @see com.engagepoint.teama.cmismanager.ConvertorEJB
     * @param typeDefinitionMap streamHashMap, where key is file name, and value is TypeDefinition instance.
     * @param fileStatusList if some of TypeDefinition instances fails convertation or validation, method will put in
     *                       this list new FileStatusReport instance, that contains file name and explanation.
     * @return List<TypeDefinition>, that contains sorted by ParentId TypeDTO instances.
     */
    public static List<TypeDefinition> validateAndSort(Map<String, TypeDefinition> typeDefinitionMap, List<FileStatusReport> fileStatusList) {

        Set<TypeDefinition> typeDefinitionTreeSet = validate(typeDefinitionMap, fileStatusList);

        Map<String, List<TypeDefinition>> sortedByIdTypeMap = sort(typeDefinitionTreeSet);

        List<TypeDefinition> resultList = new ArrayList<TypeDefinition>();

        if (sortedByIdTypeMap.isEmpty()) {
            fileStatusList.add(new FileStatusReport("ID collision", resultList.size() + FILES));
        } else {

            for (Collection value : sortedByIdTypeMap.values()) {
                resultList.addAll(value);
            }

            fileStatusList.add(new FileStatusReport("Ready to upload", resultList.size() + FILES));

        }

        return resultList;
    }

    /**
     * This method contains part of 'validateAndSort' method.
     * It validates typeDefinitions from map. If two or more TypeDefinition instances have the same ID,
     * only one will be add to returned typeDefinitionSet.
     * @param typeDefinitionMap streamHashMap, where key is file name, and value is TypeDefinition instance.
     * @param fileStatusList if some of TypeDefinition instances fails convertation or validation, method will put in
     *                       this list new FileStatusReport instance, that contains file name and explanation.
     * @return Set<TypeDefinition>, that contains TypeDTO instances with unique ID.
     */
    private static Set<TypeDefinition> validate(Map<String, TypeDefinition> typeDefinitionMap, List<FileStatusReport> fileStatusList) {

        Set<TypeDefinition> typeDefinitionTreeSet = new TreeSet<TypeDefinition>(new TypesIdentityComparator());

        for (String fileName : typeDefinitionMap.keySet()) {

            TypeDefinition type = typeDefinitionMap.get(fileName);

            try {
                validateTypeDefinition(type);
                if (!typeDefinitionTreeSet.add(type)) {
                    fileStatusList.add(new FileStatusReport(fileName, SAME_ID_ERROR));
                }
            } catch (ValidationException e) {
                LOG.error(e.getMessage(), e);
                fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
            }
        }

        return typeDefinitionTreeSet;
    }

    /**
     * This method contains part of 'validateAndSort' method.
     * It sorts typeDefinitionTreeSet by parentTypeID. Sort rule is simple: "There are set of TypeDefinition instances:
     * type1, type2 ... If type1 have parentTypeID, that equals type2 ID, at first we must create type2, then type1 ".
     * @param typeDefinitionTreeSet that contains TypeDTO instances with unique ID
     * @return Map<String, List<TypeDefinition>> , where keys ara parentTypeIDs
     */
    private static Map<String, List<TypeDefinition>> sort(Set<TypeDefinition> typeDefinitionTreeSet) {

        Map<String, List<TypeDefinition>> sortedByIdTypeMap = new HashMap<String, List<TypeDefinition>>();

        for (TypeDefinition type : typeDefinitionTreeSet) {
            if (sortedByIdTypeMap.containsKey(type.getParentTypeId())) {

                List<TypeDefinition> children = sortedByIdTypeMap.get(type.getParentTypeId());
                children.add(type);

            } else {
                ArrayList<TypeDefinition> children = new ArrayList<TypeDefinition>();

                children.add(type);
                sortedByIdTypeMap.put(type.getParentTypeId(), children);
            }
        }

        for (TypeDefinition type : typeDefinitionTreeSet) {

            if (sortedByIdTypeMap.containsKey(type.getId())) {
                List<TypeDefinition> temp = sortedByIdTypeMap.get(type.getParentTypeId());

                if (!temp.isEmpty()) {
                    temp.addAll(sortedByIdTypeMap.get(type.getId()));
                    sortedByIdTypeMap.put(type.getId(), temp);
                }

            }

        }

        for (TypeDefinition type : typeDefinitionTreeSet) {
            if (sortedByIdTypeMap.containsKey(type.getId())) {
                sortedByIdTypeMap.remove(type.getId());
            }
        }

        return sortedByIdTypeMap;
    }

    /**
     * This method validates TypeDefinition instance.
     * @throws ValidationException
     */
    public static void validateTypeDefinition(TypeDefinition type) throws ValidationException {

        if (type == null) {
            throw new IllegalArgumentException("Type is null!");
        }

        if (type.getId().isEmpty()) {
            throw new ValidationException("Type id must be set.");
        }

        if (type.getLocalName().isEmpty()) {
            throw new ValidationException("Local name must be set.");
        }

        if (type.getQueryName() != null) {
            if (type.getQueryName().length() == 0) {
                throw new ValidationException("Query name must not be empty.");
            } else if (!checkQueryName(type.getQueryName())) {
                throw new ValidationException("Query name contains invalid characters.");
            }
        }

        if (type.isCreatable() == null) {
            throw new ValidationException("Creatable flag must be set.");
        }

        if (type.isFileable() == null) {
            throw new ValidationException("Fileable flag must be set.");
        }

        if (type.isQueryable() == null) {
            throw new ValidationException("Queryable flag must be set.");
        } else if (type.isQueryable().booleanValue()) {
            String tmp = type.getQueryName();
            if (tmp == null || tmp.isEmpty()) {
                throw new ValidationException("Queryable flag is set to TRUE, but the query name is not set.");
            }
        }

        if (type.isControllablePolicy() == null) {
            throw new ValidationException("ControllablePolicy flag must be set.");
        }

        if (type.isControllableAcl() == null) {
            throw new ValidationException("ControllableACL flag must be set.");
        }

        if (type.isFulltextIndexed() == null) {
            throw new ValidationException("FulltextIndexed flag must be set.");
        }

        if (type.isIncludedInSupertypeQuery() == null) {
            throw new ValidationException("IncludedInSupertypeQuery flag must be set.");
        }

        if (type.getBaseTypeId() == null) {
            throw new ValidationException("Base type id must be set.");
        } else if (!type.getBaseTypeId().value().equals(type.getParentTypeId())) {
            String tmp = type.getParentTypeId();
            if (tmp == null || tmp.isEmpty()) {
                throw new ValidationException("Parent type id must be set.");
            }
        }

    }

    /**
     * This method contains part of 'validateTypeDefinition' method.
     * Validates QueryName of some TypeDefinition instance.
     * @return false if validation fail
     */
    private static boolean checkQueryName(String queryName) {
        return queryName != null && queryName.length() > 0 && checkString(queryName);
    }

    /**
     * This method contains part of 'checkQueryName' method.
     * @return false if validation fail
     */
    private static boolean checkString(String str) {
        String[] tmp = {" ", ",", "\"", "'", "\\", ".", "(", ")"};

        for (int i = 0; i < tmp.length; ++i) {
            if (str.contains(tmp[i])) {
                return false;
            }
        }

        return true;
    }
}
