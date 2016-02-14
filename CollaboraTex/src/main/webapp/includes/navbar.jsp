<%-- 
    Document   : navbar
    Created on : 09.04.2014, 16:31:13
    Author     : Daniel Jürges <djuerges@googlemail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#collaboratex-navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <img id="logo" src="assets/img/uni-logo.png" alt="University of L&uuml;beck">
            <a class="navbar-brand" href="main.jsp">Collabora<span class="tex">T<sub>e</sub>X</span></a>
            <a id="api" href="/docs/index.html"><sup>API</sup></a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="collaboratex-navbar">
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-files-o"></i> Project <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a id="newproject" class="menu" href="#"><i class="fa fa-file"></i> New</a></li>
                        <li class="disabled"><a id="renameproject" class="menu" href="#"><i class="fa fa-pencil"></i> Rename</a></li>
                        <li class="disabled"><a id="deleteproject" class="menu" href="#"><i class="fa fa-trash-o"></i> Delete</a></li>
                        <li class="divider"></li>
                        <li class="disabled"><a id="shareproject" class="menu" href="#"><i class="fa fa-share"></i> Share</a></li>
                        <li class="divider"></li>
                        <li><a id="importproject" class="menu" href="upload.jsp"><i class="fa fa-upload"></i> Import</a></li>
                        <li class="disabled"><a id="downloadproject" class="menu" href="#"><i class="fa fa-download"></i> Download</a></li>
                        <li class="divider"></li>
                        <li class="disabled"><a id="newfile" class="menu" href="#"><i class="fa fa-file-text"></i> New File</a></li>
                        <li class="disabled"><a id="importpfiles" class="menu" href="#"><i class="fa fa-upload"></i> Import Files</a></li>
                        <li class="disabled"><a id="renamefile" class="menu" href="#"><i class="fa fa-pencil"></i> Rename File</a></li>
                        <li class="disabled"><a id="deletefile" class="menu" href="#"><i class="fa fa-trash-o"></i> Delete File</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-edit"></i> Edit <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a id="undo" class="menu" href="#"><i class="fa fa-undo"></i> Undo</a></li>
                        <li><a id="redo" class="menu" href="#"><i class="fa fa-repeat"></i> Redo</a></li>
                        <li class="divider"></li>
                        <li class="disabled"><a id="cut" class="menu" href="#"><i class="fa fa-cut"></i> Cut</a></li>
                        <li class="disabled"><a id="copy" class="menu" href="#"><i class="fa fa-copy"></i> Copy</a></li>
                        <li class="disabled"><a id="paste" class="menu" href="#"><i class="fa fa-paste"></i> Paste</a></li>
                        <li class="divider"></li>
                        <li><a id="selectall" class="menu" href="#"><i class="fa fa-square-o"></i> Select all</a></li>
                        <li><a id="selectnone" class="menu" href="#"><i class="fa fa-square"></i> Select none</a></li>
                        <li class="divider"></li>
                        <li class="disabled"><a id="searchandreplace" class="menu" href="#"><i class="fa fa-search"></i> Search and replace</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-paperclip"></i> Insert <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a id="insertfigure" class="menu" href="#"><i class="fa fa-picture-o"></i> Figure</a></li>
                        <li><a id="insertlink" class="menu" href="#"><i class="fa fa-link"></i> Link</a></li>
                        <li><a id="inserttable" class="menu" href="#"><i class="fa fa-table"></i> Table</a></li>
                        <li class="divider"></li>
                        <li><a id="bulletpointlist" class="menu" href="#"><i class="fa fa-list-ul"></i> Bullet point list</a></li>
                        <li><a id="numberedlist" class="menu" href="#"><i class="fa fa-list-ol"></i> Numbered list</a></li>
                        <li class="divider"></li>
                        <li><a id="insertchapter" class="menu" href="#">Chapter</a></li>
                        <li class="dropdown-submenu">
                            <a tabindex="-1" class="menu" href="#">Sections</a>
                            <ul class="dropdown-menu">
                                <li><a id="insertsection" class="menu" href="#">Section</a></li>
                                <li><a id="insertsubsection" class="menu" href="#">Subsection</a></li>
                                <li><a id="insertsubsubsection" class="menu" href="#">Subsubsection</a></li>
                            </ul>
                        </li>
                        <li class="divider"></li>
                        <li><a id="insertabstract" class="menu" href="#">Abstract</a></li>
                        <li><a id="insertbibliography" class="menu" href="#">Bibliography</a></li>
                        <li><a id="insertlistoffigures" class="menu" href="#">List of Figures</a></li>
                        <li><a id="insertlistoftables" class="menu" href="#">List of Tables</a></li>
                        <li><a id="insertlistings" class="menu" href="#">Listings</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-font"></i> Format <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a id="formatbold" class="menu" href="#"><i class="fa fa-bold"></i> Bold</a></li>
                        <li><a id="formatitalic" class="menu" href="#"><i class="fa fa-italic"></i> Italic</a></li>
                        <li><a id="formatunderscore" class="menu" href="#"><i class="fa fa-underline"></i> Underscore</a></li>
                        <li class="divider"></li>
                        <li><a id="formatleft" class="menu" href="#"><i class="fa fa-align-left"></i> Align Left</a></li>
                        <li><a id="formatcenter" class="menu" href="#"><i class="fa fa-align-center"></i> Center</a></li>
                        <li><a id="formatright" class="menu" href="#"><i class="fa fa-align-right"></i> Align Right</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-slack"></i> Commands <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <!-- include commands sub menus -->
                        <jsp:include page="commands.jsp" />
                    </ul>
                </li>

                <li class="dropdown">
                    <a class="menu" href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-book"></i> Bibliography <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a id="bib-article" class="menu" href="#">Article</a></li>
                        <li><a id="bib-book" class="menu" href="#">Book</a></li>
                        <li><a id="bib-conf-proceed" class="menu" href="#">Conference Proceedings</a></li>
                        <li><a id="bib-online" class="menu" href="#">Online Resource</a></li>
                        <li><a id="bib-thesis" class="menu" href="#">Thesis</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="nav navbar-nav navbar-right" role="profile">
                <li class="dropdown">
                    <a class="menu" href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> Username <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li class="disabled"><a class="menu" href="#"><i class="fa fa-gear"></i> Settings</a></li>
                        <li class="disabled"><a class="menu" href="#"><i class="fa fa-power-off"></i> Logout</a></li>
                    </ul>
                </li>
            </ul>



            <form class="navbar-form navbar-right" role="search" method="post" action="../searchresults.jsp">
                <div class="form-group">
                    <input type="text" class="form-control" name="query" value="" placeholder="Search">
                </div>
                <button id="search" type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
            </form>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
