﻿<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <!--                                           -->
        <!-- Any title is fine                         -->
        <!--                                           -->
        <title>Application</title>
         <script type="text/javascript" language="javascript">
        	var eshopContextPath = "<%=request.getContextPath()%>";
        </script>
        
        <style>
* {
  margin: 0px;
  padding: 0px;
}

#loading {
  position: absolute;
  left: 45%;
  top: 40%;
  margin-left: -45px;
  padding: 2px;
  z-index: 20001;
  height: auto;
  border: 1px solid #ccc;
}

#loading a {
  color: #225588;
}

#loading .loading-indicator {
  background: white;
  color: #444;
  font: bold 13px tahoma, arial, helvetica;
  padding: 10px;
  margin: 0;
  height: auto;
}

#loading .loading-indicator img {
  margin-right:8px;
  float:left;
  vertical-align:top;
}

#loading-msg {
  font: normal 10px arial, tahoma, sans-serif;
}
</style>

        <!--                                           -->
        <!-- This script loads your compiled module.   -->
        <!-- If you add any GWT meta tags, they must   -->
        <!-- be added before this line.                -->
        <!--                                           -->
        <script type="text/javascript" language="javascript" src="eshop/eshop.nocache.js"></script>
    </head>

    <!--                                           -->
    <!-- The body can have arbitrary html, or      -->
    <!-- you can leave the body empty if you want  -->
    <!-- to create a completely dynamic UI.        -->
    <!--                                           -->
    <body>
    	<div id="loading">
    <div class="loading-indicator">
    <img src="eshop/images/default/shared/large-loading.gif" width="32" height="32"/>eShop 0.1<br />
    <span id="loading-msg">Loading&nbsp;eShop&nbsp;0.1&nbsp;...</span>
    </div>
</div>

        <!-- OPTIONAL: include this if you want history support -->
        <iframe src="javascript:''" id="__gwt_historyFrame" style="position:absolute;width:0;height:0;border:0"></iframe>

    </body>
</html>
