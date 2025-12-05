<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 标签表单内容 -->
<div id="tagFormContent">
    <div class="container">
        <!-- 头部 -->
        <div class="header">
            <h1>
                <c:choose>
                    <c:when test="${not empty tag}">编辑标签</c:when>
                    <c:otherwise>新建标签</c:otherwise>
                </c:choose>
            </h1>
            <a href="#" class="btn btn-secondary" onclick="window.dashboard.loadPage('tags'); return false;">返回标签列表</a>
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
            <form id="tagForm" method="post" 
                  action="${pageContext.request.contextPath}/tags<c:if test="${not empty tag}">/${tag.id}/update</c:if>">
                
                <div class="form-group">
                    <label for="name">标签名称 <span style="color: red;">*</span></label>
                    <input type="text" 
                           id="name" 
                           name="name" 
                           value="${tag.name}" 
                           placeholder="请输入标签名称（必填，最多50个字符）"
                           maxlength="50"
                           required>
                    <small style="color: var(--apple-text-secondary); font-size: 13px; display: block; margin-top: 8px;">
                        提示：标签名称必须唯一，不能与现有标签重复
                    </small>
                </div>

                <div class="form-group">
                    <label for="color">标签颜色</label>
                    <div style="display: flex; align-items: center; gap: 12px;">
                        <input type="color" 
                               id="color" 
                               name="color" 
                               value="${tag.color != null ? tag.color : '#86868b'}"
                               style="width: 60px; height: 40px; border: 1px solid var(--apple-border); border-radius: 8px; cursor: pointer;">
                        <input type="text" 
                               id="colorText" 
                               value="${tag.color != null ? tag.color : '#86868b'}"
                               placeholder="#86868b"
                               pattern="^#[0-9A-Fa-f]{6}$"
                               maxlength="7"
                               style="flex: 1; padding: 10px 14px; border: 1px solid var(--apple-border); border-radius: 12px; font-family: 'SF Mono', 'Monaco', 'Courier New', monospace;">
                        <button type="button" 
                                id="colorPresetBtn"
                                class="btn btn-secondary"
                                style="padding: 10px 16px;">
                            预设颜色
                        </button>
                    </div>
                    <small style="color: var(--apple-text-secondary); font-size: 13px; display: block; margin-top: 8px;">
                        提示：选择颜色以区分不同的标签，默认为灰色
                    </small>
                    <div id="colorPresets" style="display: none; margin-top: 12px; display: grid; grid-template-columns: repeat(8, 40px); gap: 8px; padding: 12px; background: var(--apple-bg); border-radius: 12px; border: 1px solid var(--apple-border);">
                        <button type="button" class="color-preset" data-color="#86868b" title="灰色" style="width: 40px; height: 40px; border: 2px solid var(--apple-border); border-radius: 8px; cursor: pointer; background: #86868b; transition: all 0.2s ease;"></button>
                        <button type="button" class="color-preset" data-color="#0071e3" title="蓝色" style="width: 40px; height: 40px; border: 2px solid var(--apple-border); border-radius: 8px; cursor: pointer; background: #0071e3; transition: all 0.2s ease;"></button>
                        <button type="button" class="color-preset" data-color="#ff3b30" title="红色" style="width: 40px; height: 40px; border: 2px solid var(--apple-border); border-radius: 8px; cursor: pointer; background: #ff3b30; transition: all 0.2s ease;"></button>
                        <button type="button" class="color-preset" data-color="#ff9500" title="橙色" style="width: 40px; height: 40px; border: 2px solid var(--apple-border); border-radius: 8px; cursor: pointer; background: #ff9500; transition: all 0.2s ease;"></button>
                        <button type="button" class="color-preset" data-color="#34c759" title="绿色" style="width: 40px; height: 40px; border: 2px solid var(--apple-border); border-radius: 8px; cursor: pointer; background: #34c759; transition: all 0.2s ease;"></button>
                        <button type="button" class="color-preset" data-color="#af52de" title="紫色" style="width: 40px; height: 40px; border: 2px solid var(--apple-border); border-radius: 8px; cursor: pointer; background: #af52de; transition: all 0.2s ease;"></button>
                        <button type="button" class="color-preset" data-color="#ff2d55" title="粉红" style="width: 40px; height: 40px; border: 2px solid var(--apple-border); border-radius: 8px; cursor: pointer; background: #ff2d55; transition: all 0.2s ease;"></button>
                        <button type="button" class="color-preset" data-color="#5856d6" title="靛蓝" style="width: 40px; height: 40px; border: 2px solid var(--apple-border); border-radius: 8px; cursor: pointer; background: #5856d6; transition: all 0.2s ease;"></button>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${not empty tag}">更新</c:when>
                            <c:otherwise>创建</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="#" class="btn btn-secondary" onclick="window.dashboard.loadPage('tags'); return false;">取消</a>
                </div>
            </form>
        </div>
    </div>
    <script>
        // 标签表单验证和颜色选择器（延迟执行，确保 DOM 已加载）
        setTimeout(function() {
            const form = document.getElementById('tagForm');
            const colorInput = document.getElementById('color');
            const colorTextInput = document.getElementById('colorText');
            const colorPresetBtn = document.getElementById('colorPresetBtn');
            const colorPresets = document.getElementById('colorPresets');
            const presetButtons = document.querySelectorAll('.color-preset');

            // 颜色选择器同步
            if (colorInput && colorTextInput) {
                colorInput.addEventListener('input', function() {
                    colorTextInput.value = this.value.toUpperCase();
                });

                colorTextInput.addEventListener('input', function() {
                    const value = this.value.trim();
                    if (/^#[0-9A-Fa-f]{6}$/.test(value)) {
                        colorInput.value = value;
                    }
                });

                colorTextInput.addEventListener('blur', function() {
                    const value = this.value.trim();
                    if (!/^#[0-9A-Fa-f]{6}$/.test(value)) {
                        this.value = colorInput.value.toUpperCase();
                    }
                });
            }

            // 预设颜色按钮
            if (colorPresetBtn && colorPresets) {
                colorPresetBtn.addEventListener('click', function() {
                    const currentDisplay = window.getComputedStyle(colorPresets).display;
                    colorPresets.style.display = currentDisplay === 'none' ? 'grid' : 'none';
                });
            }

            // 预设颜色选择
            presetButtons.forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const color = this.getAttribute('data-color');
                    if (colorInput) colorInput.value = color;
                    if (colorTextInput) colorTextInput.value = color.toUpperCase();
                    if (colorPresets) colorPresets.style.display = 'none';
                });
                
                // 悬停效果
                btn.addEventListener('mouseenter', function() {
                    this.style.transform = 'scale(1.1)';
                    this.style.boxShadow = '0 2px 8px rgba(0, 0, 0, 0.2)';
                });
                
                btn.addEventListener('mouseleave', function() {
                    this.style.transform = 'scale(1)';
                    this.style.boxShadow = 'none';
                });
            });

            // 表单验证
            if (form) {
                form.addEventListener('submit', function(e) {
                    const nameInput = document.getElementById('name');
                    if (nameInput) {
                        const name = nameInput.value.trim();
                        
                        // 清除之前的错误提示
                        const errorMsg = nameInput.parentNode.querySelector('.error-message');
                        if (errorMsg) {
                            errorMsg.remove();
                        }
                        nameInput.style.borderColor = '';
                        
                        // 验证标签名称
                        if (!name) {
                            e.preventDefault();
                            showError('name', '标签名称不能为空');
                            nameInput.focus();
                            return false;
                        }
                        
                        if (name.length > 50) {
                            e.preventDefault();
                            showError('name', '标签名称长度不能超过50个字符');
                            nameInput.focus();
                            return false;
                        }
                    }

                    // 确保颜色值正确
                    if (colorTextInput) {
                        const colorValue = colorTextInput.value.trim();
                        if (colorValue && !/^#[0-9A-Fa-f]{6}$/.test(colorValue)) {
                            e.preventDefault();
                            showError('colorText', '颜色格式不正确，请输入 #RRGGBB 格式');
                            colorTextInput.focus();
                            return false;
                        }
                    }
                    
                    return true;
                });
            }
        }, 100);

        function showError(fieldId, message) {
            const field = document.getElementById(fieldId);
            if (!field) return;
            
            const errorDiv = document.createElement('div');
            errorDiv.className = 'error-message';
            errorDiv.style.color = '#dc3545';
            errorDiv.style.fontSize = '12px';
            errorDiv.style.marginTop = '5px';
            errorDiv.textContent = message;
            
            field.parentNode.appendChild(errorDiv);
            field.style.borderColor = '#dc3545';
        }
    </script>
</div>
