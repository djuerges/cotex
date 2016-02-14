/**
 * @author Daniel Jürges <djuerges@googlemail.com>
 * 
 * this js file handles all CompiLaTeX REST service calls
 * 
 */

/* base URI for the CompiLaTeX REST Services */
var baseURI = 'http://localhost:8888/compilatex/rest';

/*
 * 
 * CALL FOR CompiLaTeX LATEX ENVIRONMENT SERVICES
 * 
 */

function getLatexEnvironments() {
    var result;
    $.ajax({
        type: 'GET',
        url: baseURI + '/latex/environments',
        dataType: 'json',
        async: false,
        success: function(data) {
            result = data;
        }
    });
    return result;
}

/*
 * 
 * CALL FOR CompiLaTeX JOB SERVICES
 * 
 */

/**
 * create new CompiLaTeX job
 * 
 * @return {jobDTO} job with fields id and name
 */
function createNewJob() {
    var job;
    $.ajax({
        type: 'POST',
        url: baseURI + '/jobs',
        success: function(data) {
            job = data;
        },
        async: false
    });
    return job;
}

/**
 * compile CompiLaTeX job with given latex environment
 * 
 * @param jobId id of the CompiLaTeX job 
 * @param latexEnvironment string with environment that will be used for compilation
 */
function compileJob(jobId, latexEnvironment) {
    $.ajax({
        type: 'GET',
        url: baseURI + '/jobs/' + jobId + '/compile/' + latexEnvironment,
        success: function(data) {
        },
        async: false
    });
}

/**
 * get log of CompiLaTeX job
 *
 * @param jobId id of the CompiLaTeX job
 * @return {String} text log of the compilation
 */
function getJobLog(jobId) {
    var log;
    $.ajax({
        type: 'GET',
        url: baseURI + '/jobs/' + jobId + '/log',
        success: function(data) {
            log = data;
        },
        async: false
    });
    return log;
}

/**
 * get html log of CompiLaTeX job
 * 
 * @param jobId id of the CompiLaTeX job
 * @return {String} html log of the compilation
 */
function getHtmlJobLog(jobId) {
    var htmlLog;
    $.ajax({
        type: 'GET',
        url: baseURI + '/jobs/' + jobId + '/log/html',
        success: function(data) {
            htmlLog = data;
        },
        async: false
    });
    return htmlLog;
}

/**
 * NOT A SERVICE CALL BUT SOMEWHAT RELATED
 * get url of CompiLaTeX job's pdf
 * 
 * @param jobId id of the CompiLaTeX job 
 */
function getJobPdfUrl(jobId) {
    return baseURI + '/jobs/' + jobId + '/pdf';
}

/**
 * get pdf of CompiLaTeX job
 * 
 * @param jobId id of the CompiLaTeX job 
 */
function getJobPdf(jobId) {
//    $.get(baseURI + '/jobs/' + jobId + '/pdf', function(data) {
//        /* download can be achieved most easily by setting url and let browser handle the rest */
//        document.location.href = baseURI + '/jobs/' + jobId + '/pdf';
//        window.open(baseURI + '/jobs/' + jobId + '/pdf');
//    });

    var pdf;
    $.ajax({
        type: 'GET',
        url: baseURI + '/jobs/' + jobId + '/pdf',
        success: function(data) {
            pdf = data;
        },
        async: false
    });
    return pdf;
}

/**
 * delete CompiLaTeX job
 * 
 * @param jobId id of the CompiLaTeX job 
 */
function deleteJob(jobId) {
    $.delete(baseURI + '/jobs/' + jobId, function(data) {
        alert('deleteJob ' + data.length);
    });
}

/*
 * 
 * CALLS FOR CompiLaTeX FILE SERVICES
 * 
 */

/**
 * create new CompiLaTeX job file
 * 
 * @param jobId id of the CompiLaTeX job  
 * @param file actual file that will be sent to server
 * @param filename name of the file
 * @param isMainTex indicates if file is main tex file for job
 * 
 * @return {jobFileDTO} created file with fields id, name, parentFolder, lastChanged isMainTex
 */
function addFile(jobId, file, filename, isMainTex) {
    var createdFile;

    var data = new FormData();
    data.append('file', file);
    data.append('filename', filename);
    data.append('isMainTex', isMainTex);

    $.ajax({
        type: 'POST',
        url: baseURI + '/jobs/' + jobId + '/files',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        success: function(data) {
            createdFile = data;
        },
        async: false
    });
    return createdFile;
}

/**
 * update CompiLaTeX job file
 * 
 * @param jobId id of the CompiLaTeX job 
 * @param fileId id of the CompiLaTeX job file that will be updated 
 * @param file actual file that will be sent to server
 */
function updateFile(jobId, fileId, file) {
    $.put(baseURI + '/jobs/' + jobId + '/files' + fileId, {file: file}, function(data) {
        alert('updateFile ' + data.length);
    });
}

/**
 * get last modification of CompiLaTeX job file
 * 
 * @param jobId id of the CompiLaTeX job 
 * @param fileId id of the CompiLaTeX job file
 */
function getLastFileModificationDate(jobId, fileId) {
    $.get(baseURI + '/jobs/' + jobId + '/files/' + fileId + '/lastchanged', function(data) {
        alert('getLastFileModificationDate ' + data.length);
    });
}

/**
 * delete CompiLaTeX job file by rest service call
 * 
 * @param jobId id of the CompiLaTeX job 
 * @param fileId id of the CompiLaTeX job file
 */
function deleteJobFile(jobId, fileId) {
    $.delete(baseURI + '/jobs/' + jobId + '/files/' + fileId, function(data) {
        alert('deleteJob ' + data.length);
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