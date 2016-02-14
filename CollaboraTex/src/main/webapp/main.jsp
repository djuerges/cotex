<%@page import="de.uniluebeck.collaboratex.session.SessionManager"%>
<%@page import="de.uniluebeck.collaboratex.entity.Project"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">

    <jsp:include page="includes/header.jsp" />

    <body>
        <jsp:include page="includes/navbar.jsp" />
        
        <jsp:include page="includes/sidebar.jsp" />

        <div id="content">
        </div>

        <jsp:include page="includes/contextmenus.jsp" />

        <!-- Placed at the end of the document so the pages load faster -->
        <!-- jquery -->
        <script src="assets/lib/jquery/js/jquery-1.10.2.min.js"></script>

        <!-- jquery typing -->
        <script src="assets/lib/jquery-typing/js/jquery.typing-0.2.0.js"></script>

        <!-- Bootstrap core JavaScript -->
        <script src="assets/lib/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/lib/bootstrap/js/bootstrap-editable.min.js"></script>

        <!-- diff_patch_match lib -->
        <script src="assets/lib/diff_match_patch/js/diff_match_patch.js"></script>

        <!-- own JS -->
        <script src="assets/js/collaboratex.js"></script>
        <script src="assets/js/rest-collaboratex.js"></script>
        <script src="assets/js/rest-compilatex.js"></script>
        <script src="assets/js/contextmenu.js"></script>
        <script src="assets/js/clickhandling.js"></script>
        <script src="assets/js/typehandling.js"></script>

        <!-- ace editor -->
        <script src="assets/lib/ace/ace.js" type="text/javascript" charset="utf-8"></script>
        <script src="assets/lib/ace/ext-language_tools.js"></script>

        <script type="text/javascript" src="/_ah/channel/jsapi"></script>
        <script>
            /* get token for client from session manager */
            var token = '<%= SessionManager.getChannelToken(session.getId())%>';
            var channel = new goog.appengine.Channel(token);
            var socket = channel.open();
            socket.onopen = onOpened;
            socket.onmessage = onMessage;
            socket.onerror = onError;
            socket.onclose = onClose;

            function onOpened() {
            }

            function onMessage(patch) {
                if ($('#editor')) {
                    var dmp = new diff_match_patch();

                    /* apply patch to current editor content */
                    var currentText = ace.edit('editor').getSession().getValue();
                    var patches = dmp.patch_fromText(patch.data);
                    var results = dmp.patch_apply(patches, currentText);

                    /* set patched content as new content */
                    ace.edit('editor').getSession().setValue(results[0]);
                }
            }

            function onError(error) {
            }

            function onClose() {
            }
        </script>
        <jsp:include page="includes/log.jsp" />
    </body>
</html>