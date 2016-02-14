<%-- 
    Document   : sidebar
    Created on : 09.04.2014, 16:33:03
    Author     : Daniel Jürges <djuerges@googlemail.com>
--%>
<%@page import="java.util.List"%>
<%@page import="de.uniluebeck.collaboratex.dto.ProjectDTO"%>
<%@page import="de.uniluebeck.collaboratex.dao.ProjectDAO"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <h3>Projects</h3>
            <%

                /* instantiate new DAO */
                ProjectDAO dao = new ProjectDAO();

                /* find all project in db */
                List<ProjectDTO> projectDTOs = dao.findAll();
                request.setAttribute("projectDTOs", projectDTOs);

            %>

            <c:forEach items="${projectDTOs}" var="projectDTO">
                <ul class="nav nav-sidebar">
                    <li class="active"><a href="#" id="${projectDTO.name}-project" data-type="text" data-pk="${projectDTO.key.id}" data-url="/rest/projects/${projectDTO.key.id}/rename" data-title="Enter file name">${projectDTO.name}</a></li>
                    <hr>
                    <c:forEach items="${projectDTO.files}" var="projectFileDTO">
                        <li><a href="#" id="${projectFileDTO.name}-file" data-type="text" data-pk="${projectFileDTO.key.id}" data-url="/rest/projects/${projectDTO.key.id}/files/${projectFileDTO.key.id}/rename" data-title="Enter file name">
                            <c:if test="${projectFileDTO.mainTex}">
                                <i class="fa fa-star"></i>
                            </c:if>
                            ${projectFileDTO.name}
                        </a></li>
                    </c:forEach>
                </ul>
            </c:forEach>
        </div>
    </div>
</div>