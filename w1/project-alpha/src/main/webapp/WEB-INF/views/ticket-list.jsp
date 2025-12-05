<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- Ticket 列表内容 -->
<div id="ticketListContent">
    <!-- 头部 -->
    <div class="header">
        <h1>工单管理</h1>
        <div style="display: flex; gap: 12px;">
            <a href="${pageContext.request.contextPath}/tickets/new" class="btn btn-primary">新建 Ticket</a>
        </div>
    </div>

        <!-- 消息提示（会被 JavaScript 移动到全局消息容器） -->
        <c:if test="${not empty message}">
            <div class="message" style="display: none;" data-message="${message}"></div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error" style="display: none;" data-message="${error}"></div>
        </c:if>

        <!-- 筛选区 -->
        <div class="filter-section">
            <form method="get" action="${pageContext.request.contextPath}/tickets">
                <div class="filter-group">
                    <input type="text" name="keyword" placeholder="搜索标题..." value="${keyword}">
                    <select name="tagId">
                        <option value="">全部标签</option>
                        <c:forEach items="${allTags}" var="tag">
                            <option value="${tag.id}" ${currentTagId == tag.id ? 'selected' : ''}>
                                ${tag.name}
                            </option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary">搜索</button>
                    <a href="${pageContext.request.contextPath}/tickets" class="btn btn-secondary">重置</a>
                </div>
            </form>
        </div>

        <!-- Ticket 列表 -->
        <div class="table-container">
            <c:choose>
                <c:when test="${empty tickets}">
                    <div class="empty-state">
                        <div class="empty-state-icon">📋</div>
                        <h3>暂无 Ticket 数据</h3>
                        <p>创建你的第一个 Ticket 开始使用吧</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>标题</th>
                                <th>状态</th>
                                <th>标签</th>
                                <th>创建时间</th>
                                <th>更新时间</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${tickets}" var="ticket">
                                <tr class="${ticket.status == 1 ? 'ticket-completed' : ''}">
                                    <td>${ticket.title}</td>
                                    <td>
                                        <span class="ticket-status ${ticket.status == 1 ? 'completed' : 'open'}">
                                            ${ticket.status == 1 ? '已完成' : '未完成'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="tag-list">
                                            <c:forEach items="${ticket.tags}" var="tag">
                                                <span class="tag" style="background-color: ${tag.color != null ? tag.color : '#86868b'}; color: ${tag.color != null && (tag.color == '#ffffff' || tag.color == '#f5f5f7') ? '#1d1d1f' : '#ffffff'};">
                                                    ${tag.name}
                                                </span>
                                            </c:forEach>
                                        </div>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${ticket.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${ticket.updatedAt}" pattern="yyyy-MM-dd HH:mm"/>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/tickets/${ticket.id}/edit" 
                                               class="btn btn-sm btn-primary">编辑</a>
                                            <c:choose>
                                                <c:when test="${ticket.status == 0}">
                                                    <form method="post" 
                                                          action="${pageContext.request.contextPath}/tickets/${ticket.id}/complete" 
                                                          style="display: inline;">
                                                        <button type="submit" class="btn btn-sm btn-success">完成</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form method="post" 
                                                          action="${pageContext.request.contextPath}/tickets/${ticket.id}/reopen" 
                                                          style="display: inline;">
                                                        <button type="submit" class="btn btn-sm btn-secondary">取消完成</button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                            <form method="post" 
                                                  action="${pageContext.request.contextPath}/tickets/${ticket.id}/delete" 
                                                  style="display: inline;"
                                                  onsubmit="return confirm('确认要删除该 Ticket 吗？此操作不可恢复。');">
                                                <button type="submit" class="btn btn-sm btn-danger">删除</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 分页 -->
        <c:if test="${not empty pageInfo && pageInfo.total > 0}">
            <div class="pagination">
                <c:if test="${pageInfo.hasPrevious()}">
                    <a href="${pageContext.request.contextPath}/tickets?page=${pageInfo.page - 1}&keyword=${keyword}&tagId=${currentTagId}">上一页</a>
                </c:if>
                <c:if test="${!pageInfo.hasPrevious()}">
                    <span class="disabled">上一页</span>
                </c:if>

                <c:forEach begin="1" end="${pageInfo.totalPages}" var="p">
                    <c:choose>
                        <c:when test="${p == pageInfo.page}">
                            <span class="current">${p}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/tickets?page=${p}&keyword=${keyword}&tagId=${currentTagId}">${p}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${pageInfo.hasNext()}">
                    <a href="${pageContext.request.contextPath}/tickets?page=${pageInfo.page + 1}&keyword=${keyword}&tagId=${currentTagId}">下一页</a>
                </c:if>
                <c:if test="${!pageInfo.hasNext()}">
                    <span class="disabled">下一页</span>
                </c:if>

                <span style="margin-left: 20px;">
                    共 ${pageInfo.total} 条，第 ${pageInfo.page}/${pageInfo.totalPages} 页
                </span>
            </div>
        </c:if>
    </div>
</div>

