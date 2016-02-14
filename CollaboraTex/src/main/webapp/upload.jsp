<%-- 
    Document   : upload.jsp
    Created on : 23.04.2014, 14:54:17
    Author     : Daniel Jürges <djuerges@googlemail.com>
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html lang="en">

    <jsp:include page="includes/header.jsp" />

    <body>
        <jsp:include page="includes/navbar.jsp" />

        <jsp:include page="includes/sidebar.jsp" />

        <div id="content">
            <h1>Import Project</h1>

            <div id="fileupload">
                <form id="uploadform" method="post" enctype="multipart/form-data">
                    <div class="input-group">
                        <span class="input-group-addon">Project Name:</span>
                        <input type="text" class="form-control required" name="projectname" value="" placeholder="Name of the New Project">
                    </div>

                    <div class="input-group">
                        <span class="input-group-addon">Share Project:</span>
                        <input type="text" class="form-control" id="tokenfield" name="sharewith" value="" placeholder="Username"/>
                    </div>

                    <div class="fileupload-buttonbar">
                        <label class="fileinput-button">
                            <button id="addfiles" type="button" class="btn btn btn-default fileinput-button">Add Files</button>
                            <input type="file" name="files[]" multiple>
                        </label>
                    </div>

                    <div class="fileupload-content">
                        <ul class="files"></ul>
                    </div>

                    <div class="progress">
                        <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
                            0%
                        </div>
                    </div>

                    <div class="btn-group">
                        <button id="submitfiles" type="submit" class="btn btn btn-default">Upload</button>
                    </div>
                </form>
            </div>

        </div>

        <jsp:include page="includes/contextmenus.jsp" />

        <!-- Placed at the end of the document so the pages load faster -->

        <!-- jQuery + jquery UI -->
        <script src="assets/lib/jquery/js/jquery-1.10.2.min.js"></script>
        <script src="assets/lib/jquery/js/jquery-ui-1.10.4.min.js"></script>

        <!-- Bootstrap core JavaScript -->
        <script src="assets/lib/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/lib/bootstrap/js/bootstrap-editable.min.js"></script>

        <!-- use tokenfield for autocomplete -->
        <script src="assets/lib/tokenfield/js/bootstrap-tokenfield.js"></script>
        <script src="assets/lib/tokenfield/js/scrollspy.js"></script>
        <script src="assets/lib/tokenfield/js/activate.js"></script>

        <!-- own JS -->
        <script src="assets/js/collaboratex.js"></script>
        <script src="assets/js/rest-collaboratex.js"></script>
        <script src="assets/js/rest-compilatex.js"></script>
        <script src="assets/js/contextmenu.js"></script>
        <script src="assets/js/clickhandling.js"></script>

        <!-- jquery-fileupload -->
        <script src="assets/lib/jquery-fileupload/js/vendor/jquery.ui.widget.js"></script>
        <script src="assets/lib/jquery-fileupload/js/load-image.min.js"></script>
        <script src="assets/lib/jquery-fileupload/js/canvas-to-blob.min.js"></script>
        <script src="assets/lib/jquery-fileupload/js/jquery.iframe-transport.js"></script>
        <script src="assets/lib/jquery-fileupload/js/jquery.fileupload.js"></script>
        <script src="assets/lib/jquery-fileupload/js/jquery.fileupload-process.js"></script>
        <script src="assets/lib/jquery-fileupload/js/jquery.fileupload-image.js"></script>
        <script src="assets/lib/jquery-fileupload/js/jquery.fileupload-audio.js"></script>
        <script src="assets/lib/jquery-fileupload/js/jquery.fileupload-video.js"></script>
        <script src="assets/lib/jquery-fileupload/js/jquery.fileupload-validate.js"></script>
        <!-- the main file upload script, contains the upload url -->
        <script src="assets/lib/jquery-fileupload/js/main.js"></script>
    </body>
</html>