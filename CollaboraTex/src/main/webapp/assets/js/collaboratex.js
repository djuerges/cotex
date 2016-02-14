/**
 * @author Daniel Jürges <djuerges@googlemail.com>
 * 
 * this js file defines all custom settings like inline-editing,
 * creating and deleting elements in the DOM to represent files and projects,
 * utility functions and so on
 * 
 */

/* define file extensions or names for plain text files 
 * that can be opened in the editor */
var fileExtensions = ['.tex', '.bib', 'New File', 'Makefile'];

/* apply several thing on document ready */
$(document).ready(function() {

    /* set inline editing options */
    $.fn.editable.defaults.mode = 'inline';
    $.fn.editable.defaults.showbuttons = false;
    $.fn.editable.defaults.placeholder = 'Enter new name';
    $.fn.editable.defaults.emptytext = 'Enter new name';
    $.fn.editable.defaults.defaultValue = 'Enter new name';
    $.fn.editable.defaults.toggle = 'dblclick';

    /* make all Project, file and tab names inline editable */
    //  for now do not allow editing on tabs
    //  , *[id*='-tab']
    $("*[id*='-project'], *[id*='-file']").each(function() {
        $(this).editable();
    });

//    MIGHT WANNA USE THAT LATER
//    
//    /* make the sidebar resizeable */
//    $('.sidebar').resizable();
//    
//    /* make the list elements sortable */
//    $('ul.nav-sidebar').sortable();
//    $('ul.nav-sidebar').disableSelection();
});

/* resize navbar on document load and window change */
$(document).ready(sizeContent);
$(window).resize(sizeContent);

function sizeContent() {
    $(".sidebar").css('height', $(document).height() + "px");
}

/* set buttons disabled if there is no action, set enabled if there is */
function disableUndoRedo() {
    if ($('#editor').length) {
        var undoManager = ace.edit("editor").getSession().getUndoManager();

        if (undoManager.hasUndo())
            $('#undo').parent('li').removeClass('disabled');
        else
            $('#undo').parent('li').addClass('disabled');

        if (undoManager.hasRedo())
            $('#redo').parent('li').removeClass('disabled');
        else
            $('#redo').parent('li').addClass('disabled');
    }
}

/*
 * 
 *  VARIOUS GETS FOR COMMON ELEMENTS AND IDs
 *
 */

function isEditorOpened() {
    return $('#editor');
}

function getEditorTabElement() {
    return $('div#content > ul.nav-tabs > li.active > a');
}

function getProjectIdFromTab() {
    return getEditorTabElement().attr('data-projectid');
}

function getFileIdFromTab() {
    return getEditorTabElement().attr('data-pk');
}

function getProjectIDsFromSidebar() {
    var projectIDs = new Array();
    $('.sidebar > ul > li.active > a').each(function() {
        projectIDs.push($(this).attr('data-pk'));
        alert($(this).attr('data-pk'));
    });
    return projectIDs;
}

function getProjectFileIDsFromSidebar(projectId) {
    var fileIDs = new Array();
    var parentUl = getParentUL(projectId);

    parentUl.children('li:not(.active)').each(function() {
        var fileId = $(this).children('a').attr('data-pk');
        fileIDs.push(fileId);
    });
    return fileIDs;
}

/* add job id to editor tab */
function addJobIdToEditorTab(jobId) {
    var tab = getEditorTabElement();

    /* hack to provide job id in tab -> new attribute data-projectid */
    tab.attr('data-jobid', jobId);
}

/* get job idfrom editor tab */
function getJobIdFromEditorTab() {
    var tab = getEditorTabElement();

    /* return attribute data-jobid */
    return tab.attr('data-jobid');
}

/*
 * 
 * NAVBAR AND COMPILATION FUNCTIONS
 * 
 */

/* get available latex environments and show in select */
function createLatexEnvSelect() {

    /* get all latex environments */
    var environments = getLatexEnvironments();
    if (environments) {

        /* create select if there was a valid result */
        var select = $('<select/>').attr('id', 'latexenvironment').addClass('form-control');

        /* append option to select for each latex environments */
        for (var i = 0; i < environments.length; i++) {
            var name = environments[i].value;
            var option = $('<option/>').attr('value', name).text(name);
            select.append(option);
        }

        return select;
    }
}

function createCompilationBar() {
    /* create form */
    var form = $('<form/>').attr('id', 'compilationbar').addClass('navbar-form navbar-right');

    /* create placeholder for compilation time */
    var compileTimeSpan = $('<span/>').attr('id', 'compiletime');

    /* create select for latex environment */
    var select = createLatexEnvSelect();

    /* display buttons only when CompiLaTeX server is online
       and available environments where found */
    if(select){
       /* create buttons */
        var compileButton = $('<button/>').attr('id', 'compile')
                .attr('type', 'button').addClass('btn btn-default').text('Compile');
        var viewPdfButton = $('<button/>').attr('id', 'viewpdf')
                .attr('type', 'button').addClass('btn btn-default').text('View PDF');
        var showLogButton = $('<button/>').attr('id', 'showlog')
                .attr('type', 'button').addClass('btn btn-default').text('Show Log')
                .attr('data-toggle', 'collapse').attr('data-target', 'div#log');

        /* append span, select and buttons to form */
        form.append(compileTimeSpan).append(select).append(compileButton).append(viewPdfButton).append(showLogButton);
    } else {
        /* create label with hint that no compilation can be performed 
           when CompiLaTeX server cannot be reached */
        var icon = $('<i/>').addClass('fa fa-warning');
        var warning = $('<h5/>').text(' Compilation is currently not possible. CompiLaTeX server is not reachable.')
        form.append(warning.prepend(icon));
    }
    
    /* position form above editor, so append form to tab navbar */
    $('ul.nav-tabs').append(form); 
}

/**
 * set compilation time in compilation bar by setting html content of span#compiletime placeholder
 * 
 * @param {type} start start time of compilation
 */
function setCompilationTime(start) {
    var compileTime = (new Date().getTime() - start) / 1000;
    $('#compiletime').html('Compiled in ' + compileTime + ' seconds. <i class="fa fa-clock-o"></i>').after();
}

/**
 * remove compilation bar from DOM
 */
function removeCompilationBar() {
    $('form#compiliationbar').remove();
}

/*
 * 
 * DOM MANIPULATION FOR TABS AND EDITOR
 * 
 */

/* create editor and apply settings */
function createEditor() {
    ace.require("ace/ext/language_tools");
    var aceEditor = ace.edit("editor");
    aceEditor.setTheme("ace/theme/xcode");
    aceEditor.getSession().setUseWrapMode(true);
    aceEditor.getSession().setMode("ace/mode/latex");
    aceEditor.setOptions({
        maxLines: Infinity,
        enableBasicAutocompletion: true,
        enableSnippets: true
    });

    /* disable redo/undo buttons and listener for input changes */
    disableUndoRedo();
    ace.edit("editor").on('input', function() {
        disableUndoRedo();
    });

    /* load type handling script after editor is present */
    $.getScript('assets/js/typehandling.js', function() {
    });
}

/* close editor and remove tab when file was open */
function closeEditor(fileId) {
    /* make sure to unregister client from document */
    unregisterForDocument(fileId);

    var tab = getEditorTabElement();
    if (tab.attr('data-pk') === fileId) {
        $('#editor').remove();
        tab.parents('ul').remove();
    }
}

/* create close button for tab */
function createTabCloseButton() {
    return $('<i/>').attr('id', 'tab-close').attr('title', 'close document').addClass('fa fa-times');
}

/* create tab for file (will be displayed right over the editor) */
function createEditorTab(projectId, fileId, name) {
    /* create unordered list for tab bar -
     * must be on top of editor, so prepend */
    var ul = $('<ul/>')
            .addClass('nav nav-tabs')
            .prependTo($('#content'));

    /* create tab in tab bar */
    var li = $('<li/>')
            .addClass('active')
            .appendTo(ul);

    /* create link with tab name and attributes*/
    var a = $('<a/>')
            .attr('href', '#')
            .attr('id', name + '-tab')
            .attr('data-type', 'text')
            .attr('data-pk', fileId)
            .attr('data-value', name)
            .attr('data-url', '/rest/projects/' + projectId + '/files/' + fileId + '/rename')
            .attr('data-title', 'Enter file name')
            .attr('data-projectid', projectId)
            .text(name)
            .appendTo(li);
    /* hack to provide project id in tab -> new attribute data-projectid */

    /* add close button */
    createTabCloseButton().appendTo(a);
}

/* update editor tab with new name and id's as attributes */
function updateEditorTab(projectId, fileId, name) {
    var tab = getEditorTabElement();

    /* create close button and set atributes */
    tab.text(name).append(createTabCloseButton());
    tab.attr('id', name + '-file')
            .attr('data-pk', fileId)
            .attr('data-url', '/rest/projects/' + projectId + '/files/' + fileId + '/rename')
            .attr('data-projectid', projectId);
    /* hack to provide project id in tab -> new attribute data-projectid */
}

/*
 * 
 * DOM MANIPULATION FOR FILES AND PROJECTS CREATION/DELETION
 * 
 */

/* create new empty project list for sidebar */
function createEmptyProjectList(id, name) {
    /* create unordered list for project */
    var ul = $('<ul/>')
            .addClass('nav nav-sidebar')
            .appendTo($('.sidebar'));

    /* create list item for project name */
    var li = $('<li/>')
            .addClass('active')
            .appendTo(ul);

    /* create link with inline editing options on name */
    var a = $('<a/>')
            .attr('href', '#')
            .attr('id', name + '-project')
            .attr('data-type', 'text')
            .attr('data-pk', id)
            .attr('data-value', name)
            .attr('data-url', '/rest/projects/' + id + '/rename')
            .attr('data-title', 'Enter project name')
            .text(name)
            .appendTo(li);
}

/* create new empty project list for sidebar */
function createNewFileInProjectList(projectId, fileId, name) {
    /* find the right parent ul in sidebar */
    var ul = getParentUL(projectId);

    /* create list item for project name */
    var li = $('<li/>')
            .appendTo(ul);

    /* create link with inline editing options on name */
    var a = $('<a/>')
            .attr('href', '#')
            .attr('id', name + '-file')
            .attr('data-type', 'text')
            .attr('data-pk', fileId)
            .attr('data-value', name)
            .attr('data-url', '/rest/projects/' + projectId + '/files/' + fileId + '/rename')
            .attr('data-title', 'Enter file name')
            .text(name)
            .appendTo(li);
}

/* hightlight the file with the file id as main tex and remove highlights from other files */
function updateMainTexFileHighlighting(projectId, fileId) {
    /* find the right parent ul for project with id in sidebar */
    var ul = getParentUL(projectId);

    /* for all files in list */
    ul.children('li:not(.active)').each(function() {
        var a = $(this).children('a');

        /* find file with id and append i with font icon or remove icon from former set main tex file */
        if (a.attr('data-pk') === fileId) {
            /* prepend! otherwise icon would be behind text */
            a.prepend($('<i/>').addClass('fa fa-star'));
        } else {
            if (a.children('i').hasClass('fa')) {
                a.children('i').remove();
            }
        }
    });
}

/* delete project from sidebar */
function deleteProjectFromSidebar(projectId) {
    /* find the right project in sidebar */
    $('ul > li.active > a').each(function() {
        if ($(this).attr('data-pk') === projectId) {
            /* delete project from sidebar -> a -> li -> ul must be deleted */
            $(this).parent($('li')).parent($('ul')).remove();
        }
    });
}

/* delete file from project list in sidebar */
function deleteFileFromProjectList(projectId, fileId) {
    /* find the right parent ul for project with id in sidebar */
    var ul = getParentUL(projectId);

    /* for all files in list */
    ul.children('li:not(.active)').each(function() {
        var a = $(this).children('a');

        /* find file with id and delete from DOM */
        if (a.attr('data-pk') === fileId) {
            $(this).remove();
        }
    });

    /* close editor and remove tab when file was open */
    closeEditor(fileId);

    /* unregister client from updates when editor was closed and file was deleted */
    unregisterForDocument(fileId);
}

/* find and return the parent ul for project in sidebar */
function getParentUL(projectId) {
    var ul;
    $('ul > li > a').each(function() {
        if ($(this).attr('data-pk') === projectId) {
            ul = $(this).parent($('li')).parent($('ul'));
        }
    });
    return ul;
}

/* add search results to results table */
function listSearchResults(results, query) {
    $('h3.sub-header').text(results.length + ' results found for "' + query + '"');

    /* create table head and empty table body*/
    if (results.length > 0) {
        var thead = $('<thead/>');
        var tbody = $('<tbody/>');
        var tr = $('<tr/>')
                .append($('<th/>').text('Name'))
                .append($('<th/>').text('Content Type'))
                .append($('<th/>').text('Size'))
                .append($('<th/>').text('Changed'))
                .append($('<th/>').text('Main Tex'));
        thead.append(tr);
        $('#searchresults').append(thead).append(tbody);
    }

    /* fill table body with the returned results' attributes */
    for (var i = 0; i < results.length; i++) {
        var date = new Date(results[i].lastChanged).customFormat("on #DD#/#MM#/#YYYY# at #hhh#:#mm#");
        var name = $('<td/>').text(results[i].name);
        var contentType = $('<td/>').text(results[i].contentType);
        var size = $('<td/>').text(formatFileSize(results[i].size));
        var lastChanged = $('<td/>').text(date);
        var mainTex = $('<td/>').text(results[i].mainTex);

        var tr = $('<tr/>')
                .append(name)
                .append(contentType)
                .append(size)
                .append(lastChanged)
                .append(mainTex);

        $('#searchresults > tbody').append(tr);
    }
}


/*
 * 
 * UTILITY FUNCTIONS
 * 
 */

/* check if filename contains an extension that indicates it 
 * is a plain text file or it is a new and empty file */
function isPlainTextFile(name) {
    var isPlainText = false;
    for (var i = 0; i < fileExtensions.length; i++)
    {
//        alert(name +'vs'+ fileExtensions[i]);
        /* set true if any matching extensions/name was found */
        if (name.contains(fileExtensions[i])) {
            isPlainText = true;
        }
    }
    return isPlainText;
}

/* format the file size */
function formatFileSize(bytes) {
    if (typeof bytes !== 'number') {
        return '';
    }

    if (bytes >= 1000000000) {
        return (bytes / 1000000000).toFixed(2) + ' GB';
    }

    if (bytes >= 1000000) {
        return (bytes / 1000000).toFixed(2) + ' MB';
    }

    return (bytes / 1000).toFixed(2) + ' KB';
}

/* utuility method from http://stackoverflow.com/a/4673990/3511357 */
Date.prototype.customFormat = function(formatString) {
    var YYYY, YY, MMMM, MMM, MM, M, DDDD, DDD, DD, D, hhh, hh, h, mm, m, ss, s, ampm, AMPM, dMod, th;
    var dateObject = this;
    YY = ((YYYY = dateObject.getFullYear()) + "").slice(-2);
    MM = (M = dateObject.getMonth() + 1) < 10 ? ('0' + M) : M;
    MMM = (MMMM = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"][M - 1]).substring(0, 3);
    DD = (D = dateObject.getDate()) < 10 ? ('0' + D) : D;
    DDD = (DDDD = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"][dateObject.getDay()]).substring(0, 3);
    th = (D >= 10 && D <= 20) ? 'th' : ((dMod = D % 10) === 1) ? 'st' : (dMod === 2) ? 'nd' : (dMod === 3) ? 'rd' : 'th';
    formatString = formatString.replace("#YYYY#", YYYY).replace("#YY#", YY).replace("#MMMM#", MMMM).replace("#MMM#", MMM).replace("#MM#", MM).replace("#M#", M).replace("#DDDD#", DDDD).replace("#DDD#", DDD).replace("#DD#", DD).replace("#D#", D).replace("#th#", th);

    h = (hhh = dateObject.getHours());
    if (h === 0)
        h = 24;
    if (h > 12)
        h -= 12;
    hh = h < 10 ? ('0' + h) : h;
    AMPM = (ampm = hhh < 12 ? 'am' : 'pm').toUpperCase();
    mm = (m = dateObject.getMinutes()) < 10 ? ('0' + m) : m;
    ss = (s = dateObject.getSeconds()) < 10 ? ('0' + s) : s;
    return formatString.replace("#hhh#", hhh).replace("#hh#", hh).replace("#h#", h).replace("#mm#", mm).replace("#m#", m).replace("#ss#", ss).replace("#s#", s).replace("#ampm#", ampm).replace("#AMPM#", AMPM);
};

/* utility method that provides a contains function */
String.prototype.contains = function(it) {
    return this.indexOf(it) !== -1;
};