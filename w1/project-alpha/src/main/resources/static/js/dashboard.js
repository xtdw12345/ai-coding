/**
 * Dashboard 主页面 JavaScript
 * 实现单页面应用的导航和内容切换
 */

(function() {
    'use strict';

    const contentArea = document.getElementById('contentArea');
    const navLinks = document.querySelectorAll('.nav-link');
    const messageContainer = document.getElementById('messageContainer');
    let currentPage = 'tickets';

    // 初始化
    document.addEventListener('DOMContentLoaded', function() {
        // 加载默认页面（工单管理）
        loadPage('tickets');
        
        // 绑定导航链接点击事件
        navLinks.forEach(function(link) {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                const page = this.getAttribute('data-page');
                if (page && page !== currentPage) {
                    loadPage(page);
                }
            });
        });

        // 监听浏览器前进后退
        window.addEventListener('popstate', function(e) {
            const page = e.state ? e.state.page : 'tickets';
            loadPage(page, false);
        });
    });

    /**
     * 加载页面内容
     */
    function loadPage(page, pushState = true) {
        currentPage = page;
        
        // 更新导航状态
        updateNavState(page);
        
        // 更新浏览器历史
        if (pushState) {
            window.history.pushState({ page: page }, '', '/');
        }

        // 显示加载状态
        showLoading();

        // 根据页面类型加载内容
        const url = page === 'tickets' 
            ? '/tickets/content' 
            : '/tags/content';

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('加载失败');
                }
                return response.text();
            })
            .then(html => {
                contentArea.innerHTML = html;
                
                // 重新初始化页面内的脚本
                initPageScripts(page);
                
                // 处理页面内的消息提示
                handlePageMessages();
            })
            .catch(error => {
                console.error('加载页面失败:', error);
                showError('加载页面失败，请刷新重试');
            });
    }

    /**
     * 更新导航状态
     */
    function updateNavState(activePage) {
        navLinks.forEach(function(link) {
            const page = link.getAttribute('data-page');
            if (page === activePage) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });
    }

    /**
     * 显示加载状态
     */
    function showLoading() {
        contentArea.innerHTML = `
            <div class="content-loading">
                <div class="loading-spinner"></div>
                <div class="loading-text">加载中...</div>
            </div>
        `;
    }

    /**
     * 初始化页面内的脚本
     */
    function initPageScripts(page) {
        // 重新绑定表单提交事件
        const forms = contentArea.querySelectorAll('form');
        forms.forEach(function(form) {
            // 如果是删除表单，添加确认
            if (form.action.includes('/delete')) {
                form.addEventListener('submit', function(e) {
                    if (!confirm('确认要删除吗？此操作不可恢复。')) {
                        e.preventDefault();
                        return false;
                    }
                    // 删除表单使用 AJAX 提交
                    e.preventDefault();
                    submitForm(form);
                });
            }
            // 如果是完成/重新打开表单，使用 AJAX
            else if (form.action.includes('/complete') || form.action.includes('/reopen')) {
                form.addEventListener('submit', function(e) {
                    e.preventDefault();
                    submitForm(form);
                });
            }
            // 如果是搜索表单，使用 AJAX
            else if (form.method === 'get' || form.method === 'GET') {
                form.addEventListener('submit', function(e) {
                    e.preventDefault();
                    const formData = new FormData(form);
                    const params = new URLSearchParams(formData);
                    const url = form.action + '?' + params.toString() + '&content=1';
                    loadPageContent(url);
                });
            }
            // 其他表单（新建/编辑），使用 AJAX
            else {
                form.addEventListener('submit', function(e) {
                    e.preventDefault();
                    submitForm(form);
                });
            }
        });

        // 重新绑定链接点击事件（处理页面内的链接）
        const links = contentArea.querySelectorAll('a[href^="/"]');
        links.forEach(function(link) {
            link.addEventListener('click', function(e) {
                const href = this.getAttribute('href');
                
                // 如果是新建/编辑页面，使用 AJAX 加载
                if (href.includes('/new') || href.includes('/edit')) {
                    e.preventDefault();
                    loadFormPage(href + (href.includes('?') ? '&' : '?') + 'content=1');
                }
                // 其他链接正常跳转
            });
        });

        // 重新绑定分页链接
        const paginationLinks = contentArea.querySelectorAll('.pagination a');
        paginationLinks.forEach(function(link) {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                let url = this.getAttribute('href');
                // 确保 URL 包含 content 参数
                if (url && !url.includes('content=')) {
                    url += (url.includes('?') ? '&' : '?') + 'content=1';
                }
                loadPageContent(url);
            });
        });
        
        // 重新绑定操作按钮（完成/重新打开）
        const actionButtons = contentArea.querySelectorAll('form[action*="/complete"], form[action*="/reopen"]');
        actionButtons.forEach(function(form) {
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                submitForm(form);
            });
        });
    }

    /**
     * 提交表单（AJAX）
     */
    function submitForm(form) {
        const formData = new FormData(form);
        const url = form.action;
        const method = form.method || 'POST';

        showLoading();

        fetch(url, {
            method: method,
            body: formData,
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            },
            redirect: 'follow'
        })
        .then(response => {
            // 检查是否是重定向响应
            if (response.redirected || response.url.includes('/content')) {
                // 如果是重定向到 content 端点，直接加载
                const redirectUrl = response.url;
                if (redirectUrl.includes('/content')) {
                    return fetch(redirectUrl, {
                        headers: {
                            'X-Requested-With': 'XMLHttpRequest'
                        }
                    }).then(r => r.text());
                } else {
                    // 其他重定向，重新加载当前页面
                    loadPage(currentPage);
                    return Promise.resolve('');
                }
            } else {
                return response.text();
            }
        })
        .then(html => {
            if (html) {
                // 如果返回的是 HTML，更新内容
                contentArea.innerHTML = html;
                initPageScripts(currentPage);
                handlePageMessages();
            }
        })
        .catch(error => {
            console.error('提交表单失败:', error);
            showError('提交失败，请重试');
            // 重新加载当前页面
            loadPage(currentPage);
        });
    }

    /**
     * 加载表单页面
     */
    function loadFormPage(url) {
        showLoading();
        fetch(url, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 404) {
                        throw new Error('页面未找到');
                    }
                    throw new Error('加载失败: ' + response.status);
                }
                return response.text();
            })
            .then(html => {
                if (html && html.trim().length > 0) {
                    contentArea.innerHTML = html;
                    initPageScripts(currentPage);
                    handlePageMessages();
                } else {
                    showError('页面内容为空');
                    loadPage(currentPage);
                }
            })
            .catch(error => {
                console.error('加载表单失败:', error);
                showError('加载表单失败: ' + error.message);
                // 重新加载当前页面
                loadPage(currentPage);
            });
    }

    /**
     * 加载页面内容（用于分页等）
     */
    function loadPageContent(url) {
        showLoading();
        fetch(url + (url.includes('?') ? '&' : '?') + 'content=1')
            .then(response => response.text())
            .then(html => {
                contentArea.innerHTML = html;
                initPageScripts(currentPage);
                handlePageMessages();
            })
            .catch(error => {
                console.error('加载内容失败:', error);
                showError('加载内容失败');
            });
    }

    /**
     * 处理页面内的消息提示
     */
    function handlePageMessages() {
        const messages = contentArea.querySelectorAll('.message[data-message]');
        messages.forEach(function(msg) {
            // 从 data-message 属性获取消息
            const messageText = msg.getAttribute('data-message');
            const isError = msg.classList.contains('error');
            msg.remove();
            
            if (messageText) {
                showMessage(messageText, isError ? 'error' : 'success');
            }
        });
        
        // 也处理普通的消息元素
        const normalMessages = contentArea.querySelectorAll('.message:not([data-message])');
        normalMessages.forEach(function(msg) {
            const messageText = msg.textContent.trim();
            const isError = msg.classList.contains('error');
            msg.remove();
            
            if (messageText) {
                showMessage(messageText, isError ? 'error' : 'success');
            }
        });
    }

    /**
     * 显示消息提示
     */
    function showMessage(message, type = 'success') {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${type === 'error' ? 'error' : ''}`;
        messageDiv.textContent = message;
        messageDiv.style.animation = 'slideIn 0.3s ease-out';
        
        messageContainer.appendChild(messageDiv);

        // 自动隐藏
        setTimeout(function() {
            messageDiv.style.transition = 'all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94)';
            messageDiv.style.opacity = '0';
            messageDiv.style.transform = 'translateY(-10px)';
            setTimeout(function() {
                messageDiv.remove();
            }, 400);
        }, 4000);
    }

    /**
     * 显示错误消息
     */
    function showError(message) {
        showMessage(message, 'error');
    }

    // 导出到全局，供其他脚本使用
    window.dashboard = {
        loadPage: loadPage,
        showMessage: showMessage,
        showError: showError
    };
})();

