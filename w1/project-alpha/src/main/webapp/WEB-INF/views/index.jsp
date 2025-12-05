<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="refresh" content="0;url=${pageContext.request.contextPath}/tickets">
    <title>Ticket 管理系统 - 首页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <style>
        .welcome-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 70vh;
            text-align: center;
            padding: 60px 20px;
        }
        .welcome-title {
            font-size: 56px;
            font-weight: 600;
            letter-spacing: -0.003em;
            color: var(--apple-text);
            margin-bottom: 16px;
            background: linear-gradient(135deg, #1d1d1f 0%, #424245 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        .welcome-description {
            font-size: 21px;
            color: var(--apple-text-secondary);
            margin-bottom: 40px;
            font-weight: 400;
            letter-spacing: 0.011em;
        }
        .redirect-message {
            color: var(--apple-text-secondary);
            font-size: 17px;
            margin-top: 24px;
        }
        .welcome-actions {
            display: flex;
            gap: 16px;
            margin-top: 32px;
            flex-wrap: wrap;
            justify-content: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="welcome-container">
            <h1 class="welcome-title">Ticket 标签管理系统</h1>
            <p class="welcome-description">简洁、优雅的待办事项管理工具</p>
            <div class="welcome-actions">
                <a href="${pageContext.request.contextPath}/tickets" class="btn btn-primary">Ticket 列表</a>
                <a href="${pageContext.request.contextPath}/tags" class="btn btn-secondary">标签管理</a>
            </div>
            <p class="redirect-message">正在自动跳转到 Ticket 列表...</p>
        </div>
    </div>
</body>
</html>

