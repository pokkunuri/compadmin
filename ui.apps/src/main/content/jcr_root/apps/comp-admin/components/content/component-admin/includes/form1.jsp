<div id="step1" class="no-separator" novalidate style="display:none;">
   <div class="form-row">
   	<h2 class="coral-Heading coral-Heading--2"><b>Step 1 : Define Component Properties</b></h2>
   	<hr/>
   </div>
   
   <div class="form-row">
   	<label acs-coral-heading>
			Component Title
		</label>
		<span>
			<input type="text" name="componentName" class="coral-Textfield"  ng-required="true" ng-pattern="/^\/.+$/" ng-model="form.componentName" placeholder="Provide Component Title"/>
		</span>
   </div>
   
    <div class="form-row">
   	<label acs-coral-heading style="float:left;">
			Component Description
		</label>
		<span>
			<input type="text" name="componentDesc" class="coral-Textfield"  ng-required="false" ng-pattern="/^\/.+$/" ng-model="form.componentDesc" placeholder="Provide Component Description"/>
		</span>
   </div>
   
   
   <div class="form-row">
   	<label acs-coral-heading style="float:left;">
			Component Group
		</label>
		<span>
			<input type="text" name="componentGrp" class="coral-Textfield"  ng-required="true" ng-pattern="/^\/.+$/" ng-model="form.componentGrp" placeholder="Component group where it should display in the sidekick"/>
		</span>
   </div>
   <div class="form-row">
   <button
                ng-click="form.enabled = true; saveConfig1();"
                is="coral-button" variant="primary" icon="checkCircle" iconsize="S">
            Next Step
        </button>
        </div>
</div>
