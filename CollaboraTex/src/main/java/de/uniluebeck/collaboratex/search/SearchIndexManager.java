package de.uniluebeck.collaboratex.search;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
import java.util.Set;

/**
 * This class handles all the interaction with Google AppEngine Search API.
 * Currently, it has methods for: 
 * 1. Index a document i.e. add a document to the existing index 
 * 2. Retrieve a Document from the Index, provided its Document Id (In our case it is the user id) 
 * 3. Remove a Document from the Index, provided its Document Id 
 * 4. Retrieve a List of Documents from the Index that match the Search Term.
 *
 * https://github.com/rominirani
 *
 * @author Romin Irani <romin.irani@mindstormsoftware.com>
 * modified by Daniel Jürges <djuerges@googlemail.com.com>
 */
public enum SearchIndexManager {

    INSTANCE;

    private static final String INDEX_NAME = "ProjectFileIndex";
    
    /**
     * create documents for each file dto and add them all to the index
     * 
     * @param fileDTOs list with file dtos
     */
    public void addToIndex(Set<ProjectFileDTO> fileDTOs) {
        for(ProjectFileDTO fileDTO : fileDTOs){
            addToIndex(fileDTO);
        }
    }
    
    /**
     * create document from file dto and add to index
     * 
     * @param fileDTO dto with file meta data
     */
    public void addToIndex(ProjectFileDTO fileDTO) {
        /* build document from dto */
        Document newDoc = Document.newBuilder().setId(String.valueOf(fileDTO.getId()))
                .addField(Field.newBuilder().setName("key").setText(String.valueOf(fileDTO.getKey().getId())))
                .addField(Field.newBuilder().setName("blobKey").setText(fileDTO.getBlobKey().toString()))
                .addField(Field.newBuilder().setName("name").setText(fileDTO.getName()))
                .addField(Field.newBuilder().setName("contentType").setText(fileDTO.getContentType()))
                .addField(Field.newBuilder().setName("size").setText(String.valueOf(fileDTO.getSize())))
                .addField(Field.newBuilder().setName("lastChanged").setText(String.valueOf(fileDTO.getLastChanged())))
                .addField(Field.newBuilder().setName("mainTex").setText(String.valueOf(fileDTO.isMainTex()))).build();

        /* add the document instance to the search index */
        indexDocument(newDoc);
    }
    /**
     * This method is used to add a Document to a particular Index
     *
     * @param document This is the Document instance that needs to be added to
     * the Index
     */
    public void indexDocument(Document document) {
        //Setup the Index
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

        try {
            //Put the Document in the Index. If the document is already existing, it will be overwritten
            index.put(document);
        } catch (PutException e) {
            if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
                // retry putting the document
            }
        }
    }
    
    public Document retrieveDocument(Long fileId) {
        return retrieveDocument(String.valueOf(fileId));
    }

    /**
     * This method is used to retrieve a particular Document from the Index
     *
     * @param documentId This is the key field that uniquely identifies a
     * document in the collection i.e. the Index. In our case it is the user id
     * @return An instance of the Document object from the Index.
     */
    public Document retrieveDocument(String documentId) {
        //Setup the Index
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

        //Retrieve the Record from the Index
        return index.get(documentId);
    }

    /**
     * This method is used to retrieve a list of documents from the Index that
     * match the Search Term.
     *
     * @param query The search term to find matching documents. By default,
     * if you do not use the Search Language Syntax, it will retrieve all the
     * records that contain a partial or full text match for all attributes of a
     * document
     * @return A collection of Documents that were found
     */
    public Results<ScoredDocument> retrieveDocuments(String query) {
        //Setup the Index
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        
        //Retrieve the Records from the Index
        return index.search(query);
    }

    public void removeDocumentFromIndex(Long fileId) {
        removeDocumentFromIndex(String.valueOf(fileId));
    }
    
    /**
     * This method is used to delete a document from the Index
     *
     * @param documentId This is the key field that uniquely identifies a
     * document in the collection i.e. the Index. In our case it is the user id
     */
    public void removeDocumentFromIndex(String documentId) {
        //Setup the Index
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

        //Delete the Records from the Index
        index.delete(String.valueOf(documentId));
    }

}
