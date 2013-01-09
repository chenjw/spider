<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.lang.System" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.List" %>
<%@ page import="com.chenjw.spider.dt.model.*" %>
<%@ page import="com.chenjw.spider.dt.service.*" %>
<%@ page import="org.springframework.web.context.ContextLoader" %>
<html xmlns="http://www.w3.org/1999/xhtml" debug="true">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<link href="css/frame.css" type="text/css" rel="stylesheet" charset="utf-8"/>
	<link href="css/index.css" type="text/css" rel="stylesheet" charset="utf-8" />
	<link href="css/skin.css" type="text/css" rel="stylesheet" charset="utf-8"/>
	<title>我的首页 新浪微博-随时随地分享身边的新鲜事儿</title>
</head>
<body class="B_index L-zh-cn" style="">

<div class="W_miniblog">
	<div class="W_miniblog_fb">
		<div class="W_main">
			<div class="W_main_bg clearfix">
				<div class="W_main_l">
					<div class="WB_left_nav">
					
					</div>
				</div>
				<div id="plc_main">
					<div class="W_main_a">
						<div class="W_main_c" id="Box_center" ucardconf="type=1" smartconf="type=1">
							<div id="pl_content_homeFeed">
								<div class="WB_feed" pagenum="1" node-type="feed_list">
<%

	DeletedTweetService deletedTweetService = (DeletedTweetService)ContextLoader.getCurrentWebApplicationContext().getBean("deletedTweetService");
	List<TweetModel> list=deletedTweetService.findDeletedTweets();
	for (TweetModel tweet : list) {
%>								
								
	<div diss-data="group_source=group_gid"
		class="WB_feed_type SW_fun S_line2" mid="3531578585860772"
		isforward="1" action-type="feed_list_item"
		filtered_min_id="3531550861370129">
		<div class="WB_screen">
			<div class="WB_feed_datail S_line2 clearfix">
				<div class="WB_face">
					<a class="W_face_radius" href="/u/1199062227" title="RDL兰"> <img
						usercard="id=1199062227" title="RDL兰" alt="" width="50"
						height="50" src="http://tp4.sinaimg.cn/1199062227/50/5649771033/0" />
					</a>
				</div>
				<div class="WB_detail">
					<div class="WB_info">
						<a class="WB_name S_func1" nick-name="RDL兰" title="RDL兰"
							href="http://weibo.com/u/<%=tweet.getUser().getId()%>" usercard="id=<%=tweet.getUser().getId()%>"><%=tweet.getUser().getScreenName()%></a>
					</div>
					<div class="WB_text" node-type="feed_list_content">
						<%=tweet.getText()%>
					</div>
					<div class="WB_func clearfix">
					</div>
					<div node-type="feed_list_repeat"
						class="WB_media_expand repeat S_line1 S_bg4"
						style="display: none;" />
				</div>
			</div>
		</div>
	</div>
<%
	}
%>	
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>