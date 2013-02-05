

DT = {};

DT.clearGlobalTips = function(){
	$('#nav').poshytip('hide');
}

DT.showGlobalTips = function(){
	$('#nav').poshytip({
		showOn: 'none'
	});
	$('#nav').poshytip('show');
}

DT.showTips = function(){
	//$("#nav [title]").poshytip();
}

DT.removeTweet = function(id) {
	$("#" + id).fadeOut(1000, function() {
		$.post("tweetRpc/deleteTweet.json", {
			"tid" : id
		}, function(data) {
			if(!data.success){
				alert(data.errorCode);
			}
		}, "json");
	});
}


DT.nextPage = function(maxSort) {
	DT._page({
		"maxSort" : maxSort
	});
}

DT._page = function(params,func) {
	$.post("tweetRpc/page.json", $.extend({}, Context.searchInfo,params), function(data) {
		if(func){
			func(data);
		}
		else{
			if(data.success){
				$("#detail_list").html(data.page);
				Context.maxSort=data.maxSort;
				$("#nav").html(data.nav);
				DT.showTips();
				DT.scrollToTop();
				Context.searchInfo=data.searchInfo;
			}
		}
	}, "json");
}


DT.firstPage = function() {
	DT._page();
}

DT.userTimeline = function() {
	Context.searchInfo.demo=false;
	Context.searchInfo.useFollower=true;
	Context.searchInfo.type="TIMELINE";
	DT._page();
}

DT.demoTimeline = function() {
	Context.searchInfo.demo=true;
	Context.searchInfo.useFollower=true;
	Context.searchInfo.type="TIMELINE";
	DT._page();
}

DT.topReposts = function() {
	Context.searchInfo.useFollower=false;
	Context.searchInfo.type="TOP_REPOSTS";

	DT._page();
}

DT.update = function(params) {
	$.extend(Context.searchInfo,params)
	DT._page();
}



DT.mediaOpen = function(id) {
	$("#" + id +" [node-type='media_thumbnail']").hide();
	$("#" + id +" [node-type='media_bmiddle']").show();
}

DT.mediaClose = function(id) {
	$("#" + id +" [node-type='media_bmiddle']").hide();
	$("#" + id +" [node-type='media_thumbnail']").show();
}

DT.reasonMediaOpen = function(id) {
	$("#" + id +" [node-type='reason_media_thumbnail']").hide();
	$("#" + id +" [node-type='reason_media_bmiddle']").show();
}

DT.reasonMediaClose = function(id) {
	$("#" + id +" [node-type='reason_media_bmiddle']").hide();
	$("#" + id +" [node-type='reason_media_thumbnail']").show();
}

DT.scrollToTop = function() {
	App.scrollToTop();
}

DT.capture = function(id){
	
	$("#" + id).html2canvas({},function(imgData,w,h){
		window.open(imgData);

	});
}

$(document).ready(function() {
	
	// 查找新删除的微薄
	//new_tweets_message
	var countNew = function() {
		DT._page({
			"minSort" : Context.maxSort,
			"type" : "COUNT_NEW"
			},function(data){
				if(data.success){
					$("#new_tweets_message").html(data.page);
				}
				setTimeout(countNew, 10000);
			});
	}
	countNew();//btn_account btn_top_reposts btn_logout

	$("[image-type='pic']").remove();


	
	//App.scrollToTop();
	DT.showTips();
	DT.showGlobalTips();

	

});
