<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket 标签管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <style>
        .dashboard-container {
            display: flex;
            min-height: 100vh;
            background: var(--apple-bg);
        }

        /* 左侧导航栏 */
        .sidebar {
            width: 260px;
            background: var(--apple-white);
            box-shadow: 2px 0 8px var(--apple-shadow);
            padding: 32px 0;
            position: fixed;
            height: 100vh;
            overflow-y: auto;
            z-index: 100;
        }

        .sidebar-header {
            padding: 0 24px 32px;
            border-bottom: 1px solid var(--apple-border);
            margin-bottom: 24px;
        }

        .sidebar-header h1 {
            font-size: 24px;
            font-weight: 600;
            letter-spacing: -0.003em;
            background: linear-gradient(135deg, #1d1d1f 0%, #424245 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .nav-menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .nav-item {
            margin: 0;
        }

        .nav-link {
            display: flex;
            align-items: center;
            padding: 14px 24px;
            color: var(--apple-text);
            text-decoration: none;
            font-size: 17px;
            font-weight: 400;
            transition: all 0.2s ease;
            border-left: 3px solid transparent;
            cursor: pointer;
        }

        .nav-link:hover {
            background: var(--apple-tag-bg);
            color: var(--apple-blue);
        }

        .nav-link.active {
            background: rgba(0, 113, 227, 0.1);
            color: var(--apple-blue);
            border-left-color: var(--apple-blue);
            font-weight: 500;
        }

        .nav-link-icon {
            margin-right: 12px;
            font-size: 20px;
        }

        /* 右侧内容区 */
        .main-content {
            flex: 1;
            margin-left: 260px;
            padding: 0;
            min-height: 100vh;
        }

        .content-wrapper {
            padding: 40px;
        }

        /* 加载状态 */
        .content-loading {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 400px;
            flex-direction: column;
        }

        .loading-spinner {
            width: 40px;
            height: 40px;
            border: 3px solid var(--apple-border);
            border-top-color: var(--apple-blue);
            border-radius: 50%;
            animation: spin 0.8s linear infinite;
            margin-bottom: 16px;
        }

        .loading-text {
            color: var(--apple-text-secondary);
            font-size: 17px;
        }

        /* 内容区域样式 */
        .content-wrapper .header {
            margin-bottom: 32px;
        }

        .content-wrapper .container {
            max-width: 100%;
            padding: 0;
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
                box-shadow: 0 2px 8px var(--apple-shadow);
            }

            .main-content {
                margin-left: 0;
            }

            .content-wrapper {
                padding: 20px 16px;
            }

            .nav-link {
                padding: 12px 20px;
            }
        }

        /* 消息提示容器 */
        .message-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1000;
            max-width: 400px;
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <!-- 左侧导航栏 -->
        <nav class="sidebar">
            <div class="sidebar-header">
                <h1>Ticket 管理系统</h1>
            </div>
            <ul class="nav-menu">
                <li class="nav-item">
                    <a href="#" class="nav-link active" data-page="tickets">
                        <span class="nav-link-icon">📋</span>
                        <span>工单管理</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link" data-page="tags">
                        <span class="nav-link-icon">🏷️</span>
                        <span>标签管理</span>
                    </a>
                </li>
            </ul>
        </nav>

        <!-- 右侧内容区 -->
        <main class="main-content">
            <div class="content-wrapper" id="contentArea">
                <div class="content-loading">
                    <div class="loading-spinner"></div>
                    <div class="loading-text">加载中...</div>
                </div>
            </div>
        </main>
    </div>

    <!-- 消息提示容器 -->
    <div class="message-container" id="messageContainer"></div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>

