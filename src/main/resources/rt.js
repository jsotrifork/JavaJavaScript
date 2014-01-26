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

//// core classes follow: 

java_lang_Object = (function () {
    function java_lang_Object() {
    }
    java_lang_Object.prototype.getClass = function () {
    	return java_lang_Object;
    }
    java_lang_Object.prototype.hashCode = function () {
    	if (!this.__hashCode) {
    		this.__hashCode = id++;
    	}
    	return this.hashCode;
    }
    java_lang_Object.prototype.equals = function(other) {
    	return this === other;
    }

    java_lang_Object.prototype.toString = function() {
//        return getClass().getName() + "@" + Integer.toHexString(hashCode());
        return getClass().getName() + "@" + hashCode();
    }
    
    return java_lang_Object;
}
)();
