<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Ticket 表单内容 -->
<div id="ticketFormContent">
    <div class="container">
        <!-- 头部 -->
        <div class="header">
            <h1>
                <c:choose>
                    <c:when test="${not empty ticket}">编辑 Ticket</c:when>
                    <c:otherwise>新建 Ticket</c:otherwise>
                </c:choose>
            </h1>
            <a href="#" class="btn btn-secondary" onclick="window.dashboard.loadPage('tickets'); return false;">返回列表</a>
        </div>

        <!-- 消息提示 -->
        <c:if test="${not empty message}">
            <div class="message" style="display: none;" data-message="${message}"></div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error" style="display: none;" data-message="${error}"></div>
        </c:if>

        <!-- 表单 -->
        <div class="form-container">
            <form id="ticketForm" method="post" 
                  action="${pageContext.request.contextPath}/tickets<c:if test="${not empty ticket}">/${ticket.id}/update</c:if>">
                
                <div class="form-group">
                    <label for="title">标题 <span style="color: red;">*</span></label>
                    <input type="text" 
                           id="title" 
                           name="title" 
                           value="${ticket.title}" 
                           placeholder="请输入标题（必填，最多200个字符）"
                           maxlength="200"
                           required>
                </div>

                <div class="form-group">
                    <label for="description">描述</label>
                    <textarea id="description" 
                              name="description" 
                              placeholder="请输入描述（可选，最多2000个字符）"
                              maxlength="2000">${ticket.description}</textarea>
                </div>

                <div class="form-group">
                    <label>标签</label>
                    <div class="checkbox-group">
                        <c:forEach items="${allTags}" var="tag">
                            <div class="checkbox-item">
                                <input type="checkbox" 
                                       name="tags" 
                                       value="${tag.name}" 
                                       id="tag_${tag.id}"
                                       <c:if test="${not empty ticket && ticket.tags != null}">
                                           <c:forEach items="${ticket.tags}" var="ticketTag">
                                               <c:if test="${ticketTag.id == tag.id}">checked</c:if>
                                           </c:forEach>
                                       </c:if>>
                                <label for="tag_${tag.id}" style="display: flex; align-items: center; gap: 8px;">
                                    <span class="tag-preview" style="display: inline-block; width: 16px; height: 16px; border-radius: 4px; background-color: ${tag.color != null ? tag.color : '#86868b'}; border: 1px solid var(--apple-border);"></span>
                                    ${tag.name}
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                    <div style="margin-top: 10px;">
                        <input type="text" 
                               id="newTags" 
                               name="newTags" 
                               placeholder="输入新标签（多个标签用逗号分隔）"
                               style="width: 100%; padding: 8px;">
                    <small style="color: var(--apple-text-secondary); font-size: 13px; display: block; margin-top: 8px;">
                        提示：可以输入新标签，多个标签用逗号分隔
                    </small>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${not empty ticket}">更新</c:when>
                            <c:otherwise>创建</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="#" class="btn btn-secondary" onclick="window.dashboard.loadPage('tickets'); return false;">取消</a>
                </div>
            </form>
        </div>
    </div>
    <script>
        // 处理新标签输入（延迟执行，确保 DOM 已加载）
        setTimeout(function() {
            const form = document.getElementById('ticketForm');
            if (form) {
                form.addEventListener('submit', function(e) {
                    const newTagsInput = document.getElementById('newTags');
                    if (newTagsInput) {
                        const newTags = newTagsInput.value.trim();
                        if (newTags) {
                            // 将新标签添加到表单中
                            const tags = newTags.split(',').map(t => t.trim()).filter(t => t);
                            tags.forEach(function(tag) {
                                const input = document.createElement('input');
                                input.type = 'hidden';
                                input.name = 'tags';
                                input.value = tag;
                                this.appendChild(input);
                            }, this);
                        }
                    }
                });
            }
        }, 100);
    </script>
</div>
