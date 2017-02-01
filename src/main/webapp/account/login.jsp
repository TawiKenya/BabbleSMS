<!DOCTYPE html>
<!-- The styles -->
<link id="bs-css" href="bootstrap/css/bootstrap-cerulean.css" rel="stylesheet">
<style type="text/css">
    body {
        padding-bottom: 40px;
    }
    .sidebar-nav {
        padding: 9px 0;
    }
</style>
<link href="css/boorstrap/bootstrap-responsive.css" rel="stylesheet">
<link href="css/tawi/charisma-app.css" rel="stylesheet">
<link href="css/jquery/jquery-ui-1.8.21.custom.css" rel="stylesheet">
<link href='css/tawi/fullcalendar.css' rel='stylesheet'>
<link href='css/tawi/fullcalendar.print.css' rel='stylesheet'  media='print'>
<link href='css/tawi/chosen.css' rel='stylesheet'>
<link href='css/tawi/uniform.default.css' rel='stylesheet'>
<link href='css/tawi/colorbox.css' rel='stylesheet'>
<link href='css/jquery/jquery.cleditor.css' rel='stylesheet'>
<link href='css/jquery/jquery.noty.css' rel='stylesheet'>
<link href='css/tawi/noty_theme_default.css' rel='stylesheet'>
<link href='css/tawi/elfinder.min.css' rel='stylesheet'>
<link href='css/tawi/elfinder.theme.css' rel='stylesheet'>
<link href='css/jquery/jquery.iphone.toggle.css' rel='stylesheet'>
<link href='css/tawi/opa-icons.css' rel='stylesheet'>
<link href='css/tawi/uploadify.css' rel='stylesheet'>

<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<html lang="en">

<body>


                <div class="row-fluid">
                        <div class="span12 center login-header">
                                <h2>Welcome to Babble SMS</h2>
                        </div><!--/span-->
</div><!--/row-->

<div class="row-fluid">
    <div class="well span5 center login-box">
        <div class="alert alert-info">
            Please login with your Username and Password.
        </div>
        <form class="form-horizontal" action="../Login" method="POST">
            <fieldset>
                <div class="input-prepend" title="Username" data-rel="tooltip">
                    <span class="add-on"><i class="icon-user"></i></span><input autofocus class="input-large span10" name="username" id="username" type="text" value="" />
                </div>
                <div class="clearfix"></div>

                <div class="input-prepend" title="Password" data-rel="tooltip">
                    <span class="add-on"><i class="icon-lock"></i></span><input class="input-large span10" name="password" id="password" type="password" value="" />
                </div>
                <div class="clearfix"></div>

                <div class="input-prepend">
                    <label class="remember" for="remember"><input type="checkbox" id="remember" />Remember me</label>
                </div>
                <div class="clearfix"></div>

                <p class="center span5">
                    <button type="submit" class="btn btn-primary">Login</button>
                </p>
            </fieldset>
        </form>
    </div><!--/span-->
</div><!--/row-->


</body>
</html>
