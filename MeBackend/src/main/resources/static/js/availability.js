const availability = document.getElementById("availability");
checkAvailability();

setInterval(checkAvailability,5000);
async function checkAvailability(){
    fetch("/api/available", { method: "POST" }).then(response => response.text()).then(body => {
        console.log(body);
        setAvailable(body);
    }).catch((error) => {setAvailable("false");});
}
function setAvailable(b) {
    if (b == "true") {
        availability.setAttribute("style", "color:green");
        availability.innerHTML = "Me ist verfügbar";
    } else {
        availability.setAttribute("style", "color:red");
        availability.innerHTML = "Me ist abwesend";
    }
}