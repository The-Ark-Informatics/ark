<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : patient_admissions.xsl
    Created on : May 13, 2004, 3:12 PM
    Author     : renny
    Description:Adding admissions of a patient.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../../common/common_btn_name.xsl"/>
  <xsl:output method="html" indent="no" />
   <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
   
<!-- Start Secure Download Implementation -->

<xsl:template match='sdiagnosis_picker'>
<script language="javascript">
        
/*--------------------------------------------------|

| dTree 2.05 | www.destroydrop.com/javascript/tree/ |

|---------------------------------------------------|

| Copyright (c) 2002-2003 Geir Landr                |

|                                                   |

| This script can be used freely as long as all     |

| copyright messages are intact.                    |

|                                                   |

| Updated: 17.04.2003                               |

|--------------------------------------------------*/



// Node object

function Node(id, pid, name, code, selected, url, title, target, icon, iconOpen, open) {

	this.id = id;

	this.pid = pid;
        
        this.selected = selected;
        
        this.code = code;
        
        this.name = name;

	this.url = url;

	this.title = title;

	this.target = target;

	this.icon = icon;

	this.iconOpen = iconOpen;

	this._io = open || false;

	this._is = false;

	this._ls = false;

	this._hc = false;

	this._ai = 0;

	this._p;

};



// Tree object

function dTree(objName) {

	this.config = {

		target					: null,

		folderLinks			: true,

		useSelection		: true,

		useCookies			: false,

		useLines				: true,

		useIcons				: true,

		useStatusText		: false,

		closeSameLevel	: false,

		inOrder					: false

	}

	this.icon = {

		root				: 'media/neuragenix/icons/dtree/folderopen.gif',

		folder			: 'media/neuragenix/icons/dtree/folder.gif',

		folderOpen	: 'media/neuragenix/icons/dtree/folderopen.gif',

		node				: 'media/neuragenix/icons/dtree/page.gif',

		empty				: 'media/neuragenix/icons/dtree/empty.gif',

		line				: 'media/neuragenix/icons/dtree/line.gif',

		join				: 'media/neuragenix/icons/dtree/join.gif',

		joinBottom	: 'media/neuragenix/icons/dtree/joinbottom.gif',

		plus				: 'media/neuragenix/icons/dtree/plus.gif',

		plusBottom	: 'media/neuragenix/icons/dtree/plusbottom.gif',

		minus				: 'media/neuragenix/icons/dtree/minus.gif',

		minusBottom	: 'media/neuragenix/icons/dtree/minusbottom.gif',

		nlPlus			: 'media/neuragenix/icons/dtree/nolines_plus.gif',

		nlMinus			: 'media/neuragenix/icons/dtree/nolines_minus.gif'

	};

	this.obj = objName;

	this.aNodes = [];

	this.aIndent = [];

	this.root = new Node(-1);

	this.selectedNode = null;

	this.selectedFound = false;

	this.completed = false;

};



// Adds a new node to the node array

dTree.prototype.add = function(id, pid, name, code, selected, url, title, target, icon, iconOpen, open) {

	this.aNodes[this.aNodes.length] = new Node(id, pid, name, code, selected, url, title, target, icon, iconOpen, open);

};



// Open/close all nodes

dTree.prototype.openAll = function() {

	this.oAll(true);

};

dTree.prototype.closeAll = function() {

	this.oAll(false);

};



// Outputs the tree to the page

dTree.prototype.toString = function() {

	var str = '&lt;div class="dtree"&gt;\n';

	if (document.getElementById) {

		if (this.config.useCookies) this.selectedNode = this.getSelected();

		str += this.addNode(this.root);

	} else str += 'Browser not supported.';

	str += '&lt;/div&gt;';

	if (!this.selectedFound) this.selectedNode = null;

	this.completed = true;

	return str;

};



// Creates the tree structure

dTree.prototype.addNode = function(pNode) {

	var str = '';

	var n=0;

	if (this.config.inOrder) n = pNode._ai;

	for (n; n&lt;this.aNodes.length; n++) {

		if (this.aNodes[n].pid == pNode.id) {

			var cn = this.aNodes[n];

			cn._p = pNode;

			cn._ai = n;

			this.setCS(cn);

			if (!cn.target &amp;&amp; this.config.target) cn.target = this.config.target;

			if (cn._hc &amp;&amp; !cn._io &amp;&amp; this.config.useCookies) cn._io = this.isOpen(cn.id);

			if (!this.config.folderLinks &amp;&amp; cn._hc) cn.url = null;

			if (this.config.useSelection &amp;&amp; cn.id == this.selectedNode &amp;&amp; !this.selectedFound) {

					cn._is = true;

					this.selectedNode = n;

					this.selectedFound = true;

			}

			str += this.node(cn, n);

			if (cn._ls) break;

		}

	}

	return str;

};



// Creates the node icon, url and text

dTree.prototype.node = function(node, nodeId) {
        
	var str = '&lt;div class="dTreeNode"&gt;';
        var a = node.pid;
        var b = this.root.id;
        if( a != b ){
        
            str += '&lt;input id="DIAGNOSIS" type="checkbox" name="diagnosis_' + node.id + '" value="true" ';
            if( node.selected )
                str +='checked="true"';
            str +=' onclick="javascript:' + this.obj + '.select(' + node.id + ', this.checked )" ';
            str += '/&gt;';
            
        }
                
        str += this.indent(node, nodeId);

	if (this.config.useIcons) {

		if (!node.icon) node.icon = (this.root.id == node.pid) ? this.icon.root : ((node._hc) ? this.icon.folder : this.icon.node);

		if (!node.iconOpen) node.iconOpen = (node._hc) ? this.icon.folderOpen : this.icon.node;

		if (this.root.id == node.pid) {

			node.icon = this.icon.root;

			node.iconOpen = this.icon.root;

		}

		str += '&lt;img id="i' + this.obj + nodeId + '" src="' + ((node._io) ? node.iconOpen : node.icon) + '" alt="" /&gt;';

	}
//        str += '&lt;div id="code_"' + nodeID + '&gt;' + node.code + ': ' + '&lt;/div&gt;';

        if( a == b ){
            str += "&lt;b&gt;";
        }

	if (node.url) {

		str += '&lt;a id="s' + this.obj + nodeId + '" class="' + ((this.config.useSelection) ? ((node._is ? 'nodeSel' : 'node')) : 'node') + '" href="' + node.url + '"';

		if (node.title) str += ' title="' + node.title + '"';

		if (node.target) str += ' target="' + node.target + '"';

		if (this.config.useStatusText) str += ' onmouseover="window.status=\'' + node.name + '\';return true;" onmouseout="window.status=\'\';return true;" ';

		if (this.config.useSelection &amp;&amp; ((node._hc &amp;&amp; this.config.folderLinks) || !node._hc))

			str += ' onclick="javascript: ' + this.obj + '.s(' + nodeId + ');"';

		str += '&gt;';

	}
        else if ((!this.config.folderLinks || !node.url) &amp;&amp; node._hc &amp;&amp; node.pid != this.root.id){

		str += '&lt;a href="javascript: ' + this.obj + '.o(' + nodeId + ');" class="node"&gt;';

	}
        
	str += node.name;

	if (node.url || ((!this.config.folderLinks || !node.url) &amp;&amp; node._hc)){

		str += '&lt;/a&gt;';
	
	}
        if( a == b ){
            str += "&lt;/b&gt;";
        }


	str += '&lt;/div&gt;';

	if (node._hc) {

		//str += '&lt;div id="d' + this.obj + nodeId + '" class="clip" style="display:' + ((this.root.id == node.pid || node._io) ? 'block' : 'none') + ';"&gt;';
                str += '&lt;div id="d' + this.obj + nodeId + '" class="clip" style="display:' + ((this.root.id == node.id || node._io) ? 'block' : 'none') + ';"&gt;';

		str += this.addNode(node);

		str += '&lt;/div&gt;';

	}

	this.aIndent.pop();

	return str;

};



// Adds the empty and line icons

dTree.prototype.indent = function(node, nodeId) {

	var str = '';

	//if (this.root.id != node.pid) {

		for (var n=0; n&lt;this.aIndent.length; n++)

			str += '&lt;img src="' + ( (this.aIndent[n] == 1 &amp;&amp; this.config.useLines) ? this.icon.line : this.icon.empty ) + '" alt="" /&gt;';

		(node._ls) ? this.aIndent.push(0) : this.aIndent.push(1);

		if (node._hc) {

			str += '&lt;a href="javascript: ' + this.obj + '.o(' + nodeId + ');"&gt;&lt;img id="j' + this.obj + nodeId + '" src="';

			if (!this.config.useLines) str += (node._io) ? this.icon.nlMinus : this.icon.nlPlus;

			else str += ( (node._io) ? ((node._ls &amp;&amp; this.config.useLines) ? this.icon.minusBottom : this.icon.minus) : ((node._ls &amp;&amp; this.config.useLines) ? this.icon.plusBottom : this.icon.plus ) );

			str += '" alt="" border="0" /&gt;&lt;/a&gt;';

		} else str += '&lt;img src="' + ( (this.config.useLines) ? ((node._ls) ? this.icon.joinBottom : this.icon.join ) : this.icon.empty) + '" alt="" /&gt;';

	//}
        
	return str;

};



// Checks if a node has any children and if it is the last sibling

dTree.prototype.setCS = function(node) {

	var lastId;

	for (var n=0; n&lt;this.aNodes.length; n++) {

		if (this.aNodes[n].pid == node.id) node._hc = true;

		if (this.aNodes[n].pid == node.pid) lastId = this.aNodes[n].id;

	}

	if (lastId==node.id) node._ls = true;

};



// Returns the selected node

dTree.prototype.getSelected = function() {

	var sn = this.getCookie('cs' + this.obj);

	return (sn) ? sn : null;

};



// Highlights the selected node

dTree.prototype.s = function(id) {

	if (!this.config.useSelection) return;

	var cn = this.aNodes[id];

	if (cn._hc &amp;&amp; !this.config.folderLinks) return;

	if (this.selectedNode != id) {

		if (this.selectedNode || this.selectedNode==0) {

			eOld = document.getElementById("s" + this.obj + this.selectedNode);

			eOld.className = "node";

		}

		eNew = document.getElementById("s" + this.obj + id);

		eNew.className = "nodeSel";

		this.selectedNode = id;

		if (this.config.useCookies) this.setCookie('cs' + this.obj, cn.id);

	}

};



// Toggle Open or close

dTree.prototype.o = function(id) {

	var cn = this.aNodes[id];

	this.nodeStatus(!cn._io, id, cn._ls);

	cn._io = !cn._io;

	if (this.config.closeSameLevel) this.closeLevel(cn);

	if (this.config.useCookies) this.updateCookie();

};



// Open or close all nodes

dTree.prototype.oAll = function(status) {

	for (var n=0; n&lt;this.aNodes.length; n++) {

		if (this.aNodes[n]._hc &amp;&amp; this.aNodes[n].pid != this.root.id) {

			this.nodeStatus(status, n, this.aNodes[n]._ls)

			this.aNodes[n]._io = status;

		}

	}

	if (this.config.useCookies) this.updateCookie();

};



// Opens the tree to a specific node

dTree.prototype.openTo = function(nId, bSelect, bFirst) {

	if (!bFirst) {

		for (var n=0; n&lt;this.aNodes.length; n++) {

			if (this.aNodes[n].id == nId) {

				nId=n;

				break;

			}

		}

	}

	var cn=this.aNodes[nId];

	if (cn.pid==this.root.id || !cn._p) return;

	cn._io = true;

	cn._is = bSelect;

	if (this.completed &amp;&amp; cn._hc) this.nodeStatus(true, cn._ai, cn._ls);

	if (this.completed &amp;&amp; bSelect) this.s(cn._ai);

	else if (bSelect) this._sn=cn._ai;

	this.openTo(cn._p._ai, false, true);

};



// Closes all nodes on the same level as certain node

dTree.prototype.closeLevel = function(node) {

	for (var n=0; n&lt;this.aNodes.length; n++) {

		if (this.aNodes[n].pid == node.pid &amp;&amp; this.aNodes[n].id != node.id &amp;&amp; this.aNodes[n]._hc) {

			this.nodeStatus(false, n, this.aNodes[n]._ls);

			this.aNodes[n]._io = false;

			this.closeAllChildren(this.aNodes[n]);

		}

	}

}



// Closes all children of a node

dTree.prototype.closeAllChildren = function(node) {

	for (var n=0; n&lt;this.aNodes.length; n++) {

		if (this.aNodes[n].pid == node.id &amp;&amp; this.aNodes[n]._hc) {

			if (this.aNodes[n]._io) this.nodeStatus(false, n, this.aNodes[n]._ls);

			this.aNodes[n]._io = false;

			this.closeAllChildren(this.aNodes[n]);		

		}

	}

}



// Change the status of a node(open or closed)

dTree.prototype.nodeStatus = function(status, id, bottom) {

	eDiv	= document.getElementById('d' + this.obj + id);

	eJoin	= document.getElementById('j' + this.obj + id);

	if (this.config.useIcons) {

		eIcon	= document.getElementById('i' + this.obj + id);

		eIcon.src = (status) ? this.aNodes[id].iconOpen : this.aNodes[id].icon;

	}

	eJoin.src = (this.config.useLines)?

	((status)?((bottom)?this.icon.minusBottom:this.icon.minus):((bottom)?this.icon.plusBottom:this.icon.plus)):

	((status)?this.icon.nlMinus:this.icon.nlPlus);

	eDiv.style.display = (status) ? 'block': 'none';

};

dTree.prototype.toggleCode = function( status ) {

	for (var n=0; n&lt;this.aNodes.length; n++) {

		if ( this.aNodes[n]._hc ) {

			this.codeStatus(status);

			
		}

	}

}

// Change the status of a node(open or closed)

dTree.prototype.codeStatus = function(status, id) {

	eDiv	= document.getElementById('code_' + id);

	eDiv.style.display = (status) ? 'block': 'none';

};




// [Cookie] Clears a cookie

dTree.prototype.clearCookie = function() {

	var now = new Date();

	var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);

	this.setCookie('co'+this.obj, 'cookieValue', yesterday);

	this.setCookie('cs'+this.obj, 'cookieValue', yesterday);

};



// [Cookie] Sets value in a cookie

dTree.prototype.setCookie = function(cookieName, cookieValue, expires, path, domain, secure) {

	document.cookie =

		escape(cookieName) + '=' + escape(cookieValue)

		+ (expires ? '; expires=' + expires.toGMTString() : '')

		+ (path ? '; path=' + path : '')

		+ (domain ? '; domain=' + domain : '')

		+ (secure ? '; secure' : '');

};



// [Cookie] Gets a value from a cookie

dTree.prototype.getCookie = function(cookieName) {

	var cookieValue = '';

	var posName = document.cookie.indexOf(escape(cookieName) + '=');

	if (posName != -1) {

		var posValue = posName + (escape(cookieName) + '=').length;

		var endPos = document.cookie.indexOf(';', posValue);

		if (endPos != -1) cookieValue = unescape(document.cookie.substring(posValue, endPos));

		else cookieValue = unescape(document.cookie.substring(posValue));

	}

	return (cookieValue);

};



// [Cookie] Returns ids of open nodes as a string

dTree.prototype.updateCookie = function() {

	var str = '';

	for (var n=0; n&lt;this.aNodes.length; n++) {

		if (this.aNodes[n]._io &amp;&amp; this.aNodes[n].pid != this.root.id) {

			if (str) str += '.';

			str += this.aNodes[n].id;

		}

	}

	this.setCookie('co' + this.obj, str);

};



// [Cookie] Checks if a node id is in a cookie

dTree.prototype.isOpen = function(id) {

	var aOpen = this.getCookie('co' + this.obj).split('.');

	for (var n=0; n&lt;aOpen.length; n++)

		if (aOpen[n] == id) return true;

	return false;

};

// [Cookie] Checks if a node id is in a cookie

dTree.prototype.select = function(id, status) {

        var n = 0;
        while( (this.aNodes[n].id != id) ){   
                n++;
        }

        if( status ){
            
            if( (this.aNodes[n].pid != this.root.id) ){
                
                eval( 'document.diagnosisForm.diagnosis_' + this.aNodes[n].id + '.checked = true');
                this.select( this.aNodes[n].pid, true );
                
                
            }
        }
        
};


// If Push and pop is not implemented by the browser

if (!Array.prototype.push) {

	Array.prototype.push = function array_push() {

		for(var i=0;i&lt;arguments.length;i++)

			this[this.length]=arguments[i];

		return this.length;

	}

};

if (!Array.prototype.pop) {

	Array.prototype.pop = function array_pop() {

		lastElement = this[this.length-1];

		this.length = Math.max(this.length-1,0);

		return lastElement;

	}

};

         
// Long's script         
/*
function submitSelected(){
    document.diagnosisForm.action = document.diagnosisForm.action + '&amp;diagnosisSelected=true';
    document.diagnosisForm.submit();

}
*/

function submitSelected( currentField ){
    
    var diagnosisCount = parseInt( document.getElementById( 'diagnosisCount' ).getAttribute('value') ) ;
    
    var strValues = '';
    for( var i = 1; i &lt;= diagnosisCount; i++ ){

        var checkObj = eval( 'document.diagnosisForm.diagnosis_' + i );
        
        
        if( checkObj ){

            if( checkObj.checked ){
                strValues += document.getElementById( 'diagnosis_' + i ).getAttribute( 'codeValue' ) + ": " +
                             document.getElementById( 'diagnosis_' + i ).getAttribute( 'nameValue' ) + "\n" ;
            }else{
                
            }

        }else{
            
        }
    }
    
    var currentObj = eval( "document.diagnosisForm." + currentField );
    currentObj.value = strValues;
    document.diagnosisForm.submit();

}


function cancelSelected(){
    
    document.diagnosisForm.submit();

}



        </script>
        
        <xsl:variable name="currentField"><xsl:value-of select="currentField"/></xsl:variable>
        <table width="100%">
               <tr>
			<td class="uportal-channel-subtitle">
			Choose diagnosis<br/><hr/>
			</td> 
		</tr> 
                <tr>
                    <td class="uportal-label">
                    
                    <!-- start my massive script -->
                    <xsl:variable name="diagnosisCount"><xsl:value-of select="diagnosisCount"/></xsl:variable>
                    
                    
                    <span id="diagnosisCount" value="{$diagnosisCount}"/>
                    <xsl:for-each select="diagnosis">
                        
                        <xsl:variable name="SDIAGNOSIS_intDiagnosisKey"><xsl:value-of select="SDIAGNOSIS_intDiagnosisKey"/></xsl:variable>
                        <xsl:variable name="SDIAGNOSIS_strDiagnosisName"><xsl:value-of select="SDIAGNOSIS_strDiagnosisName"/></xsl:variable>
                        <xsl:variable name="SDIAGNOSIS_strEPCCCode"><xsl:value-of select="SDIAGNOSIS_strEPCCCode"/></xsl:variable>
                        <xsl:variable name="SDIAGNOSIS_intParentKey"><xsl:value-of select="SDIAGNOSIS_intParentKey"/></xsl:variable>
                        <xsl:variable name="DIAGNOSIS_ADMISSIONS_intDiagnosisAdmissionsKey"><xsl:value-of select="DIAGNOSIS_ADMISSIONS_intDiagnosisAdmissionsKey"/></xsl:variable>
                    
                        <span id="diagnosis_{$SDIAGNOSIS_intDiagnosisKey}" keyValue="{$SDIAGNOSIS_intDiagnosisKey}"
                            nameValue="{$SDIAGNOSIS_strDiagnosisName}" codeValue="{$SDIAGNOSIS_strEPCCCode}" 
                            parentValue="{$SDIAGNOSIS_intParentKey}">
                            <xsl:if test="string-length( $DIAGNOSIS_ADMISSIONS_intDiagnosisAdmissionsKey ) > 0">
                                <xsl:attribute name="selectedValue">true</xsl:attribute>
                            </xsl:if>
                        </span>
                    </xsl:for-each>
                    <form name="diagnosisForm" action="{$baseActionURL}?uP_root=root&amp;" method="post">
                    <!--<div align="right"> <input type="checkbox" name="showCode" onclick="javascript:d.toggleCode( this.checked )">Show EPCC code</input>
                    -->
                    
                        <div class="dtree">
                        <script language="javascript">
                        
                        var diagnosisCount = parseInt( document.getElementById( 'diagnosisCount' ).getAttribute('value') ) ;
                        
                        var d = new dTree('d');

                        for( var i = 1; i &lt;= diagnosisCount; i++ ){

                            if( document.getElementById( 'diagnosis_' + i ) ){

                                d.add(document.getElementById( 'diagnosis_' + i ).getAttribute( 'keyValue') ,
                                document.getElementById( 'diagnosis_' + i ).getAttribute( 'parentValue'),
                                document.getElementById( 'diagnosis_' + i ).getAttribute( 'nameValue'),
                                document.getElementById( 'diagnosis_' + i ).getAttribute( 'codeValue'),
                                document.getElementById( 'diagnosis_' + i ).getAttribute( 'selectedValue'),'');

                            }
                        }

      
                        document.write(d);
                      

                        </script>
                        </div>
                        <xsl:for-each select="parameter">
                            <xsl:variable name="name"><xsl:value-of select="name"/></xsl:variable>
                            <xsl:variable name="value"><xsl:value-of select="value"/></xsl:variable>
                            <input type="hidden" name="{$name}" value="{$value}"/>
                        </xsl:for-each>
                        
                        <input type="hidden" name="formLink" value="true"/>
                    </form>
                    
                    <!--{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}-->
                    </td>
                </tr>
	</table>
        <table width="100%">
           
            <tr>
                <td align="left">
                   
                        <input type="button"  name="cancelSelected" value="Cancel" onclick="javascript:cancelSelected()" class="uportal-button" />
                   
                </td>
                <td align="right">
                   <form action="{$baseActionURL}?uP_root=root&amp;" method="POST">
                        
                        <input type="button"  name="diagnosisSelected" value="Submit" onclick="javascript:submitSelected('{$currentField}')" class="uportal-button" />
                    </form>
                </td>
            </tr>
        </table>
        
        <!--table width="100%">
            <tr>
                <td width="100%">
                    <hr/>
                </td>
            </tr>
            <tr>
                <td>
                  <input type="button" name="back" value="{$backBtnLabel}" tabindex="26" class="uportal-button"
                        onclick="javascript:window.location='{$baseActionURL}?{$action}&amp;back=true&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}'" />
                  
                </td>
            </tr>
        </table-->
    </xsl:template>

    <xsl:template match="strTitle"/>
    <xsl:template match="isFromWorkspace"/>
    <xsl:template match="strBackButton" />
    <xsl:template match="intStudyID"/>
    <xsl:template match="SMARTFORMPARTICIPANTS_strSmartformStatus"/>
    
</xsl:stylesheet>