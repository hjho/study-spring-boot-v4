
const AjaxUtils = {
	isLog: function() {
		return false;	
	},
	getContentType: function(type) {
		const typeLower = type.toLowerCase();
		if(typeLower === 'json') {
			return 'application/json; charset=utf-8';
		}
		return 'application/x-www-form-urlencoded; charset=utf-8';
	},
	sendGet: function(url, input, cbSuccess) {
		AjaxUtils.send(url, 'GET', input, true, 'form', cbSuccess);
	},
	sendPost: function(url, input, cbSuccess) {
		AjaxUtils.send(url, 'POST', input, true, 'form', cbSuccess);
	},
	sendJson: function(url, input, cbSuccess) {
		AjaxUtils.send(url, 'POST', JSON.stringify(input), true, 'json', cbSuccess);
	},
	sendFile: function(url, formData, cbSuccess) {
		AjaxUtils.send(url, 'POST', formData, true, 'part', cbSuccess);
	},
	// Ajax Send
	send: function(url, method, input, isAsync, type, cbSuccess, cbError, cbComplate) {
		const ajaxObj = 
			$.ajax(url, { 
			// options
				method      : (method) ? method : "POST",	// GET
				async       : (isAsync === false) ? false : true,	// true
				data        : input,	
				contentType : (type === 'part') ? false : AjaxUtils.getContentType(type),	// 'application/x-www-form-urlencoded; charset=utf-8'
				processData : (type === 'part') ? false : true,		// true
				dataType    : "json",
				beforeSend  : function(_, settings) {// 1:jqXHR
					// jqXHR.setRequestHeader('header-name', 'header-value');
					if(StringJsUtils.isEmpty(url)) {
						throw 'URL은 필수 입력 값 입니다.';
						// return false;
					} else if(typeof cbSuccess !== 'function') {
						throw 'CALLBACK 함수는 필수 입력 값 입니다.';
						// return false;
					}
					if(AjaxUtils.isLog()) {
						console.log("CALL: ("+ settings.type +")", settings.url);
						if(settings.type === "POST") {
							console.log("INPUT: ", settings.data);
						}
					}
				}
			// success
			}).done(function(output, _, _) {// 1:data, 2:textStatus, 3:jqXHR
				if(AjaxUtils.isLog()) {
					console.log("SUCCESS:", output);
				} 
				cbSuccess(output);
			// error	
			}).fail(function(jqXHR, _, _) { // 1:jqXHR, 2:textStatus, 3:errorThrown
				if(AjaxUtils.isLog()) {
					console.log("ERROR:", jqXHR.responseJSON);
				} 
				if(typeof cbError === 'function') {
					cbError(jqXHR.responseJSON);
				} else {
					AjaxUtils.error(jqXHR.responseJSON);
				}
			// complete
			}).always(function(_, _, _) {// 1:data|jqXHR, 2:textStatus, 3:jqXHR|errorThrown
				if(AjaxUtils.isLog()) {
					console.log("COMPLETE");
				}
				if(typeof cbComplate === 'function') {
					cbComplate(jqXHR.responseJSON);
				}
			}
		);
		// then complete
		ajaxObj.always(function(_, _, _) {// 1:data|jqXHR, 2:textStatus, 3:jqXHR|errorThrown
			/*
			console.log('THEN COMPLETE: ', textStatus);
			let debug = 'OUTPUT: ';
			if(typeof output === 'string') {
				debug += output;
			} else if(typeof output === 'object') {
				if(Array.isArray(output)) {
					output.forEach(function(value, index, array) {
						debug += `${value}, `;
					});
				} else {
					for (const [key, value] of Object.entries(output)) {
						debug += `${key}=${value}, `;
					}
				}
			}
			if(AjaxUtils.isLog()) {
				console.log('THEN COMPLETE: ', output);
			} 
			*/
		});
	},
	error: function(error) {
		let message = '요청 중 알수 없는 오류가 발생했습니다.';
		if(error != null) {
			if(AjaxUtils.isLog()) {
				let errorLog =  'ERROR : ' + '(' + error.status + ') ' + error.error + ', ';
				    errorLog += error.message + ', URL [' + error.path + '], 시간 [' + new Date(error.timestamp).toLocaleString() + ']';
				console.log(errorLog);
			} 
			
			if(error.message != null && error.message !== undefined && error.message !== "") {
				message = error.message;
			}
		} 
		switch(error.status) {
			case 403: alert("권한이 없습니다."); break;
			case 404: alert("존재하지 않은 페이지입니다."); break;
			case 500: alert(message); break;
			default : alert(message); break;
		}
	},
}

const StringJsUtils = {
	isEmpty: function(s) {
		if(s === undefined || s === null || (typeof s === 'string' && s === "")) {
			return true;
		}
		return false;
	},
	isNotEmpty: function(s) {
		return !StringJsUtils.isEmpty(s);
	},
}

const ObjectJsUtils  = {
	isObject: function(o) {
		if(typeof o === 'object' && !Array.isArray(o)) {
			return true;
		}
		return false;
	},
	isEmpty: function(o) {
		if(o === undefined || o === null || (typeof o === 'object' && JSON.stringify(o) === '{}')) {
			return true;
		}
		return false;
	},
	isNotEmpty: function(o) {
		return !ObjectJsUtils.isEmpty(o);
	},
	serialize: function(o) {
		let queryString = "";
		if(typeof o === 'object' && !Array.isArray(o)) {
			Object.keys(o).map(function(key, index, _) {//_:array
				queryString += (index === 0) ? "" : "&";
				queryString += key + "=" + encodeURIComponent(o[key]);
			});
		} else {
			queryString = o;
		}
		return queryString;
	},
}