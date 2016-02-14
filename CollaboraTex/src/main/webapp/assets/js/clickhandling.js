/**
 * @author Daniel Jürges <djuerges@googlemail.com>
 * 
 * This js file serves the purpose to register and handle clicks for menu items and so on.
 * It realies heavily on the ACE Editor API, see http://ace.c9.io/#nav=api for it.
 * 
 */

/*
 * 
 * HANDLE PROJECT MENU ITEMS
 * 
 */

/* handle new project for both menu and context menu */
$('a#newproject.menu, a#newproject.contextmenu').on('click', function() {
    createNewProject();
});

/* handle rename project */
$('a#renameproject.menu, a#renameproject.contextmenu').on('click', function() {
    /* TODO: know which project to rename -> modal dialog showing list or getting current projectid from editor? */
    alert('renameproject');
});

/* handle delete project */
$('a#deleteproject.menu').on('click', function() {
    /* TODO: know which project to delete -> modal dialog showing list or getting current projectid from editor? */
//    deleteProject(projectId);
    alert('deleteproject');
});

/* handle share project */
$('a#shareproject.menu').on('click', function() {
    /* TODO: know which project to share -> modal dialog showing list or getting current projectid from editor? */
    alert('shareproject');
});

/* handle import project */
$('a#importproject.menu').on('click', function() {
    window.location.href = 'upload.jsp';
});

/* handle download project */
$('a#downloadproject.menu').on('click', function() {
    /* TODO: know which project to download -> modal dialog showing list or getting current projectid from editor? */
    alert('downloadproject');
});

/* handle new file */
$('a#newfile.menu').on('click', function() {
    /* TODO: know which project to create file in -> modal dialog showing list or getting current projectid from editor? */
    alert('newfile');
});

/* handle rename file */
$('a#renamefile.menu').on('click', function() {
    /* TODO: know which file to rename -> modal dialog showing list or getting current projectid from editor? */
    alert('renamefile');
});

/* handle delete file */
$('a#deletefile.menu').on('click', function() {
    /* TODO: know which file to delete -> modal dialog showing list or getting current projectid from editor? */
    alert('deletefile');
});

/*
 * 
 * HANDLE EDIT MENU ITEMS
 * 
 */

/* handle undo */
$('a#undo.menu').on('click', function() {
    var undoManager = ace.edit('editor').getSession().getUndoManager();
    if (undoManager.hasUndo()) {
        undoManager.undo();
    }
});

/* handle redo */
$('a#redo.menu').on('click', function() {
    var undoManager = ace.edit('editor').getSession().getUndoManager();
    if (undoManager.hasRedo()) {
        undoManager.redo();
    }
});

/* handle cut */
$('a#cut.menu').on('click', function() {
});

/* handle copy */
$('a#copy.menu').on('click', function() {
});

/* handle paste */
$('a#paste.menu').on('click', function() {
});

/* handle select all */
$('a#selectall.menu').on('click', function() {
    ace.edit('editor').selection.selectAll();
});

/* handle select none */
$('a#selectnone.menu').on('click', function() {
    ace.edit('editor').selection.clearSelection();
});

/* handle search and repleditor */
$('a#searchandreplace.menu').on('click', function() {
    /* todo */
});

/*
 * 
 * HANDLE INSERT MENU ITEMS
 * 
 */

/* handle insert figure */
$('a#insertfigure.menu').on('click', function() {
    insertIntoEditor(
            '\\begin{figure}[htbpH!]\n' +
            '\t\\caption{text of the caption}\n' +
            '\t\\centering\n' +
            '\t\t\\includegraphics[width=0.5\\textwidth]{nameofgraphic}\n' +
            '\\end{figure}\n');
});

/* handle insert link */
$('a#insertlink.menu').on('click', function() {
    insertIntoEditor('\\href{http://anyurl}{link text}\n');
});

/* handle insert table */
$('a#inserttable.menu').on('click', function() {
    insertIntoEditor(
            '\\begin{tabular}{ |l|l| }\n' +
            '\t\\hline\n' +
            '\tstuff & stuff \\\\ \\hline\n' +
            '\tstuff & stuff \\\\\n' +
            '\t\\hline\n' +
            '\\end{tabular}\n');
});

/* handle insert bullet point list */
$('a#bulletpointlist.menu').on('click', function() {
    insertIntoEditor(
            '\\begin{itemize}\n' +
            '\t\\item...\n' +
            '\\end{itemize}\n');
});

/* handle insert numbered list */
$('a#numberedlist.menu').on('click', function() {
    insertIntoEditor(
            '\\begin{enumerate}\n' +
            '\t\\item...\n' +
            '\\end{enumerate}\n');
});

/* handle insert chapter */
$('a#insertchapter.menu').on('click', function() {
    insertIntoEditor('\\chapter{name}\n');
});

/* handle insert section */
$('a#insertsection.menu').on('click', function() {
    insertIntoEditor('\\section{name}\n');
});

/* handle insert subsection */
$('a#insertsubsection.menu').on('click', function() {
    insertIntoEditor('\\subsection{name}\n');
});

/* handle insert subsubsection */
$('a#insertsubsubsection.menu').on('click', function() {
    insertIntoEditor('\\subsubsection{name}\n');
});

/* handle insert abstract */
$('a#insertabstract.menu').on('click', function() {
    insertIntoEditor(
            '\\begin{abstract}\n' +
            '\tyour abstract goes here\n' +
            '\\end{abstract}\n');
});

/* handle insert bibliography */
$('a#insertbibliography.menu').on('click', function() {
    insertIntoEditor(
            '\\bibliography{nameofbibliographyfile}\n' +
            '\\bibliographystyle{alpha}\n');
});

/* handle insert list of figures */
$('a#insertlistoffigures.menu').on('click', function() {
    insertIntoEditor('\\listoffigures\n');
});

/* handle insert list of tables */
$('a#insertlistoftables.menu').on('click', function() {
    insertIntoEditor('\\listoftables\n');
});

/* handle insert listings */
$('a#insertlistings.menu').on('click', function() {
    insertIntoEditor('\\lstlistoflistings\n');
});

/*
 * 
 * HANDLE FORMAT MENU ITEMS
 * 
 */

/* handle format bold */
$('a#formatbold.menu').on('click', function() {
    var boldText = formatSelectionOrAddNew('\\textbf', 'bold text');
    insertIntoEditor(boldText);
});

/* handle format italic */
$('a#formatitalic.menu').on('click', function() {
    var italicText = formatSelectionOrAddNew('\\textit', 'italic text');
    insertIntoEditor(italicText);
});

/* handle format underscore */
$('a#formatunderscore.menu').on('click', function() {
    var underscoreText = formatSelectionOrAddNew('\\textt', 'underscored text');
    insertIntoEditor(underscoreText);
});

/* handle format text align left */
$('a#formatleft.menu').on('click', function() {
    var alignLeft = encloseSelectionOrAddNew('flushleft', '...');
    insertIntoEditor(alignLeft);
});

/* handle format text align center */
$('a#formatcenter.menu').on('click', function() {
    var alignCenter = encloseSelectionOrAddNew('center', '...');
    insertIntoEditor(alignCenter);
});

/* handle format text align right */
$('a#formatright.menu').on('click', function() {
    var alignRight = encloseSelectionOrAddNew('flushright', '...');
    insertIntoEditor(alignRight);
});


/*
 * 
 * HANDLE COMMANDS MENU
 * 
 */

/* handle any trigonometric function */
$('a.commands').each(function() {
    $(this).on('click', function() {
        
        /* text of item is command */
        var command = $(this).text();
        insertIntoEditor(command);
    });
});

/*
 * 
 * HANDLE BIBLIOGRAPHY MENU ITEMS
 * 
 */

/* handle bibliography journal article */
$('a#bib-article.menu').on('click', function() {
    insertIntoEditor(
            '@article{Xarticle,\n' +
            '\tauthor = "",\n' +
            '\ttitle = "",\n' +
            '\tjournal = "",\n' +
            '\t% volume = "",\n' +
            '\t% number = "",\n' +
            '\t% pages = "",\n' +
            '\tyear = "XXXX",\n' +
            '\t% month = "",\n' +
            '\t% note = "",\n' +
            '}\n');
});

/* handle bibliography book */
$('a#bib-book.menu').on('click', function() {
    insertIntoEditor(
            '@book{Xbook,\n' +
            '\tauthor = "",\n' +
            '\ttitle = "",\n' +
            '\tpublisher= "",\n' +
            '\t% volume = "",\n' +
            '\t% number = "",\n' +
            '\t% series = "",\n' +
            '\t% address = "",\n' +
            '\t% edition = "",\n' +
            '\tyear = "XXXX",\n' +
            '\t% month = "",\n' +
            '\t% note = "",\n' +
            '}\n');
});

/* handle bibliography conference proceedings */
$('a#bib-conf-proceed.menu').on('click', function() {
    insertIntoEditor(
            '@conference{Xconference,\n' +
            '\tauthor = "",\n' +
            '\ttitle = "",\n' +
            '\tbooktitle = "",\n' +
            '\t% editor = "",\n' +
            '\t% volume = "",\n' +
            '\t% number = "",\n' +
            '\t% series = "",\n' +
            '\t% pages = "",\n' +
            '\t% address = "",\n' +
            '\tyear = "XXXX",\n' +
            '\t% month = "",\n' +
            '\t% publisher= "",\n' +
            '\t% note = "",\n' +
            '}\n');
});

/* handle bibliography online */
$('a#bib-online.menu').on('click', function() {
    insertIntoEditor(
            '@online{Xonline,\n' +
            '\tauthor = "",\n' +
            '\ttitle = "",\n' +
            '\tdate = "",\n' +
            '\turl = "",\n' +
            '}\n');
});

/* handle bibliography thesis */
$('a#bib-thesis.menu').on('click', function() {
    insertIntoEditor(
            '@phdthesis{Xphdthesis,\n' +
            '\tauthor = "",\n' +
            '\ttitle = "",\n' +
            '\tschool = "",\n' +
            '\t% type = "",\n' +
            '\t% address = "",\n' +
            '\tyear = "",\n' +
            '\t%month= "",\n' +
            '\t%note= "",\n' +
            '}\n');
});

/*
 * 
 * HANDLE FILE CLICKS
 * 
 */

/* handle click on file -> load file in editor */
$("*[id*='-file']").each(function() {
    $(this).on('click', function(e) {
        handleClickOnFile($(this));
        return false;
    });
});

/* handle click on file in sidebar */
function handleClickOnFile(element) {
    /* get name and new ids of the file */
    var name = element.text().trim();
    var newProjectId = element.parent('li').siblings('li.active').children('a').attr('data-pk');
    var newFileId = element.attr('data-pk');

    /* handle click if: 
     * 1. we are on the main page (div with id content is present)
     * 2. file is plain tex(t) related
     * 3. no inline-editing is happening currently */
    if (document.getElementById('content') && isPlainTextFile(name) && !$('a').hasClass('editable-open')) {

        /* if there is no editor, create it */
        if (!document.getElementById('editor')) {

            /* append div for editor to content */
            $('<div/>').attr('id', 'editor').appendTo('div#content');

            /* create editor and apply settings */
            createEditor();

            /* create tab for opened file over editor */
            createEditorTab(newProjectId, newFileId, name);

            /* create compile button bar over editor */
            createCompilationBar();

            /* register for new document (needed to receive document updates
             * from server by push through Channel API) */
            registerForDocument(newFileId);

            /* get file content and set as new editor content */
            var newContent = getFileContent(newProjectId, newFileId);
            ace.edit('editor').getSession().setValue(newContent);

            /* if editor was already present another file must have been opened so handle that */
        } else {

            /* get old ids of the file */
            var tab = $('div#content > ul.nav-tabs > li.active > a');
            var oldProjectId = tab.attr('data-projectid');
            var oldFileId = tab.attr('data-pk');

            /* load new, save old content and change tab attributes only if is a different file */
            if (newFileId !== oldFileId) {

                /* register for new document (needed to receive document updates
                 * from server by push through Channel API) */
                unregisterForDocument(oldFileId);
                registerForDocument(newFileId);

                /* get file content and set as new editor content */
                var newContent = getFileContent(newProjectId, newFileId);
                ace.edit('editor').getSession().setValue(newContent);

                /* update tab with new name and attributes */
                updateEditorTab(newProjectId, newFileId, name);
            }
        }
    }
}

/*
 * 
 * HANDLE TAB CLICKS
 * 
 */

/* handle close tab */
$(document).on('click', '#tab-close', function() {
    var tab = $('div#content > ul.nav-tabs > li.active > a');
    var fileId = tab.attr('data-pk');
    var projectId = tab.attr('data-projectid');

    /* save existing file content in editor back to file before changing it */
    var content = ace.edit('editor').getSession().getValue();
    updateFileContent(projectId, fileId, content);

    /* remove compilation bar */
    removeCompilationBar();

    /* close editor */
    closeEditor(fileId);
});

/*
 * 
 * HANDLE COMPILATION RELATED CLICKS
 * 
 */

/* handle compile */
$(document).on('click', 'button#compile', function() {
    var start = new Date().getTime();

    /* get latex environment for compilation from select */
    var latexEnvironment = $('#latexenvironment option:selected').text();

    /* create job by posting to CompiLaTeX service */
    var job = createNewJob();

    /* get project files */
    var projectId = getProjectIdFromTab();
    var fileIDs = getProjectFileIDsFromSidebar(projectId);
    for (var i = 0; i < fileIDs.length; i++) {

        /* add them to CompiLaTeX Job by posting to its service */
        var fileId = fileIDs[i];
        var fileMetaData = getFileMetaData(projectId, fileId);
        var content = getFileContent(projectId, fileId);
        var createdFile = addFile(job.id, content, fileMetaData.name, fileMetaData.mainTex);
    }

    /* compile */
    compileJob(job.id, latexEnvironment);
    var htmlLog = getHtmlJobLog(job.id);

    /* append log to div#log */
    appendLogToPage(htmlLog);

    /* set compile time in compilation bar */
    setCompilationTime(start);
    
    /* RATHER UGLY HACK TO SAVE JOB IB */
    addJobIdToEditorTab(job.id);
});

/* handle view PDF */
$(document).on('click', 'button#viewpdf', function() {

    /* RATHER UGLY HACK TO GET JOB IB */
    var jobId = getJobIdFromEditorTab();

    /* open PDF viewer */
    window.open('/pdfviewer.jsp?jobId=' + jobId); //  + job.id
});


/* handle show log */
$(document).on('click', 'button#showlog', function() {

    /* toggle text when log shown or not */
    $(this).text(function(i, text) {
        return text.trim() === 'Show Log' ? 'Hide Log' : 'Show Log';
    });
});



/*
 * 
 * UTILITY FUNCTIONS
 * 
 */

/**
 * find line in log containing filename, page number and file size
 * 
 * @param {String} htmlLog compilation log with HTML annotations
 * @returns {String} line containing filename, page number and file size of the created pdf
 */
function findOutputLine(htmlLog) {
    return searchRegex(/^Output written on(.*)$/gm, htmlLog)[0];
}

/*  */

/**
 * search regex in given string and return all results as array
 * 
 * @param {String} regex Regular Expression that will be applied
 * @param {String} string that will be search for the RegEx
 * @returns {searchRegex.matches|Array}
 */
function searchRegex(regex, string) {
    var matches = [], found;
    while (found = regex.exec(string)) {
        matches.push(found[0]);
    }
    return matches;
}

/* insert text into editor */
function insertIntoEditor(text) {
    if ($('#editor').length) {
        ace.edit('editor').insert(text);
    }
}

/* return formatted text if there is is a selection, 
 * otherwise insert new command with example text */
function formatSelectionOrAddNew(texCommand, placeholderText) {
    var text;
    var selection = ace.edit('editor').selection;
    if (selection.isEmpty()) {
        text = texCommand + '{' + placeholderText + '}';
    } else {
        text = formatSelectedText(texCommand);
    }
    return text;
}

/* return formated selection */
function formatSelectedText(texCommand) {
    var selectionRange = ace.edit('editor').getSelectionRange();
    var selectedText = ace.edit('editor').session.getTextRange(selectionRange);
    return texCommand + '{' + selectedText + '}';
}

/* when something needs to be enclosed with begin and end */
function encloseSelectionOrAddNew(texCommand, placeholderText) {
    var text;
    var selection = ace.edit('editor').selection;
    if (selection.isEmpty()) {
        text = encloseSelectedText(texCommand, placeholderText);
    } else {
        var selectionRange = ace.edit('editor').getSelectionRange();
        var selectedText = ace.edit('editor').session.getTextRange(selectionRange);
        text = encloseSelectedText(texCommand, selectedText);
    }
    return text;
}

/* return enclosed selection */
function encloseSelectedText(texCommand, text) {
    return '\\begin{' + texCommand + '}\n' + '\t' + text + '\n' + '\\end{' + texCommand + '}\n';
}

/**
 * append the HTML annotated logfile to log div and 
 * set headlines with summary and file stats
 * 
 * @param {String} htmlLog logfile with HTML annotations
 */
function appendLogToPage(htmlLog) {
    /* append log and stats to div */
    $('div#log').html(htmlLog);

    /* prepend number of errors and warnings */
    var errorWarningText = getErrorWarningText(htmlLog);
    $('<h4/>').html(errorWarningText).prependTo('div#log');

    /* prepend documents stats */
    var stats = getDocumentStats(htmlLog);
    if (stats) {
        $('<h4/>').text(stats).prependTo('div#log');
    }

    /* prepend filename */
    var filename = getFilename(htmlLog);
    if (filename) {
        $('<h3/>').text(filename).prependTo('div#log');
    }
}

/**
 * create text for number of errors and warnings
 *
 * @param {String} htmlLog compilation log with HTML annotations
 * @returns {String} string with HTML annotations for number of errors and warnings
 */
function getErrorWarningText(htmlLog) {
    var text = '';
    /* error and warnings numbers */
    var numberOfWarnings = (htmlLog.match(/class="warning"/g) || []).length;
    var numberOfErrors = (htmlLog.match(/class="error"/g) || []).length;

    /* use singular if there was just one, else plural */
    if (numberOfErrors === 1) {
        text += '<span class="error">' + numberOfErrors + ' Error</span>';
    } else if (numberOfErrors >= 1) {
        text += '<span class="error">' + numberOfErrors + ' Errors</span>';
    }
    /* if there was both concatenate with 'and' */
    if (numberOfErrors >= 1 && numberOfWarnings >= 1) {
        text += ' and ';
    }
    if (numberOfWarnings === 1) {
        text += '<span class="warning">' + numberOfWarnings + ' Warning</span>';
    } else if (numberOfWarnings >= 1) {
        text += '<span class="warning">' + numberOfWarnings + ' Warnings</span>';
    }

    return text;
}

/**
 * get the filename of the created pdf
 * PLEASE NOTE: can return NaN if compilation failed, 
 * so if-check this result first before using it
 * 
 * @param {String} htmlLog compilation log with HTML annotations
 * @returns {searchRegex.matches|Array} filename the name of the created pdf
 */
function getFilename(htmlLog) {
    /* get line with filename, page number and file size */
    var outputLine = findOutputLine(htmlLog);

    /* filename must be someword.pdf */
    var filename = searchRegex(/.\w+.pdf/gm, outputLine);
    return filename;
}

/**
 * parse logfile and get the document stats as text
 * PLEASE NOTE: can return NaN if compilation failed, 
 * so if-check this result first before using it
 * 
 * @param {String} htmlLog compilation log with HTML annotations
 * @returns {String} text containg the documents stats
 */
function getDocumentStats(htmlLog) {
    /* get line with filename, page number and file size */
    var outputLine = findOutputLine(htmlLog);

    /* get numbers from line, first being page number and second file size */
    var numbers = searchRegex(/\d+/gm, outputLine);
    var numberOfPages = parseInt(numbers[0]);
    var fileSize = parseInt(numbers[1]);

    if (numbers && numberOfPages && fileSize) {
        return '(' + numberOfPages + ' Pages, ' + formatFileSize(fileSize) + ')';
    } else {
        return;
    }
}