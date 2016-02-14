/**
 * @author Daniel Jürges <djuerges@googlemail.com>
 * 
 * This js file serves the purpose to handle typing in the editor.
 * It calculates a patch for the changes made to the text and sends it to the server.
 * 
 */

/* assure diff_match_patch script is loaded so it can be used now */
$.getScript('assets/lib/diff_match_patch/js/diff_match_patch.js', function() {

    /* define what happends when typing starts/ends in the editor */
    $(document).ready(function() {
        var originalText;

        /* save editor text on typing start and send changes to server on typing stop */
        $('#editor > textarea').typing({
            start: function() {
                originalText = ace.edit('editor').getSession().getValue();
            },
            stop: function() {
                sendChangesToServer(originalText);
            },
            delay: 400
        });
    });
    
    /* diff current with original text, make patch and send to server */
    function sendChangesToServer(originalText) {
        var dmp = new diff_match_patch();



        /* SHOW DIFFED CONTENT BELOW EDITOR FOR TESTING S*/
        var d = dmp.diff_main(originalText, ace.edit('editor').getSession().getValue());
        dmp.diff_cleanupSemantic(d);
        var ds = dmp.diff_prettyHtml(d);
        $('#content').append(ds).append('<hr/>').append('<hr/>');



        /* calculate diff from original and current text */
        var currentText = ace.edit('editor').getSession().getValue();
        var diff = dmp.diff_main(originalText, currentText, true);

        /* clean up if necessary */
        if (diff.length > 2) {
            dmp.diff_cleanupSemantic(diff);
        }

        /* make patch from diff and original and current text */
        var patch_list = dmp.patch_make(originalText, currentText, diff);
        var patch = dmp.patch_toText(patch_list);


        /* get ids from editor tab attributes */
        var tab = $('div#content > ul.nav-tabs > li.active > a');
        var projectId = tab.attr('data-projectid');
        var fileId = tab.attr('data-pk');

        /* send patch to server */
        patchFileContent(projectId, fileId, patch);
    }
});