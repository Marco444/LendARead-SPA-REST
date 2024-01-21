<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<th>
    <c:url var="sortUrl" value="/sortUserHomeAssets"/>
    <form action="${sortUrl}" method="get" class="assetInstanceSort-form">
        <input type="hidden" name="table" value="${param.table}" />
        <input type="hidden" name="attribute" value="${param.attribute}" />
        <input type="hidden" name="direction" value="${param.sortAttribute ? 'desc' : 'asc'}" />
        <input type="hidden" name="filterValue" value="${param.filterValue}">
        <input type="hidden" name="filterAtribuite" value="${param.filterAtribuite}">
        <input type="hidden" name="currentPage" value="${param.currentPage}">
        <button type="submit" class="assetInstanceSort-button<c:if test='${param.sortAttribute}'> assetInstanceSort-button-selected</c:if>">
            <spring:message code="${param.title}"/>
            <i class="fas fa-arrow-<c:out value='${param.sortAttribute ? "up" : "down"}' />"></i>
        </button>
    </form>
</th>
