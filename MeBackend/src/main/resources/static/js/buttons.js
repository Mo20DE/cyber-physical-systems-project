//Driving
document.getElementById("Up").addEventListener("click", async function () {
    console.log("clicked Up");
    let response = await fetch("/api/driving/1", { method: "POST" })
})
document.getElementById("Down").addEventListener("click", async function () {
    console.log("clicked Down");
    let response = await fetch("/api/driving/2", { method: "POST" })
})
document.getElementById("Left").addEventListener("click", async function () {
    console.log("clicked Left");
    let response = await fetch("/api/driving/3", { method: "POST" })
})
document.getElementById("Right").addEventListener("click", async function () {
    console.log("clicked Right");
    let response = await fetch("/api/driving/4", { method: "POST" })
})

//Events
document.getElementById("AlarmStarten").addEventListener("click", async function () {
    console.log("alarm started");
    let time = document.getElementById("appt").value;
    time = time.replace(":", "-");
    console.log(time);
    let response = await fetch("/api/alarm/set/" + time, { method: "POST" })
})
document.getElementById("DisplayMonopolyMan").addEventListener("click", async function () {
    console.log("displaying Monopoly-Man");
    let response = await fetch("/api/event/2", { method: "POST" })
})
document.getElementById("DisplayAlpaca").addEventListener("click", async function () {
    console.log("displaying Alpaca");
    let response = await fetch("/api/event/3", { method: "POST" })
})
document.getElementById("DisplayPanda").addEventListener("click", async function () {
    console.log("displaying Panda");
    let response = await fetch("/api/event/4", { method: "POST" })
})
document.getElementById("PlayPinkPanther").addEventListener("click", async function () {
    console.log("playing Pink Panther");
    let response = await fetch("/api/event/5", { method: "POST" })
})
document.getElementById("PlayStarWars").addEventListener("click", async function () {
    console.log("playing Star Wars");
    let response = await fetch("/api/event/6", { method: "POST" })
})

// Drop Down
document.getElementById("mood").addEventListener("change", async function (){
    let mood = document.getElementById("mood").value;
    console.log("Setting mood: " + mood);
    let response = await fetch("/api/mood/" + mood, { method: "POST" })
})