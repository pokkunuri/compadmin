<form class="no-separator" novalidate >
<div id="step0" style="display:block;">
    <div class="form-row">
        <div class="form-left-cell">&nbsp;</div>
        <button
                ng-hide="form.enabled"
                ng-click="form.enabled = true; saveConfig();"
                is="coral-button" variant="primary" icon="checkCircle" iconsize="S">
            Begin Component Creation
        </button>

    </div>
 </div>
