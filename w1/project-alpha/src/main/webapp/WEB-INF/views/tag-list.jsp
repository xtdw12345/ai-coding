<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 标签列表内容 -->
<div id="tagListContent">
    <!-- 头部 -->
    <div class="header">
        <h1>标签管理</h1>
        <div style="display: flex; gap: 12px;">
            <a href="${pageContext.request.contextPath}/tags/new" class="btn btn-primary">新建标签</a>
        </div>
    </div>

        <!-- 消息提示（会被 JavaScript 移动到全局消息容器） -->
        <c:if test="${not empty message}">
            <div class="message" style="display: none;" data-message="${message}"></div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error" style="display: none;" data-message="${error}"></div>
        </c:if>

        <!-- 标签列表 -->
        <div class="table-container">
            <c:choose>
                <c:when test="${empty tags}">
                    <div class="empty-state">
                        <div class="empty-state-icon">🏷️</div>
                        <h3>暂无标签数据</h3>
                        <p>创建你的第一个标签开始使用吧</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>标签名称</th>
                                <th>颜色</th>
                                <th>创建时间</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${tags}" var="tag">
                                <tr>
                                    <td>${tag.id}</td>
                                    <td>
                                        <span class="tag" style="background-color: ${tag.color != null ? tag.color : '#86868b'}; color: ${tag.color != null && (tag.color == '#ffffff' || tag.color == '#f5f5f7') ? '#1d1d1f' : '#ffffff'};">
                                            ${tag.name}
                                        </span>
                                    </td>
                                    <td>
                                        <div style="display: flex; align-items: center; gap: 8px;">
                                            <div style="width: 24px; height: 24px; border-radius: 6px; background-color: ${tag.color != null ? tag.color : '#86868b'}; border: 1px solid var(--apple-border);"></div>
                                            <span style="font-family: 'SF Mono', 'Monaco', 'Courier New', monospace; font-size: 13px; color: var(--apple-text-secondary);">
                                                ${tag.color != null ? tag.color : '#86868b'}
                                            </span>
                                        </div>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${tag.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/tags/${tag.id}/edit" 
                                               class="btn btn-sm btn-primary">编辑</a>
                                            <form method="post" 
                                                  action="${pageContext.request.contextPath}/tags/${tag.id}/delete" 
                                                  style="display: inline;"
                                                  onsubmit="return confirm('确认要删除该标签吗？如果该标签正在被 Ticket 使用，删除将失败。');">
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
    </div>
</div>

