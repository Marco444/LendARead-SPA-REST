<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-aFq/bzH65dt+w6FI2ooMVUpc+21e0SRygnTpmBvdBgSdnuTN7QbdgL+OapgHtvPp" crossorigin="anonymous">
    <link href="<c:url value="/static/css/main.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/static/css/index.css"/>" rel="stylesheet"/>
    <script src="<c:url value="/static/javaScript/topbar.js"/>"></script>
    <script src="<c:url value="/static/javaScript/discovery.js"/>"></script>
    <link href="<c:url value="/static/css/bookCard.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/static/css/searchBar.css"/>" rel="stylesheet"/>
    <title>Lend a read</title>
    <link rel="shortcut icon" href="<c:url value='/static/images/favicon-claro.ico'/>" type="image/x-icon">
</head>
<body data-path="${path}" class = "body-class" >
<!-- Esto va a cambiar es un mockUp -->
<jsp:include page="../components/navBar.jsp"/>
<jsp:include page="../components/snackbarComponent.jsp"/>
<div class="main-class">

    <div class="container">
        <div class="row height d-flex justify-content-center align-items-center">
            <div class="">
                <div class="form">
                    <i class="fa fa-search"></i>
                    <input type="text" class="form-control form-input" placeholder="Search anything...">
                </div>
            </div>
        </div>
    </div>


    <div class="container-row">
        <div class="container-column" style="flex: 0 0 15%; margin: 10px;">

            <h5><spring:message code="discovery.filters.author"/></h5>
            <ul>
                <c:choose>
                    <c:when test="${authorsFiltered.size() <= 0}">
                        <c:forEach var="author" items="${authors}" varStatus="status">
                            <input class="form-check-input" type="checkbox" value="" id="author-${status.index}">
                            <label class="form-check-label authorLabel" for="author-${status.index}" id="author-${status.index}-label"><c:out value="${author}"/></label>
                            <br>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="author" items="${authorsFiltered}" varStatus="status">
                            <ul class="list-group">
                                <li class="list-group-item author-filtered-item"><c:out value="${author}"/></li>
                            </ul>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
            <h5><spring:message code="discovery.filters.language"/></h5>
            <ul>
                <c:choose>
                    <c:when test="${languagesFiltered.size() <= 0}">
                        <c:forEach var="language" items="${languages}" varStatus="status">
                            <input class="form-check-input" type="checkbox" value="" id="language-${status.index}">
                            <label class="form-check-label languageLabel" for="language-${status.index}" id="language-${status.index}-label"><c:out value="${language}"/></label>
                            <br>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="language" items="${languagesFiltered}" varStatus="status">
                            <ul class="list-group">
                                <li class="list-group-item language-filtered-item"><c:out value="${language}"/></li>
                            </ul>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
            <h5><spring:message code="discovery.filters.physicalCondition"/></h5>
            <ul>
                <c:choose>
                    <c:when test="${physicalConditionsFiltered.size() <= 0}">
                        <c:forEach var="physicalCondition" items="${physicalConditions}" varStatus="status">
                            <input class="form-check-input" type="checkbox" value="" id="physicalCondition-${status.index}">
                            <label class="form-check-label physicalConditionLabel" for="physicalCondition-${status.index}" id="physicalCondition-${status.index}-label"><c:out value="${physicalCondition}"/></label>
                            <br>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="physicalCondition" items="${physicalConditionsFiltered}" varStatus="status">
                            <ul class="list-group">
                                <li class="list-group-item physicalCondition-filtered-item"><c:out value="${physicalCondition}"/></li>
                            </ul>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>

            <c:url	value="/discovery"	var="discoveryPageUrl"	/>
            <form:form method="get" action="${discoveryPageUrl}" modelAttribute="searchFilterSortForm" id="springForm">
                <input class="btn btn-light" type="submit" value="submit" id="submit-filter"/>
                <input type ="hidden" name="currentPage" id="currentPageID" value="${page}"/>
            </form:form>

        </div>

        <div class="container-column" style="flex: 0 1 85%;">
            <c:if test="${books.size() > 0}">
                <div class="container-row-wrapped" style="margin-top: 25px; width: 100%;">
                    <h1><spring:message code="discovery.title"/></h1>
                </div>

                <div class="container-row-wrapped" style="margin-top: 50px; width: 100%;">
                    <c:forEach var="book" items="${books}">
                        <% request.setCharacterEncoding("utf-8"); %>
                        <jsp:include page="../components/bookCard.jsp">
                            <jsp:param name="id" value="${book.id}"/>
                            <jsp:param name="bookTitle" value="${book.book.name}"/>
                            <jsp:param name="bookAuthor" value="${book.book.author}"/>
                            <jsp:param name="imageId" value="${book.imageId}"/>
                        </jsp:include>
                    </c:forEach>
                </div>

                <div class="container-row-wrapped" style="margin-top: 25px; width: 100%;">
                    <div>
                        <nav aria-label="Page navigation example">
                            <ul class="pagination justify-content-center">
                                <li class="page-item">
                                    <button type="button" class="btn ${previousPage ? "" : "disabled"}" id="previousPageButton"><spring:message code="paginationButton.previous" /></button>
                                </li>

                                <li class="page-item">
                                    <button type="button" class="btn ${nextPage ? "" : "disabled"}" id="nextPageButton"> <spring:message code="paginationButton.next"/> </button>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>

            </c:if>
            <c:if test="${books.size() <= 0}">
                <div class="container-row-wrapped" style="margin-top: 50px; width: 100%;">
                    <h1><spring:message code="discovery.noBooks" /></h1>
                </div>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>
