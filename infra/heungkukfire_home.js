if (confResult) {
	try {
		var origin = document.domain;
		document.domain = 'heungkukfire.co.kr';
		var $frame = $('#chatFrame').find('iframe').contents();
		console.warn($frame.find('#user input[name=id]').val());

		$.ajax({
			url: 'https://dev-chat.heungkukfire.co.kr/happytalk/api/customer/endChatRoom',
			method: 'get',
			data: {
				cstmUid: $frame.find('#user input[name=id]').val()
			}
		}).done(function(data, textStatus, jqXHR) {
			console.info(data);
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: END CHATROOM: ', textStatus);
		}).always(function() {
			document.domain = origin;
		});
	} catch (e) {
		console.error(e);
	}
}
