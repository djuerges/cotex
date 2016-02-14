/**
 * @author Daniel Jürges <djuerges@googlemail.com>
 * 
 * this js file handles all CollaboraTeX REST service calls
 * 
 */

/*
 * 
 * CALL FOR PROJECT SERVICES
 * 
 */

/**
 * create new project 
 */
function createNewProject() {
    $.post("/rest/projects", function(data) {
        /* create project entry in sidebar */
        createEmptyProjectList(data.id, data.name);
        /* would be data.key.id but Jackson adds extra field id and KeySerializer.java
         * writes key to plain string to every field gets lost */

        /* make project name editable */
        $("a[data-pk='" + data.id + "']").editable();
        
        /* reset project context menu otherwise it wouldn't be available for this item */
        setProjectContextMenu();
    });
}

/**
 * download project with all files
 * 
 * @param projectId id of the CollaboraTeX job 
 */
function downloadProject(projectId) {
//    $.get('/rest/projects/' + projectId + '/download', function(data) {
//        /* do nothing, browser will automatically start download */
//        alert(data.length);
//    });

    /* download can be achieved most easily by setting url and let browser handle the rest */
    document.location.href = '/rest/projects/' + projectId + '/download';
}

/**
 * rename project by posting new name to rest service
 * 
 * @param projectId id of the CollaboraTeX job 
 * @param name new name of the project
 */
function renameProject(projectId, name) {
    $.post('/rest/projects/' + projectId + '/rename', {value: name}, function(data) {
        /* change project name in sidebar */
        $("a[data-pk='" + projectId + "']").text(name);
    });
}

/**
 * share project
 * 
 * @param projectId id of the CollaboraTeX job 
 */
function shareProject(projectId) {
    alert("share " + projectId);
}

/**
 * delete project
 * 
 * @param projectId id of the CollaboraTeX job 
 */
function deleteProject(projectId) {
    $.delete('/rest/projects/' + projectId, function(data) {
        /* after success delete project in sidebar */
        deleteProjectFromSidebar(projectId);
    });
}

/*
 * 
 * CALLS FOR FILE SERVICES
 * 
 */

/**
 * create new file
 * 
 * @param projectId id of the CollaboraTeX job 
 */
function createNewFile(projectId) {
    $.post('/rest/projects/' + projectId + '/files', function(data) {
        /* create new file entry in project */
        createNewFileInProjectList(projectId, data.id, data.name);
        /* would be data.key.id but Jackson adds extra field id and KeySerializer.java
         * writes key to plain string to every field gets lost */

        /* make file name editable */
        $("a[data-pk='" + data.id + "']").editable();
        
        /* reset file context menu otherwise it wouldn't be available for this item */
        setFileContextMenu();
    });
}

/**
 * import new files
 * 
 * @param projectId id of the CollaboraTeX job 
 */
function importFiles(projectId) {
    $.post('/rest/projects/' + projectId + '/files/import', function(data) {
        /* something to do or maybe not */
    });
}


/**
 * update file
 * 
 * @param projectId id of the CollaboraTeX job 
 * @param fileId id of the CollaboraTeX project file that will be updated
 * @param file the actual file that will be sent to the server
 */
function updateFile(projectId, fileId, file) {
    $.put('/rest/projects/' + projectId + '/files' + fileId, file, function(data) {
        /* not much to do on success */
    });
}

/**
 * update file content (for plain text files) 
 * 
 * @param projectId id of the CollaboraTeX job 
 * @param fileId id of the CollaboraTeX project file which's content will be updated
 * @param content the new content of the file as string
 */
function updateFileContent(projectId, fileId, content) {
    $.post('/rest/projects/' + projectId + '/files/' + fileId + '/content', {content: content}, function(data) {
        /* not much to do on success */
    });
}

/**
 * rename file by posting new name to rest service
 * 
 * @param projectId id of the CollaboraTeX job 
 * @param fileId id of the CollaboraTeX project file
 * @param name the new name of the CollaboraTeX project file
 */
function renameFile(projectId, fileId, name) {
    $.post('/rest/projects/' + projectId + '/files/' + fileId + '/rename', {value: name}, function(data) {
        /* change project name in sidebar */
        $("a[data-pk='" + fileId + "']").text(name);
    });
}

/**
 * patch file content (for plain text files)
 * 
 * @param projectId id of the CollaboraTeX job 
 * @param fileId id of the CollaboraTeX project file
 * @param patch patch which will update the CollaboraTeX project file to the most recent version
 */
function patchFileContent(projectId, fileId, patch) {
    $.post('/rest/projects/' + projectId + '/files/' + fileId + '/patch', {patch: patch}, function(data) {
        /* not much to do on success */
    });
}

/**
 *  set file as main tex service 
 *  
 * @param projectId id of the CollaboraTeX job 
 * @param fileId id of the CollaboraTeX project file
 */
function setFileAsMainTex(projectId, fileId) {
    $.post('/rest/projects/' + projectId + '/files/' + fileId + '/setmaintex', {mainTex: true}, function(data) {
        /* change main tex file highlighting in project list */
        updateMainTexFileHighlighting(projectId, fileId);
    });
}

/**
 * get file content service
 * 
 * @param projectId id of the CollaboraTeX job 
 * @param fileId id of the CollaboraTeX project file
 */
function getFileContent(projectId, fileId) {
    var content;
    $.ajax({
        type: 'GET',
        dataType: 'text',
        async: false,
        url: '/rest/projects/' + projectId + '/files/' + fileId,
        success: function(data) {
            content = data;
        }
    });
    return content;
}

/**
 * get meta data for file
 * 
 * @param projectId id of the CollaboraTeX job 
 * @param fileId id of the CollaboraTeX project file
 */
function getFileMetaData(projectId, fileId){
    var metaData;
    $.ajax({
        type: 'GET',
        dataType: 'json',
        async: false,
        url: '/rest/projects/' + projectId + '/files/' + fileId + '/meta',
        success: function(data) {
            metaData = data;
        }
    });
    return metaData;
}

/**
 * delete file
 * 
 * @param projectId id of the CollaboraTeX job 
 * @param fileId id of the CollaboraTeX project file
 */
function deleteProjectFile(projectId, fileId) {
    $.delete('/rest/projects/' + projectId + '/files/' + fileId, function(data) {
        /* delete file from project list */
        deleteFileFromProjectList(projectId, fileId);
    });
}

/*
 * 
 * CALLS FOR SEARCH SERVICES
 * 
 */

/**
 * search all documents with given query string
 * 
 * @param query search string that will be matched against all documents
 */
function search(query) {
    $.post('/rest/search', {query: query}, function(results) {
        /* pass results to method that visualises them on a result page */
        listSearchResults(results, query);
    });
}

/*
 * 
 * CALLS FOR SESSION SERVICES
 * 
 */

/**
 * get token from Channel API for JS client
 */
function getToken() {
    var token;
    $.ajax({
        type: 'GET',
        dataType: 'text',
        async: false,
        url: '/rest/session/channel/token',
        success: function(data) {
            token = data;
        }
    });
    return token;
}

/**
 * register for open document 
 * 
 * @param fileId id of the CollaboraTeX project file
 */
function registerForDocument(fileId) {
    $.post('/rest/session/document/' + fileId + '/register', function(data) {
        /* not much to do on success */
    });
}

/**
 * unregister for opened document 
 * 
 * @param fileId id of the CollaboraTeX project file
 */
function unregisterForDocument(fileId) {
    $.post('/rest/session/document/' + fileId + '/unregister', function(data) {
        /* not much to do on success */
    });
}

/*
 * 
 * UTILITY FUNCTIONS
 * 
 */

/* extend jQuery with functions for PUT and DELETE requests */
function _ajax_request(url, data, callback, type, method) {
    if (jQuery.isFunction(data)) {
        callback = data;
        data = {};
    }
    return jQuery.ajax({
        type: method,
        url: url,
        data: data,
        success: callback,
        dataType: type
    });
}

jQuery.extend({
    put: function(url, data, callback, type) {
        return _ajax_request(url, data, callback, type, 'PUT');
    },
    delete: function(url, data, callback, type) {
        return _ajax_request(url, data, callback, type, 'DELETE');
    }
});