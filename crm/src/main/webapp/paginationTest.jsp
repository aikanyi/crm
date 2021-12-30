<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <%--jquery的依赖--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <%--bootstrap的依赖--%>
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <%--日历插件的依赖--%>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <%--分页插件的依赖--%>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
    <script type="text/javascript">
        $(function() {
            $("#demo_pag1").bs_pagination({
                currentPage: 1,//当前第几页 默认是1
                rowsPerPage: 10,//每页显示几条 默认是10
                totalPages: 100,//全部页数 必填参数
                totalRows:1000,//总条数 默认是1000
                visiblePageLinks: 5,//最多显示的页号数
                showGoToPage: true,//是否显示跳转到第几页，默认是true
                showRowsPerPage: true,//是否显示每页显示条数，默认是true
                showRowsInfo: true,//是否显示记录信息
                //每次切换页号都会触发该函数
                onChangePage:function (event,pageObj) {//pageObj 是一个具有属性currentPage和rowsPerPage的对象。
                    alert(pageObj.currentPage);
                    alert(pageObj.rowsPerPage);
                }
            });
        });
    </script>
    <title>演示日历</title>
</head>
<body>

<div id="demo_pag1"></div>
</body>
</html>
