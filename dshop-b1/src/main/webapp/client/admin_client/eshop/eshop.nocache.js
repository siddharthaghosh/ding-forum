function eshop(){
  var $intern_0 = '', $intern_29 = '" for "gwt:onLoadErrorFn"', $intern_27 = '" for "gwt:onPropertyErrorFn"', $intern_12 = '"><\/script>', $intern_14 = '#', $intern_37 = '&', $intern_67 = '.cache.html', $intern_16 = '/', $intern_54 = '151EE16E4AF2D87E59035E74EFFC40B2', $intern_56 = '343AAE0E3CEF3E774C7642793516B60A', $intern_57 = '419C9EBE7AEEBFDC9C4A193D1BC2242A', $intern_58 = '544CED808B475D04C6162FC529F44E61', $intern_59 = '54F1716BE119965BF74FDDC17F2A4B25', $intern_60 = '57DDCCE9166A55D158FBA9C21CF73324', $intern_61 = '8F1C69F35209206A2A3FD22DE28E86B4', $intern_78 = '<script defer="defer">eshop.onInjectionDone(\'eshop\')<\/script>', $intern_11 = '<script id="', $intern_24 = '=', $intern_15 = '?', $intern_62 = 'A3F2058593ECCDFBBDA0B62D0AC6402F', $intern_63 = 'AC9AA7A8C24B938BC190F56DE8E7A245', $intern_26 = 'Bad handler "', $intern_64 = 'C2EA89BE39F123BBCFC50033A62C5373', $intern_65 = 'CE5C0BB8B7DD5F3BDA5FB6320A9538F9', $intern_66 = 'D14B0DBB44B74E5A3AF527B29299927F', $intern_77 = 'DOMContentLoaded', $intern_13 = 'SCRIPT', $intern_40 = 'Unexpected exception in locale detection, using default: ', $intern_76 = 'Upload.css', $intern_39 = '_', $intern_38 = '__gwt_Locale', $intern_10 = '__gwt_marker_eshop', $intern_17 = 'base', $intern_4 = 'begin', $intern_3 = 'bootstrap', $intern_19 = 'clear.cache.gif', $intern_23 = 'content', $intern_74 = 'css/gxt-all.css', $intern_75 = 'css/resources.css', $intern_35 = 'default', $intern_9 = 'end', $intern_1 = 'eshop', $intern_48 = 'gecko', $intern_49 = 'gecko1_8', $intern_5 = 'gwt.codesvr=', $intern_6 = 'gwt.hosted=', $intern_7 = 'gwt.hybrid', $intern_68 = 'gwt/standard/standard.css', $intern_28 = 'gwt:onLoadErrorFn', $intern_25 = 'gwt:onPropertyErrorFn', $intern_22 = 'gwt:property', $intern_73 = 'head', $intern_52 = 'hosted.html?eshop', $intern_72 = 'href', $intern_47 = 'ie6', $intern_46 = 'ie8', $intern_30 = 'iframe', $intern_18 = 'img', $intern_31 = "javascript:''", $intern_69 = 'link', $intern_51 = 'loadExternalRefs', $intern_34 = 'locale', $intern_36 = 'locale=', $intern_20 = 'meta', $intern_33 = 'moduleRequested', $intern_8 = 'moduleStartup', $intern_45 = 'msie', $intern_21 = 'name', $intern_42 = 'opera', $intern_32 = 'position:absolute;width:0;height:0;border:none', $intern_70 = 'rel', $intern_44 = 'safari', $intern_53 = 'selectingPermutation', $intern_2 = 'startup', $intern_71 = 'stylesheet', $intern_50 = 'unknown', $intern_41 = 'user.agent', $intern_43 = 'webkit', $intern_55 = 'zh';
  var $wnd = window, $doc = document, $stats = $wnd.__gwtStatsEvent?function(a){
    return $wnd.__gwtStatsEvent(a);
  }
  :null, $sessionId = $wnd.__gwtStatsSessionId?$wnd.__gwtStatsSessionId:null, scriptsDone, loadDone, bodyDone, base = $intern_0, metaProps = {}, values = [], providers = [], answers = [], onLoadErrorFunc, propertyErrorFunc;
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_4});
  if (!$wnd.__gwt_stylesLoaded) {
    $wnd.__gwt_stylesLoaded = {};
  }
  if (!$wnd.__gwt_scriptsLoaded) {
    $wnd.__gwt_scriptsLoaded = {};
  }
  function isHostedMode(){
    var result = false;
    try {
      var query = $wnd.location.search;
      return (query.indexOf($intern_5) != -1 || (query.indexOf($intern_6) != -1 || $wnd.external && $wnd.external.gwtOnLoad)) && query.indexOf($intern_7) == -1;
    }
     catch (e) {
    }
    isHostedMode = function(){
      return result;
    }
    ;
    return result;
  }

  function maybeStartModule(){
    if (scriptsDone && loadDone) {
      var iframe = $doc.getElementById($intern_1);
      var frameWnd = iframe.contentWindow;
      if (isHostedMode()) {
        frameWnd.__gwt_getProperty = function(name){
          return computePropValue(name);
        }
        ;
      }
      eshop = null;
      frameWnd.gwtOnLoad(onLoadErrorFunc, $intern_1, base);
      $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_8, millis:(new Date).getTime(), type:$intern_9});
    }
  }

  function computeScriptBase(){
    var thisScript, markerId = $intern_10, markerScript;
    $doc.write($intern_11 + markerId + $intern_12);
    markerScript = $doc.getElementById(markerId);
    thisScript = markerScript && markerScript.previousSibling;
    while (thisScript && thisScript.tagName != $intern_13) {
      thisScript = thisScript.previousSibling;
    }
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf($intern_14);
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf($intern_15);
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf($intern_16, Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):$intern_0;
    }

    ;
    if (thisScript && thisScript.src) {
      base = getDirectoryOfFile(thisScript.src);
    }
    if (base == $intern_0) {
      var baseElements = $doc.getElementsByTagName($intern_17);
      if (baseElements.length > 0) {
        base = baseElements[baseElements.length - 1].href;
      }
       else {
        base = getDirectoryOfFile($doc.location.href);
      }
    }
     else if (base.match(/^\w+:\/\//)) {
    }
     else {
      var img = $doc.createElement($intern_18);
      img.src = base + $intern_19;
      base = getDirectoryOfFile(img.src);
    }
    if (markerScript) {
      markerScript.parentNode.removeChild(markerScript);
    }
  }

  function processMetas(){
    var metas = document.getElementsByTagName($intern_20);
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name = meta.getAttribute($intern_21), content;
      if (name) {
        if (name == $intern_22) {
          content = meta.getAttribute($intern_23);
          if (content) {
            var value, eq = content.indexOf($intern_24);
            if (eq >= 0) {
              name = content.substring(0, eq);
              value = content.substring(eq + 1);
            }
             else {
              name = content;
              value = $intern_0;
            }
            metaProps[name] = value;
          }
        }
         else if (name == $intern_25) {
          content = meta.getAttribute($intern_23);
          if (content) {
            try {
              propertyErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_26 + content + $intern_27);
            }
          }
        }
         else if (name == $intern_28) {
          content = meta.getAttribute($intern_23);
          if (content) {
            try {
              onLoadErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_26 + content + $intern_29);
            }
          }
        }
      }
    }
  }

  function __gwt_isKnownPropertyValue(propName, propValue){
    return propValue in values[propName];
  }

  function __gwt_getMetaProperty(name){
    var value = metaProps[name];
    return value == null?null:value;
  }

  function unflattenKeylistIntoAnswers(propValArray, value){
    var answer = answers;
    for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
      answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
    }
    answer[propValArray[n]] = value;
  }

  function computePropValue(propName){
    var value = providers[propName](), allowedValuesMap = values[propName];
    if (value in allowedValuesMap) {
      return value;
    }
    var allowedValuesList = [];
    for (var k in allowedValuesMap) {
      allowedValuesList[allowedValuesMap[k]] = k;
    }
    if (propertyErrorFunc) {
      propertyErrorFunc(propName, allowedValuesList, value);
    }
    throw null;
  }

  var frameInjected;
  function maybeInjectFrame(){
    if (!frameInjected) {
      frameInjected = true;
      var iframe = $doc.createElement($intern_30);
      iframe.src = $intern_31;
      iframe.id = $intern_1;
      iframe.style.cssText = $intern_32;
      iframe.tabIndex = -1;
      $doc.body.appendChild(iframe);
      $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_8, millis:(new Date).getTime(), type:$intern_33});
      iframe.contentWindow.location.replace(base + initialHtml);
    }
  }

  providers[$intern_34] = function(){
    try {
      var locale;
      var defaultLocale = $intern_35 || $intern_35;
      if (locale == null) {
        var args = location.search;
        var startLang = args.indexOf($intern_36);
        if (startLang >= 0) {
          var language = args.substring(startLang);
          var begin = language.indexOf($intern_24) + 1;
          var end = language.indexOf($intern_37);
          if (end == -1) {
            end = language.length;
          }
          locale = language.substring(begin, end);
        }
      }
      if (locale == null) {
        locale = __gwt_getMetaProperty($intern_34);
      }
      if (locale == null) {
        locale = $wnd[$intern_38];
      }
       else {
        $wnd[$intern_38] = locale || defaultLocale;
      }
      if (locale == null) {
        return defaultLocale;
      }
      while (!__gwt_isKnownPropertyValue($intern_34, locale)) {
        var lastIndex = locale.lastIndexOf($intern_39);
        if (lastIndex == -1) {
          locale = defaultLocale;
          break;
        }
         else {
          locale = locale.substring(0, lastIndex);
        }
      }
      return locale;
    }
     catch (e) {
      alert($intern_40 + e);
      return $intern_35;
    }
  }
  ;
  values[$intern_34] = {'default':0, zh:1};
  providers[$intern_41] = function(){
    var ua = navigator.userAgent.toLowerCase();
    var makeVersion = function(result){
      return parseInt(result[1]) * 1000 + parseInt(result[2]);
    }
    ;
    if (ua.indexOf($intern_42) != -1) {
      return $intern_42;
    }
     else if (ua.indexOf($intern_43) != -1) {
      return $intern_44;
    }
     else if (ua.indexOf($intern_45) != -1) {
      if (document.documentMode >= 8) {
        return $intern_46;
      }
       else {
        var result = /msie ([0-9]+)\.([0-9]+)/.exec(ua);
        if (result && result.length == 3) {
          var v = makeVersion(result);
          if (v >= 6000) {
            return $intern_47;
          }
        }
      }
    }
     else if (ua.indexOf($intern_48) != -1) {
      var result = /rv:([0-9]+)\.([0-9]+)/.exec(ua);
      if (result && result.length == 3) {
        if (makeVersion(result) >= 1008)
          return $intern_49;
      }
      return $intern_48;
    }
    return $intern_50;
  }
  ;
  values[$intern_41] = {gecko:0, gecko1_8:1, ie6:2, ie8:3, opera:4, safari:5};
  eshop.onScriptLoad = function(){
    if (frameInjected) {
      loadDone = true;
      maybeStartModule();
    }
  }
  ;
  eshop.onInjectionDone = function(){
    scriptsDone = true;
    $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_51, millis:(new Date).getTime(), type:$intern_9});
    maybeStartModule();
  }
  ;
  computeScriptBase();
  var strongName;
  var initialHtml;
  if (isHostedMode()) {
    if ($wnd.external && ($wnd.external.initModule && $wnd.external.initModule($intern_1))) {
      $wnd.location.reload();
      return;
    }
    initialHtml = $intern_52;
    strongName = $intern_0;
  }
  processMetas();
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_53});
  if (!isHostedMode()) {
    try {
      unflattenKeylistIntoAnswers([$intern_35, $intern_42], $intern_54);
      unflattenKeylistIntoAnswers([$intern_55, $intern_47], $intern_56);
      unflattenKeylistIntoAnswers([$intern_55, $intern_44], $intern_57);
      unflattenKeylistIntoAnswers([$intern_35, $intern_44], $intern_58);
      unflattenKeylistIntoAnswers([$intern_55, $intern_48], $intern_59);
      unflattenKeylistIntoAnswers([$intern_35, $intern_48], $intern_60);
      unflattenKeylistIntoAnswers([$intern_55, $intern_42], $intern_61);
      unflattenKeylistIntoAnswers([$intern_55, $intern_49], $intern_62);
      unflattenKeylistIntoAnswers([$intern_35, $intern_46], $intern_63);
      unflattenKeylistIntoAnswers([$intern_35, $intern_47], $intern_64);
      unflattenKeylistIntoAnswers([$intern_35, $intern_49], $intern_65);
      unflattenKeylistIntoAnswers([$intern_55, $intern_46], $intern_66);
      strongName = answers[computePropValue($intern_34)][computePropValue($intern_41)];
      initialHtml = strongName + $intern_67;
    }
     catch (e) {
      return;
    }
  }
  var onBodyDoneTimerId;
  function onBodyDone(){
    if (!bodyDone) {
      bodyDone = true;
      if (!__gwt_stylesLoaded[$intern_68]) {
        var l = $doc.createElement($intern_69);
        __gwt_stylesLoaded[$intern_68] = l;
        l.setAttribute($intern_70, $intern_71);
        l.setAttribute($intern_72, base + $intern_68);
        $doc.getElementsByTagName($intern_73)[0].appendChild(l);
      }
      if (!__gwt_stylesLoaded[$intern_74]) {
        var l = $doc.createElement($intern_69);
        __gwt_stylesLoaded[$intern_74] = l;
        l.setAttribute($intern_70, $intern_71);
        l.setAttribute($intern_72, base + $intern_74);
        $doc.getElementsByTagName($intern_73)[0].appendChild(l);
      }
      if (!__gwt_stylesLoaded[$intern_75]) {
        var l = $doc.createElement($intern_69);
        __gwt_stylesLoaded[$intern_75] = l;
        l.setAttribute($intern_70, $intern_71);
        l.setAttribute($intern_72, base + $intern_75);
        $doc.getElementsByTagName($intern_73)[0].appendChild(l);
      }
      if (!__gwt_stylesLoaded[$intern_76]) {
        var l = $doc.createElement($intern_69);
        __gwt_stylesLoaded[$intern_76] = l;
        l.setAttribute($intern_70, $intern_71);
        l.setAttribute($intern_72, base + $intern_76);
        $doc.getElementsByTagName($intern_73)[0].appendChild(l);
      }
      maybeStartModule();
      if ($doc.removeEventListener) {
        $doc.removeEventListener($intern_77, onBodyDone, false);
      }
      if (onBodyDoneTimerId) {
        clearInterval(onBodyDoneTimerId);
      }
    }
  }

  if ($doc.addEventListener) {
    $doc.addEventListener($intern_77, function(){
      maybeInjectFrame();
      onBodyDone();
    }
    , false);
  }
  var onBodyDoneTimerId = setInterval(function(){
    if (/loaded|complete/.test($doc.readyState)) {
      maybeInjectFrame();
      onBodyDone();
    }
  }
  , 50);
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_9});
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_51, millis:(new Date).getTime(), type:$intern_4});
  $doc.write($intern_78);
}

eshop();