<%-- 
    Document   : contextmenus
    Created on : 09.04.2014, 16:34:42
    Author     : Daniel Jürges <djuerges@googlemail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<div id="sidebarContextMenu" class="contextmenu dropdown clearfix">
    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="display:block;position:static;margin-bottom:5px;">
        <li><a id="newproject" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-folder"></i> New Project</a></li>
        <li class="divider"></li>
        <li><a id="importproject" class="contextmenu" tabindex="-1" href="upload.jsp"><i class="fa fa-upload"></i> Import Project</a></li>
    </ul>
</div>

<div id="projectContextMenu" class="contextmenu dropdown clearfix">
    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="display:block;position:static;margin-bottom:5px;">
        <li class="disabled"><a id="shareproject" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-share"></i> Share Project</a></li>
        <li><a id="renameproject" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-pencil"></i> Rename Project</a></li>
        <li><a id="downloadproject" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-download"></i> Download Project</a></li>
        <li class="divider"></li>
        <li><a id="deleteproject" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-trash-o"></i> Delete Project</a></li>
        <li class="divider"></li>
        <li><a id="newfile" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-file-text"></i> New File</a></li>
        <li class="disabled"><a id="importfiles" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-upload"></i> Import Files</a></li>
    </ul>
</div>

<div id="fileContextMenu" class="contextmenu dropdown clearfix">
    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="display:block;position:static;margin-bottom:5px;">
        <li><a id="renamefile" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-pencil"></i> Rename File</a></li>
        <li><a id="setmaintexfile" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-star"></i> Set as Main Tex File</a></li>
        <li class="divider"></li>
        <li><a id="deletefile" class="contextmenu" tabindex="-1" href="#"><i class="fa fa-trash-o"></i> Delete File</a></li>
    </ul>
</div>
