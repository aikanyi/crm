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
		<%--当线索主页面加载完成之后,显示所有数据的第一页,默认显示10条记录--%>
		queryClueForPage(1, 10);
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
		<%--给创建按钮绑定单击事件--%>
		$("#createClueBtn").on("click",function () {
			$("#createClueForm")[0].reset();
			$("#createClueModal").modal("show");
		});
		<%--给保存按钮绑定单击事件--%>
		$("#saveCreateClueBtn").on("click",function () {
			<%--获取参数--%>
			var owner = $.trim($("#create-clueOwner").val());
			var company = $.trim($("#create-company").val());
			var fullname = $.trim($("#create-fullname").val());
			var appellation = $.trim($("#create-appellation").val());
			var job = $.trim($("#create-job").val());
			var email = $.trim($("#create-email").val());
			var phone = $.trim($("#create-company-phone").val());
			var website = $.trim($("#create-company-website").val());
			var mphone = $.trim($("#create-clue-mphone").val());
			var state = $.trim($("#create-clue-state").val());
			var source = $.trim($("#create-clue-source").val());
			var description = $.trim($("#create-clue-description").val());
			var contact_summary = $.trim($("#create-contact-summary").val());
			var next_contact_time = $.trim($("#create-next-contact-time").val());
			var address = $.trim($("#create-address").val());
			<%--表单验证--%>
			if (owner == "") {
				alert("所有者不能为空");
				return;
			}
			if (company == "") {
				alert("公司不能为空");
				return;
			}
			if (fullname == "") {
				alert("姓名不能为空");
				return;
			}
			var emailRegexp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			if (!emailRegexp.test(email)) {
				alert("请输入合法的邮箱");
				return;
			}
			var phoneRegexp = /\d{3}-\d{8}|\d{4}-\d{7}/;
			if (!phoneRegexp.test(phone)) {
				alert("请输入合法的座机");
				return;
			}
			var websiteRegexp = /[a-zA-z]+:\/\/[^\s]*/;
			if (!websiteRegexp.test(website)) {
				alert("请输入合法的网站");
				return;
			}
			var mphoneRegexp = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
			if (!mphoneRegexp.test(mphone)) {
				alert("请输入合法的手机号码");
				return;
			}
			<%--发送请求--%>
			$.ajax({
				url:'workbench/clue/saveCreateClue.do',
				data:{
					owner:owner,
					company:company,
					fullname:fullname,
					appellation:appellation,
					job:job,
					email:email,
					phone:phone,
					website:website,
					state:state,
					source:source,
					mphone:mphone,
					description:description,
					contactSummary:contact_summary,
					nextContactTime:next_contact_time,
					address:address
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if (data.code == "1") {
						<%--创建成功之后，关闭模态窗口，刷新线索列表，显示第一页数据，保持每页显示条数不变--%>
						$("#createClueModal").modal("hide");
						queryClueForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						<%--创建失败，提示信息，模态窗口不关闭，列表也不刷新。--%>
						alert(data.message);
						$("#createClueModal").modal("show");
					}
				}
			});
		});
		<%--给查询按钮绑定单击事件--%>
		$("#queryBtn").on("click",function () {
			<%--显示所有符合条件的数据的第一页--%>
			queryClueForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		});
		<%--给删除按钮绑定单击事件--%>
		$("#deleteClueBtn").on("click",function () {
			<%--收集参数--%>
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
					url:'workbench/clue/deleteClueById.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function (data) {
						//删除成功
						if (data.code == 1) {
							//刷新线索列表,显示第一页数据,保持每页显示条数不变
							queryClueForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
						} else {
							//提示信息
							alert(data.message);
						}
					}
				});
			}
		});
		<%--给修改按钮绑定单击事件--%>
		$("#editClueBtn").on("click",function () {
			var ids =$("#tbody input[type='checkbox']:checked");
			<%--验证选中 且只能选中一条--%>
			if (ids.size() == 0) {
				alert("请选择一条信息");
				return;
			}
			if (ids.size() > 1) {
				alert("最多只能选中一条信息");
				return;
			}
			var id = ids.val();
			<%--发送请求 查询信息 显示数据--%>
			$.ajax({
				url:'workbench/clue/queryClueById.do',
				data:{
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					<%--更新模态窗口信息--%>
					$("#editId").val(data.id);
					$("#edit-clueOwner").val(data.owner);
					$("#edit-company").val(data.company);
					$("#edit-appellation").val(data.appellation);
					$("#edit-fullname").val(data.fullname);
					$("#edit-job").val(data.job);
					$("#edit-email").val(data.email);
					$("#edit-phone").val(data.phone);
					$("#edit-website").val(data.website);
					$("#edit-mphone").val(data.mphone);
					$("#edit-state").val(data.state);
					$("#edit-source").val(data.source);
					$("#edit-description").val(data.description);
					$("#edit-contactSummary").val(data.contactSummary);
					$("#edit-nextContactTime").val(data.nextContactTime);
					$("#edit-address").val(data.address);
					$("#editClueModal").modal("show");
				}
			});
		});
		<%--给更新按钮绑定单击事件--%>
		$("#updateClueBtn").on("click",function () {
			<%--获取参数--%>
			var id = $("#editId").val();
			var owner = $.trim($("#edit-clueOwner").val());
			var company = $.trim($("#edit-company").val());
			var fullname = $.trim($("#edit-fullname").val());
			var appellation = $.trim($("#edit-appellation").val());
			var job = $.trim($("#edit-job").val());
			var email = $.trim($("#edit-email").val());
			var phone = $.trim($("#edit-phone").val());
			var website = $.trim($("#edit-website").val());
			var mphone = $.trim($("#edit-mphone").val());
			var state = $.trim($("#edit-state").val());
			var source = $.trim($("#edit-source").val());
			var description = $.trim($("#edit-description").val());
			var contact_summary = $.trim($("#edit-contactSummary").val());
			var next_contact_time = $.trim($("#edit-nextContactTime").val());
			var address = $.trim($("#edit-address").val());
			<%--表单验证--%>
			if (owner == "") {
				alert("所有者不能为空");
				return;
			}
			if (company == "") {
				alert("公司不能为空");
				return;
			}
			if (fullname == "") {
				alert("姓名不能为空");
				return;
			}
			<%--发送请求--%>
			$.ajax({
				url:'workbench/clue/updateSaveClueMessage.do',
				data:{
					id:id,
					owner:owner,
					company:company,
					fullname:fullname,
					appellation:appellation,
					job:job,
					email:email,
					phone:phone,
					website:website,
					state:state,
					source:source,
					mphone:mphone,
					description:description,
					contactSummary:contact_summary,
					nextContactTime:next_contact_time,
					address:address
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if (data.code == "1") {
						<%--更新成功之后，关闭模态窗口，刷新线索列表，显示第一页数据，保持每页显示条数不变--%>
						$("#editClueModal").modal("hide");
						queryClueForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						<%--更新失败，提示信息，模态窗口不关闭，列表也不刷新。--%>
						alert(data.message);
						$("#editClueModal").modal("show");
					}
				}
			});
		});
		<%--给全选框绑定单击事件--%>
		$("#fristCheckboxBtn").on("click",function () {
			<%--将所有复选框的状态和第一个复选框绑定在一起--%>
			$("#tbody input[type='checkbox']").prop("checked",this.checked);
		});
		<%--给所有的复选框添加单击事件--%>
		$("#tbody").on("click","input[type='checkbox']",function () {
			$("#fristCheckboxBtn").prop("checked",($("#tbody input[type='checkbox']").size() == $("#tbody input[type='checkbox']:checked").size()));
		})
	});
	function queryClueForPage(pageNo,pageSize) {
		<%--获取参数--%>
		var fullname = $.trim($("#fullnameBtn").val());
		var company = $.trim($("#companyBtn").val());
		var phone = $.trim($("#phoneBtn").val());
		var source = $("#selectClueSource").val();
		var owner = $.trim($("#ownerBtn").val());
		var mphone = $.trim($("#mphoneBtn").val());
		var state = $("#selectClueState").val();
		<%--发送请求--%>
		$.ajax({
			url:'workbench/clue/queryClueByConditionForPage.do',
			data:{
				fullname:fullname,
				company:company,
				phone:phone,
				source:source,
				owner:owner,
				mphone:mphone,
				state:state,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (data) {
				var htmlStr = "";
				<%--在线索主页面,显示线索列表和记录的总条数--%>
				$.each(data.clueList,function (index,obj) {
					htmlStr +="<tr class=\"active\">";
					htmlStr +="<td><input value='"+obj.id+"' type=\"checkbox\" /></td>";
					htmlStr +="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/queryClueForDetailById.do?id="+obj.id+"'\">"+obj.fullname+"</a></td>";
					htmlStr +="<td>"+obj.company+"</td>";
					htmlStr +="<td>"+obj.phone+"</td>";
					htmlStr +="<td>"+obj.mphone+"</td>";
					htmlStr +="<td>"+obj.source+"</td>";
					htmlStr +="<td>"+obj.owner+"</td>";
					htmlStr +="<td>"+obj.state+"</td>";
					htmlStr +="</tr>";
				});
				$("#tbody").html(htmlStr);
				<%--默认每页显示条数:10--%>
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
		});
	}
</script>
</head>
<body>

	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="createClueForm" role="form">
					
						<div class="form-group">
							<label for="create-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clueOwner">
									<option></option>
									<c:forEach items="${requestScope.users}" var="u">
										<%--当 当前对象的ID 等于 当前线索的所有者时 选中--%>
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
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-appellation">
									<option></option>
									<c:forEach items="${requestScope.appellationList}" var="a">
												<option value="${a.id}">${a.value}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-fullname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-company-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company-phone">
							</div>
							<label for="create-company-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-clue-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-clue-mphone">
							</div>
							<label for="create-clue-state" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clue-state">
									<option></option>
									<c:forEach items="${requestScope.clueStateList}" var="cs">
										<option value="${cs.id}">${cs.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-clue-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clue-source">
									<option></option>
									<c:forEach items="${requestScope.sourceList}" var="s">
										<option value="${s.id}">${s.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						

						<div class="form-group">
							<label for="create-clue-description" class="col-sm-2 control-label">线索描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-clue-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contact-summary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contact-summary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-next-contact-time" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control myDate" id="create-next-contact-time">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>
						
						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateClueBtn" >保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="editClueForm" role="form">
						<%--隐藏域--%>
						<input type="hidden" id="editId" />
						<div class="form-group">
							<label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueOwner">
									<option></option>
									<c:forEach items="${requestScope.users}" var="u">
										<option value="${u.id}" >${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation">
									<option></option>
									<c:forEach items="${requestScope.appellationList}" var="a">
										<option value="${a.id}">${a.value}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-fullname" value="李四">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
							<label for="edit-state" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-state">
									<option></option>
									<c:forEach items="${requestScope.clueStateList}" var="cs">
										<option value="${cs.id}">${cs.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
									<option></option>
									<c:forEach items="${requestScope.sourceList}" var="s">
										<option value="${s.id}">${s.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">这是一条线索的描述信息</textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control myDate" id="edit-nextContactTime" value="2017-05-01">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateClueBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" id="fullnameBtn" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" id="companyBtn" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" id="phoneBtn" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
					  <select class="form-control" id="selectClueSource">
						  <option></option>
						  <c:forEach items="${requestScope.sourceList}" var="s">
							  <option value="${s.id}">${s.value}</option>
						  </c:forEach>
					  	  <%--<option></option>
					  	  <option>广告</option>
						  <option>推销电话</option>
						  <option>员工介绍</option>
						  <option>外部介绍</option>
						  <option>在线商场</option>
						  <option>合作伙伴</option>
						  <option>公开媒介</option>
						  <option>销售邮件</option>
						  <option>合作伙伴研讨会</option>
						  <option>内部研讨会</option>
						  <option>交易会</option>
						  <option>web下载</option>
						  <option>web调研</option>
						  <option>聊天</option>--%>
					  </select>
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="ownerBtn" type="text">
				    </div>
				  </div>
				  
				  
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" id="mphoneBtn" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
					  <select class="form-control" id="selectClueState">
						  <option></option>
						  <c:forEach items="${requestScope.clueStateList}" var="cs">
							  <option value="${cs.id}">${cs.value}</option>
						  </c:forEach>
					  	<%--<option></option>
					  	<option>试图联系</option>
					  	<option>将来联系</option>
					  	<option>已联系</option>
					  	<option>虚假线索</option>
					  	<option>丢失线索</option>
					  	<option>未联系</option>
					  	<option>需要条件</option>--%>
					  </select>
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="queryBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createClueBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editClueBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteClueBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="fristCheckboxBtn" type="checkbox" /></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>
					</thead>
					<tbody id="tbody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
							<td>动力节点</td>
							<td>010-84846003</td>
							<td>12345678901</td>
							<td>广告</td>
							<td>zhangsan</td>
							<td>已联系</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
                            <td>动力节点</td>
                            <td>010-84846003</td>
                            <td>12345678901</td>
                            <td>广告</td>
                            <td>zhangsan</td>
                            <td>已联系</td>
                        </tr>
                        --%>
					</tbody>

				</table>
				<%--显示查询的市场活动信息的div--%>
				<div id="demo_pag1"></div>
			</div>

			<%--<div style="height: 50px; position: relative;top: 60px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
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