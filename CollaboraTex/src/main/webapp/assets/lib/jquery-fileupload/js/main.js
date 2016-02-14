/*
 * based on the jQuery File Upload Plugin JS Example 8.9.1
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2014, Daniel Jürges
 */

/* global $, window */

$(function() {
    /* pointing to the path that getFileUploadUrl in ProjectFileService has */
    var getFileUploadUrlPath = '/rest/projects/uploadurl';
    /* activate the plugin */
    $('#fileupload').fileupload({
        /* upload all files in a single request, otherwise GAE couldn't match 
         *  files to a single project when having a request for each file */
        singleFileUploads: false,
        add: function(e, data) {

            /* add every file to the list of added files */
            for (var i = 0; i < data.files.length; i++) {
                var li = $('<li/>').appendTo('.files');
                var italic = $('<i/>').text(formatFileSize(data.files[i].size));
                var p = $('<p/>').append(data.files[i].name).append(italic).appendTo(li);
            }

            /* save item so it can be access within inner click function later */
            var $item = $(this);
            
            /* only submit files when clicking on upload button */
            data.context = $('#submitfiles').click(function() {
                
                /* submit only if form is filled out */
                if(isFormFilledOut()){
                    
                    /* remove add-files-button so no more files can be added to the form */
                    $('#addfiles').hide();

                    /* remove upload-button so files cannot be uploaded multiple times */
                    $('#submitfiles').hide();

                    /* send files */
                    $.get(getFileUploadUrlPath + '?' + new Date().getTime(), function(url) {
                        data.url = url;
                        $item.fileupload('send', data);
                    });
                }
                return false;
            });
            
            /* assure progess bar is now visible */
            $('.progress').show();
        },
        send: function(e, data) {
            /* serialize other form fields and append to data */
            var formElements = $('#uploadform').serializeArray();
            $.each(formElements, function(index, formElement) {
                data.data.append(formElement.name, formElement.value);
            });
        },
        progress: function(e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);

            /* when a single file was successfully uploaded, mark it with a check mark icon */
            if(progress === 100){
                $('#fileupload li').each(function() {
                    $('<span/>').appendTo($(this));
                });
            }
        },
        progressall: function(e, data) {
            /* update progress bar continuously for all uploads */
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('.progress-bar').css('width', progress + '%').text(progress + '%');
        }
    });

    /* change placeholder on form fields if they are not filled out */
    function isFormFilledOut() {
        var isFormValid = true;

        /* check for each text field */
        $(".required").each(function() {
            if ($.trim($(this).val()).length === 0) {
                $(this).attr('placeholder', 'Please fill out this field before submitting');
                isFormValid = false;
            }
            else {
                $(this).removeClass("highlight");
            }
        });
        return isFormValid;
    }
});