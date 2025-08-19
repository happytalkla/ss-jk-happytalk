/*
name : bookmark.js
maker : work6.kr
*/
(function ( $ ) {
    var pluginName = "bookmark";
    var defaults = {
        url:"http://work6.kr",
        icon:"http://work6.kr/skin/img/bookmark.png",
        icon_name:"워크식스",
        key:"work6"
    };
    function Plugin ( element, options ) {
        this.element = element;
        this.$element = $(element);
        this.settings = $.extend({}, defaults, options );
        this.init();
    }
    $.extend(Plugin.prototype, {
        init: function () {
			
			_this = this;
            
            _this.settings = _this.encoding(_this.settings);
            
            
            cookie = _this.getCookie('bookmark');
            
            if(cookie!="active"){

                browser = navigator.userAgent.toLowerCase();

                if(-1 != browser.indexOf("naver")){

                    _this.setCookie('bookmark','active',90);

                   location.replace("naversearchapp://addshortcut?url="+_this.settings.url+"&icon="+_this.settings.icon+"&title="+_this.settings.icon_name+"&serviceCode="+_this.settings.key+"&version=7");

                }
            }
	    },
        encoding: function(settings){
            
            settings.url = encodeURIComponent(settings.url);
            settings.icon = encodeURIComponent(settings.icon);
            settings.icon_name = encodeURIComponent(settings.icon_name);
            
            return settings; 
        },
        setCookie:function(cName,cValue,cDay){
          var expire = new Date();
            expire.setDate(expire.getDate() + cDay);
            cookies = cName + '=' + escape(cValue) + '; path=/ ';
            if(typeof cDay != 'undefined') cookies += ';expires=' + expire.toGMTString() + ';';
            document.cookie = cookies;  
        },
        getCookie:function(cName){
            cName = cName + '=';
            var cookieData = document.cookie;
            var start = cookieData.indexOf(cName);
            var cValue = '';
            if(start != -1){
                start += cName.length;
                var end = cookieData.indexOf(';', start);
                if(end == -1)end = cookieData.length;
                cValue = cookieData.substring(start, end);
            }
            return unescape(cValue);
        }
		
    });
    $[ pluginName ] = $.fn[ pluginName ] = function ( options ) {
        return this.each(function() {
            if ( !$.data( this, "plugin_" + pluginName ) ) {
                $.data( this, "plugin_" + pluginName, new Plugin( this, options ) );
            }
        });
    };
    $.fn[ pluginName ].defaults = defaults;
})( jQuery, window, document );