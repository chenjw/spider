$(document).ready(function() {
	
	// 查找新删除的微薄
	//new_tweets_message
	var countNew = function() {
		$.post("/countNew.htm", {
			"minSort" : Context.maxSort
		}, function(data) {
			$("#new_tweets_message").html(data);
			setTimeout(countNew, 10000);
		});
	}
	countNew();
	App.scrollToTop();

});

DT = {};
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
	$.post("tweetRpc/nextPage.json", {
		"maxSort" : maxSort
	}, function(data) {
		if(data.success){
			$("#detail_list").html(data.page);
			DT.scrollToTop();
		}
	}, "json");
}

DT.firstPage = function() {
	$.post("tweetRpc/firstPage.json", {}, function(data) {
		if(data.success){
			$("#detail_list").html(data.page);
			Context.maxSort=data.maxSort;
			$("#new_tweets_message").html("");
			DT.scrollToTop();
		}
	}, "json");
}


DT.topReposts = function() {
	$.post("tweetRpc/topReposts.json", {}, function(data) {
		if(data.success){
			$("#detail_list").html(data.page);
			DT.scrollToTop();
		}
	}, "json");
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

