/**
 * Ticket 标签管理系统 - 主 JavaScript 文件
 */

// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 表单验证
    const ticketForm = document.getElementById('ticketForm');
    if (ticketForm) {
        ticketForm.addEventListener('submit', function(e) {
            if (!validateTicketForm()) {
                e.preventDefault();
                return false;
            }
        });
    }

    // 删除确认 - 处理表单提交的删除按钮
    const deleteForms = document.querySelectorAll('form[action*="/delete"]');
    deleteForms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            if (!confirm('确认要删除该 Ticket 吗？此操作不可恢复。')) {
                e.preventDefault();
                return false;
            }
        });
    });

    // 自动隐藏消息提示（Apple 风格动画）
    const messages = document.querySelectorAll('.message');
    messages.forEach(function(msg) {
        setTimeout(function() {
            msg.style.transition = 'all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94)';
            msg.style.opacity = '0';
            msg.style.transform = 'translateY(-10px)';
            setTimeout(function() {
                msg.remove();
            }, 400);
        }, 4000);
    });

    // 添加表格行悬停效果
    const tableRows = document.querySelectorAll('table tbody tr');
    tableRows.forEach(function(row) {
        row.addEventListener('mouseenter', function() {
            this.style.transition = 'background-color 0.2s ease';
        });
    });

    // 添加按钮点击波纹效果
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(function(btn) {
        btn.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;
            
            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.style.position = 'absolute';
            ripple.style.borderRadius = '50%';
            ripple.style.background = 'rgba(255, 255, 255, 0.3)';
            ripple.style.transform = 'scale(0)';
            ripple.style.animation = 'ripple 0.6s ease-out';
            ripple.style.pointerEvents = 'none';
            
            this.style.position = 'relative';
            this.style.overflow = 'hidden';
            this.appendChild(ripple);
            
            setTimeout(function() {
                ripple.remove();
            }, 600);
        });
    });
});

/**
 * 验证 Ticket 表单
 */
function validateTicketForm() {
    const titleInput = document.getElementById('title');
    const title = titleInput ? titleInput.value.trim() : '';
    
    // 清除之前的错误提示
    clearError('title');
    
    // 验证标题
    if (!title) {
        showError('title', '标题不能为空');
        if (titleInput) {
            titleInput.focus();
        }
        return false;
    }
    
    if (title.length > 200) {
        showError('title', '标题长度不能超过200个字符');
        if (titleInput) {
            titleInput.focus();
        }
        return false;
    }
    
    // 验证描述
    const descriptionInput = document.getElementById('description');
    if (descriptionInput) {
        const description = descriptionInput.value.trim();
        if (description.length > 2000) {
            showError('description', '描述长度不能超过2000个字符');
            descriptionInput.focus();
            return false;
        }
    }
    
    return true;
}

/**
 * 显示错误信息
 */
function showError(fieldId, message) {
    const field = document.getElementById(fieldId);
    if (!field) return;
    
    // 移除之前的错误提示
    clearError(fieldId);
    
    // 创建错误提示元素
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.style.color = '#dc3545';
    errorDiv.style.fontSize = '12px';
    errorDiv.style.marginTop = '5px';
    errorDiv.textContent = message;
    
    // 插入错误提示
    field.parentNode.appendChild(errorDiv);
    
    // 添加错误样式
    field.style.borderColor = '#dc3545';
}

/**
 * 清除错误信息
 */
function clearError(fieldId) {
    const field = document.getElementById(fieldId);
    if (!field) return;
    
    // 移除错误提示元素
    const errorMessage = field.parentNode.querySelector('.error-message');
    if (errorMessage) {
        errorMessage.remove();
    }
    
    // 恢复边框颜色
    field.style.borderColor = '';
}

/**
 * 格式化日期时间
 */
function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '';
    
    const date = new Date(dateTimeStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

