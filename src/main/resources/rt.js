var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};

var baseUrl = "http://localhost:8080/jjs/loadclass/";

function loadClass(className) {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open("GET", baseUrl + className, false);
	xmlhttp.send();
	var cls = xmlhttp.responseText;
	var res = window.eval(cls);
	console.log("loaded class " + className);

	return res;
}