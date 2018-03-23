<div id="step3" class="no-separator"  style="display:none;">
  <div class="form-row">
      <h2 class="coral-Heading coral-Heading--2"><b>Step 3 : Define Dialog attributes</b></h2>
      <hr/>
  </div>
  
  <div class="form-row">
      <button
          ng-click="saveConfig4();"
          is="coral-button" variant="primary" icon="checkCircle" iconsize="S">
      Add Field
      </button>
      
       <button
          ng-click="saveConfig5();"
          is="coral-button" variant="primary" icon="checkCircle" iconsize="S">
     Show /Hide HTML Script
      </button>
      
      
      <div class="form-row" id="showHTML" style="display:none;">
        <textarea id = "htmlText" is="coral-textarea" ng-required="true" ng-pattern="/^\/.+$/" ng-model="form.componentHtml" placeholder="Provide Component HTML Design" name="componentHtml" value="" rows="20" cols="500">
                    </textarea>
      </div>      
      
  <div class="form-row">
  
      <table class="coral-Table coral-Table--bordered">
          <thead class="coral-Table-head">
              <tr class="coral-Table-row">
                  <th class="coral-Table-headerCell">S.no</th>
                  <th class="coral-Table-headerCell">Dialog Field Type</th>
                  <th class="coral-Table-headerCell">Change Field Type</th>
                  <th class="coral-Table-headerCell">Is Mandatory?</th>
                  <th class="coral-Table-headerCell">Field Label</th>
              </tr>
          </thead>
          <tbody id="table" class="coral-Table-body js-dialog-create-table">
          </tbody>
      </table>
  </div>
  <div class="form-row">
      <button
          ng-click="form.enabled = true; saveConfig3();"
          is="coral-button" variant="primary" icon="checkCircle" iconsize="S">
      Create Component
      </button>
  </div>
</div>
</form>