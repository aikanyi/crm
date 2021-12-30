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
	$(function(){
		<%--当市场活动主页面加载完成之后,显示所有数据的第一页--%>
		queryActivityForPage(1,10);
		<%--日历时间显示--%>
		$(".myDate").datetimepicker({
			format:'yyyy-mm-dd',
			autoclose:'true',
			minView:'month',
			todayBtn:'true',
			language:'zh-CN',
			initialDate: new Date(),
			clearBtn:'true'
		});
		<%--查询按钮绑定单击事件--%>
		$("#queryBtn").on("click",function () {
			//点击"查询"按钮,显示所有符合条件的数据的第一页，获取最新页面条数，保持每页显示条数不变
			queryActivityForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		});
		<%--给创建市场活动信息按钮绑定单击事件--%>
		$("#createActivityBtn").on("click",function () {
			//重置创建市场活动表单数据
			$("#createActivityForm").get(0).reset();
			//弹出创建市场活动信息的模态窗口
			$("#createActivityModal").modal("show");
		});
		<%--给保存市场活动信息按钮绑定单击事件--%>
		$("#saveCreateActivityBtn").on("click",function () {
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());
			if(owner == ""){
				alert("所有者不能为空");
				return;
			}
			if(name == ""){
				alert("名称不能为空");
				return;
			}
			if (startDate != "" && endDate != "") {
				if (endDate < startDate) {
					alert("结束时间不能比开始时间小");
					return;
				}
			}
			var regexp = /^\d+$/;
			if (!regexp.test(cost)) {
				alert("金额需要为正整数");
				return;
			}
			//发送保存创建市场活动数据的请求
			$.ajax({
				url:'workbench/activity/saveCreateActivity.do',
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if (data.code == "1") {
						//创建成功之后,关闭模态窗口
						$("#createActivityModal").modal("hide");
						//刷新市场活动列，显示第一页数据，保持每页显示条数不变
						queryActivityForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						//创建失败,提示信息创建失败,模态窗口不关闭,市场活动列表也不刷新
						alert(data.message);
						$("#createActivityModal").modal("show");
					}
				}
			});
		});
		<%--给修改按钮绑定单击事件--%>
		$("#updateActivityBtn").on("click",function () {
			//判断是否选中且只选中一条,获取选中的信息ID
			var checkedIds = $("#tbody input[type='checkbox']:checked");
			//表单验证
			if (checkedIds.size() == 0) {
				alert("请选择要修改的记录");
				return;
			}else if(checkedIds.size() > 1){
				alert("不能修改多条市场活动");
				return;
			}
			var id = checkedIds.val();
			//获取参数ID，发送请求，根据id查询活动信息
			$.ajax({
				url:'workbench/activity/queryActivityById.do',
				data:{
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					//成功后更新修改的模态窗口数据
					//给隐藏域赋值
					$("#editId").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-endDate").val(data.endDate);
					$("#edit-startDate").val(data.startDate);
					$("#edit-cost").val(data.cost);
					$("#edit-description").val(data.description);
					//获取数据后,弹出修改市场活动的模态窗口
					$("#editActivityModal").modal("show");

				}
			});
		});
		<%--给删除市场活动按钮绑定单击事件--%>
		$("#deleteActivityBtn").on("click",function () {
			//获取选中的信息ID
			var checkedIds = $("#tbody input[type='checkbox']:checked");
			//表单验证
			if (checkedIds.size() == 0) {
				alert("请选择要删除的记录");
				return;
			}
			//遍历checkedIds 获取每一个属性的value 拼接成id=xxx&id=xxx&id=xxx&id=xxx
			var ids = "";
			$.each(checkedIds,function () {
				ids+="id="+this.value+"&";
			});
			ids = ids.substr(0,ids.length-1);
			if (window.confirm("确认删除吗")){
				$.ajax({
					url:'workbench/activity/deleteActivityByIdForPage.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function (data) {
						//删除成功
						if (data.code == 1) {
							//刷新市场活动列表,显示第一页数据,保持每页显示条数不变
							queryActivityForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
						} else {
							//提示信息
							alert(data.message);
						}
					}
				});
			}
		});
		<%--给更新按钮绑定单击事件--%>
		$("#editActivityBtn").on("click",function () {
			<%--收集参数--%>
			var id = $("#editId").val();
			var name =$.trim($("#edit-marketActivityName").val());
			var owner =$("#edit-marketActivityOwner").val();
			var endDate =$("#edit-endDate").val();
			var startDate =$("#edit-startDate").val();
			var cost =$.trim($("#edit-cost").val());
			var description =$.trim($("#edit-description").val());
			<%--表单验证--%>
			if (name == "") {
				alert("名称不能为空");
				return;
			}
			if (owner == "") {
				alert("所有者不能为空");
				return;
			}
			if (startDate != "" && endDate != "") {
				if (startDate > endDate) {
					alert("结束日期不能比开始日期小");
					return;
				}
			}
			var regexp = /^\d+$/;
			if (!regexp.test(cost)) {
				alert("金额需要为正整数");
				return;
			}
			if (description == "") {
				alert("备注内容不能为空");
				return;
			}
			<%--发送请求--%>
			$.ajax({
				url:'workbench/activity/updateActivityById.do',
				data:{
					id:id,
					cost:cost,
					name:name,
					owner:owner,
					endDate:endDate,
					startDate:startDate,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if (data.code > 0) {
						<%--修改成功之后,关闭模态窗口,刷新列表--%>
						$("#editActivityModal").modal("hide");
						queryActivityForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'), $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						alert(data.message);
					}

				}
			});
		})
		<%--给第一个复选框绑定单击事件--%>
		$("#firstCheckboxId").on("click",function () {
			$("#tbody input[type='checkbox']").prop("checked",this.checked);
		});
		<%--给活动列表的复选框绑定单击事件--%>
		$("#tbody").on("click","input[type='checkbox']",function () {
			<%--在这里控制第一个复选框的状态--%>
				$("#firstCheckboxId").prop("checked", $("#tbody input[type='checkbox']").size() == $("#tbody input[type='checkbox']:checked").size());
		});
		<%--给导入市场活动信息按钮绑定单击时间--%>
		$("#importActivityBtn").on("click",function () {
			var fileName = $("#activityFile").val();
			var suffix = fileName.substr(fileName.length - 3).toLocaleLowerCase();
			<%--表单验证--%>
			if (suffix != 'xls') {
				alert("只支持'xls'文件");
				return;
			}
			var activityFile = $("#activityFile")[0].files[0];
			if (activityFile.size > 5 * 1024 * 1024) {
				alert("文件大小不能超过5M");
				return;
			}
			var formData = new FormData();
			formData.append("activityFile",activityFile);
			$.ajax({
				url:'workbench/activity/fileUpload.do',
				data:formData,
				processData:false,<%--默认情况下ajax会把所有参数转成字符串--%>
				contentType:false,<%--默认情况下ajax会按照urlencoded编码--%>
				type:'post',
				dataType:'json',
				success:function (data) {
					if (data.code == "1") {
						alert("成功导入" + data.retData + "条数据");
						<%--关闭模态窗口,刷新市场活动列表,显示第一页数据,保持每页显示条数不变--%>
						$("#importActivityModal").modal("hide");
						queryActivityForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						<%--导入失败,提示信息,模态窗口不关闭,列表也不刷新--%>
						alert("系统忙，请稍后重试...");
						$("#importActivityModal").modal("show");
					}
				}
			});
		});
		<%--给批量导出市场活动信息按钮绑定单击事件--%>
		$("#exportActivityAllBtn").click(function () {
			window.location.href="workbench/activity/queryAllActivitys.do";
		});
		<%--给选择导出市场活动信息按钮绑定单击事件--%>
		$("#exportActivityXzBtn").on("click",function () {
			//获取所有选中的id
			var checkedIds = $("#tbody input[type='checkbox']:checked");
			//表单验证
			if (checkedIds.size() == 0) {
				alert("请选择要导出的记录");
				return;
			}
			//遍历checkedIds 获取每一个属性的value 拼接成id=xxx&id=xxx&id=xxx&id=xxx
			var ids = "";
			$.each(checkedIds,function () {
				ids+="id="+this.value+"&";
			});
			ids = ids.substr(0,ids.length-1);
			window.location.href="workbench/activity/queryActivityByIds.do?"+ids;
		})
	});
	<%--查询市场活动显示到页面--%><%----%>
	function queryActivityForPage(pageNo,pageSize) {
		var name = $("#queryName").val();
		var owner = $("#queryOwner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();
		/*var pageNo = 1;
		var pageSize = 10;*/
		$.ajax({
			url: 'workbench/activity/queryActivityByConditionForPage.do',
			data: {
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType: 'json',
			success:function (data) {
				//显示市场活动总条数
				//$("#totalRows").text(data.totalRows);
				var htmlStr = "";
				$.each(data.activityList,function (index,obj) {
					htmlStr+="<tr class='active'>";
					htmlStr+="<td><input type='checkbox'  value='"+obj.id+"'/></td>";
					htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/activityDetail.do?id="+obj.id+"'\">"+obj.name+"</a></td>";
					htmlStr+="<td>"+obj.owner+"</td>";
					htmlStr+="<td>"+obj.startDate+"</td>";
					htmlStr+="<td>"+obj.endDate+"</td>";
					htmlStr+="</tr>";
				});
				$("#tbody").html(htmlStr);
				//计算总页数
				var totalPages = 1;
				if (data.totalRows % pageSize == 0) {
					 totalPages = data.totalRows / pageSize;
				} else {
					 totalPages = parseInt(data.totalRows / pageSize) +1;
				}
				//分页功能显示
				$("#demo_pag1").bs_pagination({
					currentPage: pageNo,//当前第几页 默认是1
					rowsPerPage: pageSize,//每页显示几条 默认是10
					totalPages: totalPages,//全部页数 必填参数 totalRows/rowsPerPage
					totalRows:data.totalRows,//总条数 默认是1000
					visiblePageLinks: 2,//最多显示的页号数
					showGoToPage: true,//是否显示跳转到第几页，默认是true
					showRowsPerPage: true,//是否显示每页显示条数，默认是true
					showRowsInfo: true,//是否显示记录信息
					//每次切换页号都会触发该函数
					onChangePage:function (event,pageObj) {//pageObj 是一个具有属性currentPage和rowsPerPage的对象。
						queryActivityForPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});
			}
		})
	}
</script>
</head>
<body>

	<%--创建市场活动的模态窗口--%>
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
					<%--市场活动表单--%>
					<form class="form-horizontal" id="createActivityForm" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control marketActivity" id="create-marketActivityOwner" >
									<option></option>>
									<c:forEach items="${requestScope.users}" var="u">
										<%--当当前对象的ID 等于 当前市场活动的所有者--%>
										<c:choose>
											<c:when test="${u.id==user.id}">
												<option value="${u.id}" selected>${u.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${u.id}">${u.name}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName"  class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10"  style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="create-startDate"  class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10"  style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10"   style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost"  class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description"  class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="saveCreateActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<%--修改市场活动的模态窗口--%>
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
					<%--修改市场活动表单--%>
					<form class="form-horizontal" role="form">
						<%--用来保存id的隐藏域--%>
						<input type="hidden" id="editId" />
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control marketActivity" id="edit-marketActivityOwner">
									<c:forEach items="${requestScope.users}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control myDate" id="edit-startDate" value="2020-10-10">
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-endDate" value="2020-10-20">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="editActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<%--导入市场活动的模态窗口--%>
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>


	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<%--市场活动列表上层--%>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<%--市场活动列表上层查询表单--%>
				<form class="form-inline"  role="form" style="position: relative;top: 8%; left: 5px;">
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="queryName">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="queryOwner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">开始日期</div>
					  <input class="form-control myDate" type="text" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">结束日期</div>
					  <input class="form-control myDate" type="text" id="query-endDate">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="queryBtn">查询</button>

				</form>
			</div>


			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn" ><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="updateActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" id="uploadBtn" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="firstCheckboxId"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tbody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
				<%--显示查询的市场活动信息的div--%>
				<div id="demo_pag1"></div>
			</div>

			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRows">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>

		</div>

	</div>
</body>
</html>