$('.reply-write input[type=submit]').click(addReply);

function addReply(e) {	
	e.preventDefault();
	console.log("눌러져부렀다");
	
	var queryString = $('.reply-write').serialize();
	console.log("query :" + queryString);
	
	var url = $('.reply-write').attr('action');
	console.log("url : " + url);
	
	$.ajax({
		type : 'post',
		url : url,
		data : queryString,
		dataType : 'json',
		error : onError,
		success : onSuccess		
	});
}
	
function onError() {}
	
function onSuccess(data, status) {	
	console.log(data);	
	var replytemplate = $('#replyTemplate').html();
	var template = replytemplate.format(data.writer.userId, data.formattedCreateDate, data.contents,data.question.id,data.id);
	$('.qna-comment-slipp-articles').prepend(template);	
	$('textarea').val("");
}
		

$('a.link-delete-article').click(deleteReply);	


function deleteReply(e){
	e.preventDefault();
		
	var deleteBtn = $(this);
	var url = deleteBtn.attr('href');
	console.log(url);
	
	$.ajax({
		type : 'delete',
		url : url,
		dataType : 'json',
		error : function(xhr, status) {
			console.log("error");					
		},
		success : function(data, status) {
			console.log(data);
			if(data.valid) {
				deleteBtn.closest("article").remove();
			} else {
				alert(data.errorMessage);
			}
		}
	});
}
	

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};
	
	
