<%--
  ~ #%L
  ~ ACS AEM Commons Bundle
  ~ %%
  ~ Copyright (C) 2013 Adobe
  ~ %%
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  ~ #L%
  --%>
<%@include file="/libs/foundation/global.jsp" %><%
%><%@page session="false" %><%

%><div class="page"
        id="scroll-top"
        role="main"
        ng-controller="MainCtrl"
        ng-init="app.resource = '${resourcePath}'; init();">

  <br/>
    <hr/>

    <p>The Component Admin feature allows the System administrators to create the simple components without 
        understanding the complete AEM implementation approach.</p>
   <p> This is a Three step wizard process. Please fill in the details as requested.

    <ul>
        <li>Step 1 : Define Component Properties</li>
         <li>Step 2 : Setup the HTML Rendering script</li>
         <li>Step 3 : Define Dialog attributes</li>
    </ul>
		<cq:include script="includes/form.jsp"/>
		<cq:include script="includes/form1.jsp"/>
		<cq:include script="includes/form2.jsp"/>
		<cq:include script="includes/form3.jsp"/>


</div>