<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">

    <jsp:include page="includes/header.jsp" />

    <body>
        <jsp:include page="includes/navbar.jsp" />

        <jsp:include page="includes/sidebar.jsp" />

        <div id="content">
            <h1>Search Results</h1>
            
            <h3 class="sub-header"></h2>
            <div class="table-responsive">
                <table id="searchresults" class="table table-striped">
                </table>
            </div>
        </div>

        <jsp:include page="includes/contextmenus.jsp" />


        <!-- Placed at the end of the document so the pages load faster -->
        <!-- jquery -->
        <script src="assets/lib/jquery/js/jquery-1.10.2.min.js"></script>

        <!-- Bootstrap core JavaScript -->
        <script src="assets/lib/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/lib/bootstrap/js/bootstrap-editable.min.js"></script>

        <!-- own JS -->
        <script src="assets/js/collaboratex.js"></script>
        <script src="assets/js/rest-collaboratex.js"></script>
        <script src="assets/js/rest-compilatex.js"></script>
        <script src="assets/js/contextmenu.js"></script>
        <script src="assets/js/clickhandling.js"></script>
        
        <script>search('<%=request.getParameter("query")%>');</script>
    </body>
</html>
