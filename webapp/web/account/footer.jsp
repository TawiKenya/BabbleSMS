<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the ?License?); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an ?AS IS? BASIS, WITHOUT WARRANTIES OR
    CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and limitations 
    under the License.
    */
%>

<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Calendar" %>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<% 
    String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);

    CacheManager mgr = CacheManager.getInstance();
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);

    Account account = new Account();
    Element element;

    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }

    String accountuuid = account.getUuid();
%>


<hr>

<div class="modal hide fade" id="myModal">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3>Settings</h3>
    </div>
    <div class="modal-body">
        <p>Here settings can be configured...</p>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">Close</a>
        <a href="#" class="btn btn-primary">Save changes</a>
    </div>
</div>

<footer>
    <center><p class="pull-left">&copy;Tawi Commercial Services Ltd <%= Calendar.getInstance().get(Calendar.YEAR)%>. All rights reserved.</p><center>
    <!--<p class="pull-right">Powered by: <a href="#">Cha</a></p>-->
</footer>


</div><!--/.fluid-container-->

<!-- external javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->


<!-- jQuery UI -->
 <!-- jQuery/javascript -->

        <!--<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>-->

<script src="../js/jquery/jquery-ui-1.8.21.custom.min.js"></script>
<!-- transition / effect library -->
<script src="../js/bootstrap/bootstrap-transition.js"></script>
<!-- alert enhancer library -->
<script src="../js/bootstrap/bootstrap-alert.js"></script>
<!-- modal / dialog library -->
<script src="../js/bootstrap/bootstrap-modal.js"></script>
<!-- custom dropdown library -->
<script src="../js/bootstrap/bootstrap-dropdown.js"></script>
<!-- scrolspy library -->
<script src="../js/bootstrap/bootstrap-scrollspy.js"></script>
<!-- library for creating tabs -->
<script src="../js/bootstrap/bootstrap-tab.js"></script>
<!-- library for advanced tooltip -->
<script src="../js/bootstrap/bootstrap-tooltip.js"></script>
<!-- popover effect library -->
<script src="../js/bootstrap/bootstrap-popover.js"></script>
<!-- button enhancer library -->
<script src="../js/bootstrap/bootstrap-button.js"></script>
<!-- accordion library (optional, not used in demo) -->
<script src="../js/bootstrap/bootstrap-collapse.js"></script>
<!-- carousel slideshow library (optional, not used in demo) -->
<script src="../js/bootstrap/bootstrap-carousel.js"></script>
<!-- autocomplete library -->
<script src="../js/bootstrap/bootstrap-typeahead.js"></script>
<!-- tour library -->
<script src="../js/bootstrap/bootstrap-tour.js"></script>
<!-- library for cookie management -->
<script src="../js/jquery/jquery.cookie.js"></script>
<!-- calander plugin -->
<script src='../js/tawi/fullcalendar.min.js'></script>
<!-- data table plugin -->
<script src='../js/jquery/jquery.dataTables.min.js'></script>

<!-- chart libraries start -->
<script src="../js/tawi/excanvas.js"></script>
<script src="../js/jquery/jquery.flot.min.js"></script>
<script src="../js/jquery/jquery.flot.pie.min.js"></script>
<script src="../js/jquery/jquery.flot.stack.js"></script>
<script src="../js/jquery/jquery.flot.resize.min.js"></script>
<!-- chart libraries end -->

<!-- select or dropdown enhancer -->
<script src="../js/jquery/jquery.chosen.min.js"></script>
<!-- checkbox, radio, and file input styler -->
<script src="../js/jquery/jquery.uniform.min.js"></script>
<!-- plugin for gallery image view -->
<script src="../js/jquery/jquery.colorbox.min.js"></script>
<!-- rich text editor library -->
<script src="../js/jquery/jquery.cleditor.min.js"></script>
<!-- notification plugin -->
<script src="../js/jquery/jquery.noty.js"></script>
<!-- file manager library -->
<script src="../js/jquery/jquery.elfinder.min.js"></script>
<!-- star rating plugin -->
<script src="../js/jquery/jquery.raty.min.js"></script>
<!-- for iOS style toggle switch -->
<script src="../js/jquery/jquery.iphone.toggle.js"></script>
<!-- autogrowing textarea plugin -->
<script src="../js/jquery/jquery.autogrow-textarea.js"></script>
<!-- multiple file upload plugin -->
<script src="../js/jquery/jquery.uploadify-3.1.min.js"></script>
<!-- history.js for cross-browser state change on ajax -->
<script src="../js/jquery/jquery.history.js"></script>
<!-- application script for Charisma demo -->
<script src="../js/tawi/charisma.js"></script>

<!--affect the popup on contact page-->
<script src="../js/tawi/editcontact.js"></script>
<!--affect the popup on contact page-->
<script src="../js/tawi/editcontact_popup.js"></script>
<!--manages the table on contactspergroup page-->
<script src="../js/tawi/contactgrpselected.js"></script>

<script type="text/javascript">

    $(function() {
        $("#infrom").datepicker({
            defaultDate: "+0d",
            changeMonth: true,
            numberOfMonths: 1,
            changeYear: true,
            onClose: function(selectedDate) {
                $("#into").datepicker("option", "minDate", selectedDate);
            }
        });
        $("#into").datepicker({
            defaultDate: "+1w",
            changeMonth: true,
            changeYear: true,
            numberOfMonths: 1,
            maxDate: "+0D",
            onClose: function(selectedDate) {
                $("#infrom").datepicker("option", "maxDate", selectedDate);
            }
        });
    });

    $(function() {
        $("#outfrom").datepicker({
            defaultDate: "+0d",
            changeMonth: true,
            numberOfMonths: 1,
            changeYear: true,
            onClose: function(selectedDate) {
                $("#outto").datepicker("option", "minDate", selectedDate);
            }
        });
        $("#outto").datepicker({
            defaultDate: "+1w",
            changeMonth: true,
            changeYear: true,
            numberOfMonths: 1,
            maxDate: "+0D",
            onClose: function(selectedDate) {
                $("#from").datepicker("option", "maxDate", selectedDate);
            }
        });
    });
    $(document).ready(function() {
        

        // process the form
        $('#incomingform').submit(function(event) {
            document.getElementById('inbar').src = '../incomingBarDay?accountuuid=<%=URLEncoder.encode(accountuuid, "UTF-8")%>&from='+$('#infrom').val()+'&to='+$('#into').val();            
            // stop the form from submitting the normal way and refreshing the page
            event.preventDefault();
        });

        $('#outgoingform').submit(function(event) {
           document.getElementById('outbar').src = '../outgoingBarDay?accountuuid=<%=URLEncoder.encode(accountuuid, "UTF-8")%>&from='+$('#infrom').val()+'&to='+$('#into').val();
            // stop the form from submitting the normal way and refreshing the page
            event.preventDefault();
        });
    });
</script>

</body>
</html>
