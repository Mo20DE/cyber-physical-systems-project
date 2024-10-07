var SpeechRecognition = SpeechRecognition || webkitSpeechRecognition;
var SpeechGrammarList = SpeechGrammarList || webkitSpeechGrammarList;
var SpeechRecognitionEvent = SpeechRecognitionEvent || webkitSpeechRecognitionEvent;

//var aktivität = ['drehe', 'bewege', 'fahre', 'rotiere', 'lenke', ''];

//var musikstarwars = ['starwars', 'star wars', 'luke', 'darth vader'];

var resultPara = document.querySelector('.result');
var diagnosticPara = document.querySelector('.output');

var testBtn = document.querySelector('#speach');

function startSpeech() {
  var driving = ['fahre', 'bewege', 'gehe', 'drehe', 'bremse', 'bleib', 'rotiere', 'lenk'];
  var emotion = ['fühlst', 'geht', 'wie', 'ist'];
  var musik = ['spiele', 'sprich', 'musik', 'ertöne', 'singe', 'treller', 'lied'];
  var displ = ['zeig', 'spiegel', 'projeziere', 'übertrage', 'gib'];
  var alarm = ['alarm', 'wecke', 'erinner'];
	
	
  testBtn.disabled = true;
  testBtn.textContent = 'Test in progress';

  // To ensure case consistency while checking with the returned output text
  resultPara.textContent = '...';
  resultPara.style.background = 'rgba(0,0,0,0.2)';
  diagnosticPara.textContent = '...';

  var recognition = new SpeechRecognition();
  recognition.lang = 'de-DE';
  recognition.interimResults = false;
  recognition.maxAlternatives = 1;

  recognition.start();

  recognition.onresult = function(event) {
    // The SpeechRecognitionEvent results property returns a SpeechRecognitionResultList object
    // The SpeechRecognitionResultList object contains SpeechRecognitionResult objects.
    // It has a getter so it can be accessed like an array
    // The first [0] returns the SpeechRecognitionResult at position 0.
    // Each SpeechRecognitionResult object contains SpeechRecognitionAlternative objects that contain individual results.
    // These also have getters so they can be accessed like arrays.
    // The second [0] returns the SpeechRecognitionAlternative at position 0.
    // We then return the transcript property of the SpeechRecognitionAlternative object 
    var speechResult = event.results[0][0].transcript.toLowerCase();
    diagnosticPara.textContent = 'Gesprochen wurde: ' + speechResult + '.';
	
	console.log('Confidence: ' + event.results[0][0].confidence);
	
	for (i in driving){
		console.log(driving[i]);
		console.log(speechResult);
		let result = speechResult.indexOf(driving[i]);
		console.log('result:'+result);
		if(result != -1) {
		  let drtmp= speechResult.substring(result, speechResult.length);
		  console.log(drtmp);
		    
		  var direction = ['vorne', 'gerade', 'vorraus', 'links', 'backbord', 'länks', 'rechts', 'steuerbord', 'hinten', 'rückwerts', 'zurück'];
		  
		  for (j in direction){
			  let result2 = drtmp.indexOf(direction[j]);
			  console.log('direction:'+result2);
			  if(result2 != -1) {
				 resultPara.textContent = 'Fahre in Richtung: ';
		         resultPara.style.background = 'lime'; 
				  var ditmp= "";
				 if (j < 3){
					 ditmp= "Up";
					 //let response = fetch("/api/driving/1", { method: "POST" }); console.log(response);
				 }
				 else if(j < 6){
					 ditmp= "Down";
					 //let response = fetch("/api/driving/3", { method: "POST" }); console.log(response);
				 }
				 else if(j < 8){
					 ditmp= "Left";
					 //let response = fetch("/api/driving/4", { method: "POST" }); console.log(response);
				 }
				 else {
					 ditmp= "Right";
					 //let response = fetch("/api/driving/2", { method: "POST" }); console.log(response);
				 }
				 resultPara.textContent = resultPara.textContent + ditmp;
				 return;
			  }
		  }
		} else {
		  resultPara.textContent = 'Leider wurde nichts verstanden';
		  resultPara.style.background = 'red';
		}
	}
	
	for (i in emotion){
		console.log(emotion[i]);
		console.log(speechResult);
		let result = speechResult.indexOf(emotion[i]);
		console.log('result:'+result);
		if(result != -1) {
		  let moodtmp= speechResult.substring(result, speechResult.length);
		  console.log(moodtmp);
		  
		  var mood = ['erschöpft', 'erledigt', 'nicht gut', 'kalt', 'kühl', 'warm', 'heiß', 'kocht', 'müde', 'schlaf', 'schläf', 'wütend', 'sauer', 'böse', 'gut', 'glücklich', 'zufrieden'];
		  
		  for (j in mood){
			  let result2 = moodtmp.indexOf(mood[j]);
			  console.log('mood:'+result2);
			  if(result2 != -1) {
				 resultPara.textContent = 'Me ist: ';
		         resultPara.style.background = 'yellow'; 
				 var emtmp= "";
				 if (j < 3){
					 emtmp= "Müde";
					 //let response = fetch("/api/mood/5", { method: "POST" });
				 }
				 else if(j < 5){
					 emtmp= "Kalt";
					 //let response = fetch("/api/mood/1", { method: "POST" });
				 }
				 else if(j < 8){
					 emtmp= "Warm";
					 //let response = fetch("/api/mood/2", { method: "POST" });
				 }
				 else if(j < 11){
					 emtmp= "Warm";
					 //let response = fetch("/api/mood/3", { method: "POST" });
				 }
				 else if(j < 14){
					 emtmp= "Wütend";
					 //let response = fetch("/api/mood/4", { method: "POST" });
				 }
				 else {
					 emtmp= "Zufrieden";
					 //let response = fetch("/api/mood/0", { method: "POST" });
				 }
				 resultPara.textContent = resultPara.textContent + emtmp;
				 return;
			  }
		  }
		} else {
		  resultPara.textContent = 'Leider wurde nichts verstanden';
		  resultPara.style.background = 'red';
		}
	}
	
	for (i in musik){
		console.log(musik[i]);
		console.log(speechResult);
		let result = speechResult.indexOf(musik[i]);
		console.log('result:'+result);
		if(result != -1) {
		  let mutmp= speechResult.substring(result, speechResult.length);
		  console.log(mutmp);
		  
		  var songs = ['wars', 'star', 'vader', 'luke', 'skywalker', 'pink', 'panther'];	  
		  
		   for (j in songs){
			  let result2 = mutmp.indexOf(songs[j]);
			  console.log(result2);
			  if(result2 != -1) {
				 resultPara.textContent = 'Spiele das Lied: ';
			     resultPara.style.background = 'lightblue'; 
				 var sotmp= "";
				 console.log("Wort:"+j+ "_" + songs[j]);
				 if (j < 5){
					 sotmp= "Star Wars";
					 //let response = fetch("/api/event/6", { method: "POST" });
				 }
				 else { 
					 sotmp= "Pink Panther";
					 //let response = fetch("/api/event/5", { method: "POST" });
				 }
				 resultPara.textContent = resultPara.textContent + sotmp;
				 return;
			  }
		  }
		} else {
		  resultPara.textContent = 'Leider wurde nichts verstanden';
		  resultPara.style.background = 'red';
		}
	}
	
	for (i in displ){
		console.log(displ[i]);
		console.log(speechResult);
		let result = speechResult.indexOf(displ[i]);
		console.log('result:'+result);
		if(result != -1) {
		  let dispimtmp= speechResult.substring(result, speechResult.length);
		  console.log(dispimtmp);
		  
		  var dispim = ['monopoly', 'mann', 'anzug', 'panda', 'bambus', 'asia', 'alpaka', 'alpaca', 'kamel', 'wüste'];
		  
		  for (j in dispim){
			  let result2 = dispimtmp.indexOf(dispim[j]);
			  console.log('dispim:'+result2);
			  if(result2 != -1) {
				 resultPara.textContent = 'Me zeigt: ';
		         resultPara.style.background = 'orange'; 
				 var distmp= "";
				 if (j < 3){
					 distmp= "Monopoly-Mann";
					 //let response = fetch("/api/event/2", { method: "POST" })
				 }
				 else if(j < 6){
					 distmp= "Panda";
					 //let response = fetch("/api/event/4", { method: "POST" })
				 }
				 else {
					 distmp= "Alpaca";
					 //let response = fetch("/api/event/3", { method: "POST" })
				 }
				 resultPara.textContent = resultPara.textContent + distmp;
				 return;
			  }
		  }
		} else {
		  resultPara.textContent = 'Leider wurde nichts verstanden';
		  resultPara.style.background = 'red';
		}
	}
	
	for (i in alarm){
		console.log(alarm[i]);
		console.log(speechResult);
		let result = speechResult.indexOf(alarm[i]);
		console.log('result:'+result);
		if(result != -1) {
		  let timetmp= speechResult.substring(result, speechResult.length);
		  console.log(timetmp);
		  
		  var altime = ['in', 'nach', 'gegen', 'um'];
		  
		  for (j in altime){
			  let result2 = timetmp.indexOf(altime[j]);
			  console.log(result2);
			  timetmp= timetmp.substring(result2+altime[j].length, timetmp.length);
			  console.log(timetmp);
			  if(result2 != -1) {
				resultPara.textContent = 'Den Wecker gestellt um: ';
				resultPara.style.background = 'green';
				 
				let timtmp = parseInt(timetmp);
				console.log(timtmp);
				let result3= timetmp.indexOf('und');
				if (result3!= -1){
					timetmp= timetmp.substring(result3+3, timetmp.length);
					console.log(timetmp);
					let timtmp2= parseInt(timetmp);
					if(timtmp2!=NaN){
						let sendtime= timtmp+":"+ timtmp2;
						resultPara.textContent = resultPara.textContent + sendtime;
						//let response = fetch("/api/alarm/set/" + sendtime, { method: "POST" });
						return;
					}
				}
				resultPara.textContent = resultPara.textContent + timtmp;
				//let response = fetch("/api/alarm/set/" + timtmp, { method: "POST" });
				return;
			  }
		  }
		} else {
		  resultPara.textContent = 'Leider wurde nichts verstanden';
		  resultPara.style.background = 'red';
		}
	}
  }

  recognition.onspeechend = function() {
    recognition.stop();
    testBtn.disabled = false;
    testBtn.textContent = 'new Sprachtest';
  }

  recognition.onerror = function(event) {
    testBtn.disabled = false;
    testBtn.textContent = 'new Sprachtest';
    diagnosticPara.textContent = 'Error occurred in recognition: ' + event.error;
  }
  
  recognition.onaudiostart = function(event) {
      //Fired when the user agent has started to capture audio.
      console.log('SpeechRecognition.onaudiostart');
  }
  
  recognition.onaudioend = function(event) {
      //Fired when the user agent has finished capturing audio.
      console.log('SpeechRecognition.onaudioend');
  }
  
  recognition.onend = function(event) {
      //Fired when the speech recognition service has disconnected.
      console.log('SpeechRecognition.onend');
  }
  
  recognition.onnomatch = function(event) {
      //Fired when the speech recognition service returns a final result with no significant recognition. This may involve some degree of recognition, which doesn't meet or exceed the confidence threshold.
      console.log('SpeechRecognition.onnomatch');
  }
  
  recognition.onsoundstart = function(event) {
      //Fired when any sound — recognisable speech or not — has been detected.
      console.log('SpeechRecognition.onsoundstart');
  }
  
  recognition.onsoundend = function(event) {
      //Fired when any sound — recognisable speech or not — has stopped being detected.
      console.log('SpeechRecognition.onsoundend');
  }
  
  recognition.onspeechstart = function (event) {
      //Fired when sound that is recognised by the speech recognition service as speech has been detected.
      console.log('SpeechRecognition.onspeechstart');
  }
  recognition.onstart = function(event) {
      //Fired when the speech recognition service has begun listening to incoming audio with intent to recognize grammars associated with the current SpeechRecognition.
      console.log('SpeechRecognition.onstart');
  }
}

testBtn.addEventListener('click', startSpeech);
