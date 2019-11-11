<SCRIPT LANGUAGE="JavaScript1.2">
<!--
/**************************************************************************

	Script om de CSS layer zichtbaar te maken en te verbergen.
	Aparte code voor IE4 en NS4.

	Erik van Zijst - erik@prutser.cx

**************************************************************************/

NS4 = (document.layers) ? 1 : 0;
IE4 = (document.all) ? 1 : 0;

function toggle_on(layer)
{
	if(NS4)
	{
//		document.layers[layer].left = document.width - 480;
//		document.layers[layer].top = document.height - 310;
		document.layers[layer].visibility = "show";
//		document.loginForm.login.focus();
	}
	else if(IE4)
	{
//		alert('anchor location: ' + document.all.leeg.style.posTop + 'pixels.');
//		document.all[layer].style.pixelLeft = document.body.clientWidth  - 480;
//		document.all[layer].style.pixelTop = document.body.clientHeight;
		document.all[layer].style.visibility = "visible";
	}
	// else jammer. geen css support, geen popup window.
}

function toggle_off(layer)
{
	if(NS4)
		document.layers[layer].visibility = "hide";
	else if(IE4)
		document.all[layer].style.visibility = "HIDDEN";
	// else jammer. geen css support, geen popup window.
}

//-->
</SCRIPT>
