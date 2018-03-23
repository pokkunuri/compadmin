<div id="step2" class="no-separator" novalidate style="display:none;">
   <div class="form-row">
   	<h2 class="coral-Heading coral-Heading--2"><b>Step 2 : Setup the HTML Rendering script</b></h2>
   	<hr/>
   </div>
   
   <div class="form-row">
			<p>Paste the HTML Structure (especially the component structure):</p>
   </div>
   
    <div class="form-row">
    
   <table border="0">
   <tr>
   <td width="33%">
    <textarea is="coral-textarea" ng-required="true" ng-pattern="/^\/.+$/" ng-model="form.componentHtml" placeholder="Provide Component HTML Design" name="componentHtml" value="" rows="20" cols="500">
</textarea>
</td width="50%">
<td>
	<img src="/apps/comp-admin/components/content/component-admin/clientlibs/images/Picture2.png" width="114%"></ima>

   </td></tr></table>

     <div class="form-row">
   <button
                ng-click="form.enabled = true; saveConfig2();"
                is="coral-button" variant="primary" icon="checkCircle" iconsize="S">
            Next Step
        </button>
       </div> 
</div>
