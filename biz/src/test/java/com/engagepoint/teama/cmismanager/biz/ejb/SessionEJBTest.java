package com.engagepoint.teama.cmismanager.biz.ejb;


import com.engagepoint.teama.cmismanager.biz.ejb.SessionEJB;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Acl;
import org.apache.chemistry.opencmis.commons.data.BulkUpdateObjectIdAndChangeToken;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.enums.*;
import org.apache.chemistry.opencmis.commons.spi.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;

import static org.mockito.Mockito.*;

public class SessionEJBTest {

    @Test
    public void testCreateSession() throws Exception {
        SessionFactory sessionFactory = new SessionFactory() {
            @Override
            public Session createSession(Map<String, String> parameters) {
                return null;
            }

            @Override
            public List<Repository> getRepositories(Map<String, String> parameters) {
                return null;
            }
        };

        SessionFactory spy = spy(sessionFactory);
        SessionEJB sessionEJB = new SessionEJB(spy);
        Map<String, String> map = new HashMap<String, String>();
        map.put(SessionParameter.USER, "user");
        map.put(SessionParameter.PASSWORD, "password");
        map.put(SessionParameter.ATOMPUB_URL, "url" + "/atom11");
        map.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        map.put(SessionParameter.REPOSITORY_ID, "repoID");

        Session session = new Session() {
            @Override
            public void clear() {

            }

            @Override
            public CmisBinding getBinding() {
                return null;
            }

            @Override
            public OperationContext getDefaultContext() {
                return null;
            }

            @Override
            public void setDefaultContext(OperationContext context) {

            }

            @Override
            public OperationContext createOperationContext() {
                return null;
            }

            @Override
            public OperationContext createOperationContext(Set<String> filter, boolean includeAcls, boolean includeAllowableActions, boolean includePolicies, IncludeRelationships includeRelationships, Set<String> renditionFilter, boolean includePathSegments, String orderBy, boolean cacheEnabled, int maxItemsPerPage) {
                return null;
            }

            @Override
            public ObjectId createObjectId(String id) {
                return null;
            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public org.apache.chemistry.opencmis.commons.data.RepositoryInfo getRepositoryInfo() {
                return null;
            }

            @Override
            public ObjectFactory getObjectFactory() {
                return null;
            }

            @Override
            public ObjectType getTypeDefinition(String typeId) {
                return null;
            }

            @Override
            public ItemIterable<ObjectType> getTypeChildren(String typeId, boolean includePropertyDefinitions) {
                return null;
            }

            @Override
            public List<Tree<ObjectType>> getTypeDescendants(String typeId, int depth, boolean includePropertyDefinitions) {
                return null;
            }

            @Override
            public ObjectType createType(TypeDefinition type) {
                return null;
            }

            @Override
            public ObjectType updateType(TypeDefinition type) {
                return null;
            }

            @Override
            public void deleteType(String typeId) {

            }

            @Override
            public Folder getRootFolder() {
                return null;
            }

            @Override
            public Folder getRootFolder(OperationContext context) {
                return null;
            }

            @Override
            public ItemIterable<Document> getCheckedOutDocs() {
                return null;
            }

            @Override
            public ItemIterable<Document> getCheckedOutDocs(OperationContext context) {
                return null;
            }

            @Override
            public CmisObject getObject(ObjectId objectId) {
                return null;
            }

            @Override
            public CmisObject getObject(ObjectId objectId, OperationContext context) {
                return null;
            }

            @Override
            public CmisObject getObject(String objectId) {
                return null;
            }

            @Override
            public CmisObject getObject(String objectId, OperationContext context) {
                return null;
            }

            @Override
            public CmisObject getObjectByPath(String path) {
                return null;
            }

            @Override
            public CmisObject getObjectByPath(String path, OperationContext context) {
                return null;
            }

            @Override
            public void removeObjectFromCache(ObjectId objectId) {

            }

            @Override
            public void removeObjectFromCache(String objectId) {

            }

            @Override
            public ItemIterable<QueryResult> query(String statement, boolean searchAllVersions) {
                return null;
            }

            @Override
            public ItemIterable<QueryResult> query(String statement, boolean searchAllVersions, OperationContext context) {
                return null;
            }

            @Override
            public ItemIterable<CmisObject> queryObjects(String typeId, String where, boolean searchAllVersions, OperationContext context) {
                return null;
            }

            @Override
            public QueryStatement createQueryStatement(String statement) {
                return null;
            }

            @Override
            public ChangeEvents getContentChanges(String changeLogToken, boolean includeProperties, long maxNumItems) {
                return null;
            }

            @Override
            public ChangeEvents getContentChanges(String changeLogToken, boolean includeProperties, long maxNumItems, OperationContext context) {
                return null;
            }

            @Override
            public ObjectId createDocument(Map<String, ?> properties, ObjectId folderId, ContentStream contentStream, VersioningState versioningState, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createDocument(Map<String, ?> properties, ObjectId folderId, ContentStream contentStream, VersioningState versioningState) {
                return null;
            }

            @Override
            public ObjectId createDocumentFromSource(ObjectId source, Map<String, ?> properties, ObjectId folderId, VersioningState versioningState, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createDocumentFromSource(ObjectId source, Map<String, ?> properties, ObjectId folderId, VersioningState versioningState) {
                return null;
            }

            @Override
            public ObjectId createFolder(Map<String, ?> properties, ObjectId folderId, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createFolder(Map<String, ?> properties, ObjectId folderId) {
                return null;
            }

            @Override
            public ObjectId createPolicy(Map<String, ?> properties, ObjectId folderId, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createPolicy(Map<String, ?> properties, ObjectId folderId) {
                return null;
            }

            @Override
            public ObjectId createItem(Map<String, ?> properties, ObjectId folderId, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createItem(Map<String, ?> properties, ObjectId folderId) {
                return null;
            }

            @Override
            public ObjectId createRelationship(Map<String, ?> properties, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createRelationship(Map<String, ?> properties) {
                return null;
            }

            @Override
            public ItemIterable<Relationship> getRelationships(ObjectId objectId, boolean includeSubRelationshipTypes, RelationshipDirection relationshipDirection, ObjectType type, OperationContext context) {
                return null;
            }

            @Override
            public List<BulkUpdateObjectIdAndChangeToken> bulkUpdateProperties(List<CmisObject> objects, Map<String, ?> properties, List<String> addSecondaryTypeIds, List<String> removeSecondaryTypeIds) {
                return null;
            }

            @Override
            public void delete(ObjectId objectId) {

            }

            @Override
            public void delete(ObjectId objectId, boolean allVersions) {

            }

            @Override
            public ContentStream getContentStream(ObjectId docId) {
                return null;
            }

            @Override
            public ContentStream getContentStream(ObjectId docId, String streamId, BigInteger offset, BigInteger length) {
                return null;
            }

            @Override
            public Acl getAcl(ObjectId objectId, boolean onlyBasicPermissions) {
                return null;
            }

            @Override
            public Acl applyAcl(ObjectId objectId, List<Ace> addAces, List<Ace> removeAces, AclPropagation aclPropagation) {
                return null;
            }

            @Override
            public Acl setAcl(ObjectId objectId, List<Ace> aces) {
                return null;
            }

            @Override
            public void applyPolicy(ObjectId objectId, ObjectId... policyIds) {

            }

            @Override
            public void removePolicy(ObjectId objectId, ObjectId... policyIds) {

            }
        };
        when(spy.createSession(map)).thenReturn(session);
        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");
        verify(spy).createSession(map);
        Assert.assertEquals(session, sessionEJB.getSession("sessionId"));
    }

    @Test(expected = ConnectionException.class)
    public void testCloseSessionException() throws Exception {
        SessionEJB sessionEJB = new SessionEJB();
        sessionEJB.closeSession("sessionId");
        sessionEJB.getSession("sessionId");
    }

    @Test(expected = ConnectionException.class)
    public void testCloseSession() throws ConnectionException {
        SessionFactory sessionFactory = new SessionFactory() {
            @Override
            public Session createSession(Map<String, String> parameters) {
                return null;
            }

            @Override
            public List<Repository> getRepositories(Map<String, String> parameters) {
                return null;
            }
        };
        SessionFactory spy = spy(sessionFactory);
        SessionEJB sessionEJB = new SessionEJB(spy);
        Map<String, String> map = new HashMap<String, String>();
        map.put(SessionParameter.USER, "user");
        map.put(SessionParameter.PASSWORD, "password");
        map.put(SessionParameter.ATOMPUB_URL, "url" + "/atom11");
        map.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        map.put(SessionParameter.REPOSITORY_ID, "repoID");

        Session session = new Session() {
            @Override
            public void clear() {

            }

            @Override
            public CmisBinding getBinding() {
                return null;
            }

            @Override
            public OperationContext getDefaultContext() {
                return null;
            }

            @Override
            public void setDefaultContext(OperationContext context) {

            }

            @Override
            public OperationContext createOperationContext() {
                return null;
            }

            @Override
            public OperationContext createOperationContext(Set<String> filter, boolean includeAcls, boolean includeAllowableActions, boolean includePolicies, IncludeRelationships includeRelationships, Set<String> renditionFilter, boolean includePathSegments, String orderBy, boolean cacheEnabled, int maxItemsPerPage) {
                return null;
            }

            @Override
            public ObjectId createObjectId(String id) {
                return null;
            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public org.apache.chemistry.opencmis.commons.data.RepositoryInfo getRepositoryInfo() {
                return null;
            }

            @Override
            public ObjectFactory getObjectFactory() {
                return null;
            }

            @Override
            public ObjectType getTypeDefinition(String typeId) {
                return null;
            }

            @Override
            public ItemIterable<ObjectType> getTypeChildren(String typeId, boolean includePropertyDefinitions) {
                return null;
            }

            @Override
            public List<Tree<ObjectType>> getTypeDescendants(String typeId, int depth, boolean includePropertyDefinitions) {
                return null;
            }

            @Override
            public ObjectType createType(TypeDefinition type) {
                return null;
            }

            @Override
            public ObjectType updateType(TypeDefinition type) {
                return null;
            }

            @Override
            public void deleteType(String typeId) {

            }

            @Override
            public Folder getRootFolder() {
                return null;
            }

            @Override
            public Folder getRootFolder(OperationContext context) {
                return null;
            }

            @Override
            public ItemIterable<Document> getCheckedOutDocs() {
                return null;
            }

            @Override
            public ItemIterable<Document> getCheckedOutDocs(OperationContext context) {
                return null;
            }

            @Override
            public CmisObject getObject(ObjectId objectId) {
                return null;
            }

            @Override
            public CmisObject getObject(ObjectId objectId, OperationContext context) {
                return null;
            }

            @Override
            public CmisObject getObject(String objectId) {
                return null;
            }

            @Override
            public CmisObject getObject(String objectId, OperationContext context) {
                return null;
            }

            @Override
            public CmisObject getObjectByPath(String path) {
                return null;
            }

            @Override
            public CmisObject getObjectByPath(String path, OperationContext context) {
                return null;
            }

            @Override
            public void removeObjectFromCache(ObjectId objectId) {

            }

            @Override
            public void removeObjectFromCache(String objectId) {

            }

            @Override
            public ItemIterable<QueryResult> query(String statement, boolean searchAllVersions) {
                return null;
            }

            @Override
            public ItemIterable<QueryResult> query(String statement, boolean searchAllVersions, OperationContext context) {
                return null;
            }

            @Override
            public ItemIterable<CmisObject> queryObjects(String typeId, String where, boolean searchAllVersions, OperationContext context) {
                return null;
            }

            @Override
            public QueryStatement createQueryStatement(String statement) {
                return null;
            }

            @Override
            public ChangeEvents getContentChanges(String changeLogToken, boolean includeProperties, long maxNumItems) {
                return null;
            }

            @Override
            public ChangeEvents getContentChanges(String changeLogToken, boolean includeProperties, long maxNumItems, OperationContext context) {
                return null;
            }

            @Override
            public ObjectId createDocument(Map<String, ?> properties, ObjectId folderId, ContentStream contentStream, VersioningState versioningState, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createDocument(Map<String, ?> properties, ObjectId folderId, ContentStream contentStream, VersioningState versioningState) {
                return null;
            }

            @Override
            public ObjectId createDocumentFromSource(ObjectId source, Map<String, ?> properties, ObjectId folderId, VersioningState versioningState, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createDocumentFromSource(ObjectId source, Map<String, ?> properties, ObjectId folderId, VersioningState versioningState) {
                return null;
            }

            @Override
            public ObjectId createFolder(Map<String, ?> properties, ObjectId folderId, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createFolder(Map<String, ?> properties, ObjectId folderId) {
                return null;
            }

            @Override
            public ObjectId createPolicy(Map<String, ?> properties, ObjectId folderId, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createPolicy(Map<String, ?> properties, ObjectId folderId) {
                return null;
            }

            @Override
            public ObjectId createItem(Map<String, ?> properties, ObjectId folderId, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createItem(Map<String, ?> properties, ObjectId folderId) {
                return null;
            }

            @Override
            public ObjectId createRelationship(Map<String, ?> properties, List<Policy> policies, List<Ace> addAces, List<Ace> removeAces) {
                return null;
            }

            @Override
            public ObjectId createRelationship(Map<String, ?> properties) {
                return null;
            }

            @Override
            public ItemIterable<Relationship> getRelationships(ObjectId objectId, boolean includeSubRelationshipTypes, RelationshipDirection relationshipDirection, ObjectType type, OperationContext context) {
                return null;
            }

            @Override
            public List<BulkUpdateObjectIdAndChangeToken> bulkUpdateProperties(List<CmisObject> objects, Map<String, ?> properties, List<String> addSecondaryTypeIds, List<String> removeSecondaryTypeIds) {
                return null;
            }

            @Override
            public void delete(ObjectId objectId) {

            }

            @Override
            public void delete(ObjectId objectId, boolean allVersions) {

            }

            @Override
            public ContentStream getContentStream(ObjectId docId) {
                return null;
            }

            @Override
            public ContentStream getContentStream(ObjectId docId, String streamId, BigInteger offset, BigInteger length) {
                return null;
            }

            @Override
            public Acl getAcl(ObjectId objectId, boolean onlyBasicPermissions) {
                return null;
            }

            @Override
            public Acl applyAcl(ObjectId objectId, List<Ace> addAces, List<Ace> removeAces, AclPropagation aclPropagation) {
                return null;
            }

            @Override
            public Acl setAcl(ObjectId objectId, List<Ace> aces) {
                return null;
            }

            @Override
            public void applyPolicy(ObjectId objectId, ObjectId... policyIds) {

            }

            @Override
            public void removePolicy(ObjectId objectId, ObjectId... policyIds) {

            }
        };

        Session sessionSpy = spy(session);
        when(spy.createSession(map)).thenReturn(sessionSpy);
        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");
        CmisBinding cmisBinding = new CmisBinding() {
            @Override
            public RepositoryService getRepositoryService() {
                return null;
            }

            @Override
            public NavigationService getNavigationService() {
                return null;
            }

            @Override
            public ObjectService getObjectService() {
                return null;
            }

            @Override
            public VersioningService getVersioningService() {
                return null;
            }

            @Override
            public RelationshipService getRelationshipService() {
                return null;
            }

            @Override
            public DiscoveryService getDiscoveryService() {
                return null;
            }

            @Override
            public MultiFilingService getMultiFilingService() {
                return null;
            }

            @Override
            public AclService getAclService() {
                return null;
            }

            @Override
            public PolicyService getPolicyService() {
                return null;
            }

            @Override
            public BindingsObjectFactory getObjectFactory() {
                return null;
            }

            @Override
            public AuthenticationProvider getAuthenticationProvider() {
                return null;
            }

            @Override
            public void clearAllCaches() {

            }

            @Override
            public void clearRepositoryCache(String repositoryId) {

            }

            @Override
            public void close() {

            }
        };
        CmisBinding spyCmisBinding = spy(cmisBinding);
        when(sessionSpy.getBinding()).thenReturn(spyCmisBinding);
        sessionEJB.closeSession("sessionId");
        sessionEJB.closeSession("sessionId");
    }
}
