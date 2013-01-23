$(document).ready(function() {
	App.scrollToTop();
	setTimeout(function() {
		//App.setPageHeight(document.body.clientHeight);
	}, 200);

});

DT = {};
DT.removeTweet = function(id) {
//	$("#" + id).cluetip({
//		  hoverClass: 'highlight',
//		  sticky: true,
//		  closePosition: 'bottom',
//		  closeText: '<img src="cross.png" alt="close" />',
//		  truncate: 60,
//		  ajaxSettings: {
//		    type: 'POST'
//		  }
//		});
	
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



DT.mediaOpen = function(id) {	
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

