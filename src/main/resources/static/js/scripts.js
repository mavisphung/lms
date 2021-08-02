		var minutes = 60.1;

		function elapse() {
		    minutes -= 1 / 60;
		    document.getElementById('time').innerText = minuteConvert(minutes)
		}


		function minuteConvert(d) {
		    d = Number(d);
		    var h = Math.floor(d / 60);
		    var m = Math.floor(d % 60);
		    var s = Math.floor(((d % 60) % 1) * 60);

		    var hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hours, ") : "";
		    var mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " minutes, ") : "";
		    var sDisplay = s > 0 ? s + (s == 1 ? " second" : " seconds") : "";
		    return hDisplay + mDisplay + sDisplay;
		}

		setInterval(elapse, 1000);