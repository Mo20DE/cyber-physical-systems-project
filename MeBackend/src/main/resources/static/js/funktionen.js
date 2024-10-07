var s = 10;
var etwas = document.querySelector('#sensorDaten');
var rowLength = etwas.rows.length;
var temperatur = [];
var luft = [];
var abstand = [];
var uv = [];
var vis = [];
var ir = [];
var zeit = []
var tabelleG = false;
for (i = 0; i < rowLength; i++) {

    var oCells = etwas.rows.item(i).cells;
    //console.log(oCells);
    var cellLength = oCells.length;
    //console.log(cellLength);

    for (var j = 0; j < cellLength; j++) {

        if (j == 0 && i != 0) {
            neuer = oCells.item(j).innerHTML;
            //console.log("Temperatur Daten: "+oCells.item(j).innerHTML)
            zeit.push(neuer);
        }

        else if (j == 1 && i != 0) {
            neuer = oCells.item(j).innerHTML;
            //console.log("Temperatur Daten: "+oCells.item(j).innerHTML)
            temperatur.push(neuer);
        }
        else if (j == 2 && i != 0) {
            neuer = oCells.item(j).innerHTML;
            //console.log("Temperatur Daten: "+oCells.item(j).innerHTML)
            luft.push(neuer);
        }
        else if (j == 3 && i != 0) {
            neuer = oCells.item(j).innerHTML;
            //console.log("Temperatur Daten: "+oCells.item(j).innerHTML)
            abstand.push(neuer);
        }
        else if (j == 4 && i != 0) {
            neuer = oCells.item(j).innerHTML;
            //console.log("Temperatur Daten: "+oCells.item(j).innerHTML)
            uv.push(neuer);
        }
        else if (j == 5 && i != 0) {
            neuer = oCells.item(j).innerHTML;
            //console.log("Temperatur Daten: "+oCells.item(j).innerHTML)
            vis.push(neuer);
        }
        else if (j == 6 && i != 0) {
            neuer = oCells.item(j).innerHTML;
            //console.log("Temperatur Daten: "+oCells.item(j).innerHTML)
            ir.push(neuer);
        }
    }
}
/*console.log(zeit);
console.log(temperatur);
console.log(luft);
console.log(abstand);
console.log(uv);
console.log(vis);
console.log(ir);*/

var data2 = ["Temperatur", "Luft", "Abstand", "UV", "VIS", "IR"];

(function ($) {
    $.fn.extend({
        rotaterator: function (options) {

            var defaults = {
                fadeSpeed: 500,
                pauseSpeed: 100,
                child: null
            };

            var options = $.extend(defaults, options);

            return this.each(function () {
                var o = options;
                var obj = $(this);
                var items = $(obj.children(), obj);
                items.each(function () {
                    $(this).hide();
                })
                if (!o.child) {
                    var next = $(obj).children(':first');
                } else {
                    var next = o.child;
                }
                $(next).fadeIn(o.fadeSpeed, function () {
                    $(next).delay(o.pauseSpeed).fadeOut(o.fadeSpeed, function () {
                        var next = $(this).next();
                        if (next.length == 0) {
                            next = $(obj).children(':first');
                        }
                        $(obj).rotaterator({
                            child: next,
                            fadeSpeed: o.fadeSpeed,
                            pauseSpeed: o.pauseSpeed
                        });
                    })
                });
            });
        }
    });
})(jQuery);

$(document).ready(function () {

    var video = document.getElementById("tm-welcome-video");

    video.onloadeddata = function() {
        $('#tm-video-text-overlay').removeClass('d-none');
        $('#tm-video-loader').addClass('d-none');

        $('#rotate').rotaterator({
            fadeSpeed: 1000,
            pauseSpeed: 300
        });
    }
    document.querySelector('.tm-current-year').textContent = new Date().getFullYear();
});

(function ($) {
    $.fn.extend({
        rotaterator: function (options) {

            var defaults = {
                fadeSpeed: 500,
                pauseSpeed: 100,
                child: null
            };

            var options = $.extend(defaults, options);

            return this.each(function () {
                var o = options;
                var obj = $(this);
                var items = $(obj.children(), obj);
                items.each(function () {
                    $(this).hide();
                })
                if (!o.child) {
                    var next = $(obj).children(':first');
                } else {
                    var next = o.child;
                }
                $(next).fadeIn(o.fadeSpeed, function () {
                    $(next).delay(o.pauseSpeed).fadeOut(o.fadeSpeed, function () {
                        var next = $(this).next();
                        if (next.length == 0) {
                            next = $(obj).children(':first');
                        }
                        $(obj).rotaterator({
                            child: next,
                            fadeSpeed: o.fadeSpeed,
                            pauseSpeed: o.pauseSpeed
                        });
                    })
                });
            });
        }
    });
})(jQuery);

$(document).ready(function () {

    var video = document.getElementById("tm-welcome-video");

    video.onloadeddata = function() {
        $('#tm-video-text-overlay').removeClass('d-none');
        $('#tm-video-loader').addClass('d-none');

        $('#rotate').rotaterator({
            fadeSpeed: 1000,
            pauseSpeed: 300
        });
    }
    document.querySelector('.tm-current-year').textContent = new Date().getFullYear();
});



function generateTableHead(table, data) {
    let thead = table.createTHead();
    let row = thead.insertRow();
    for (let key of data) {
        let th = document.createElement("th");
        let text = document.createTextNode(key);
        th.appendChild(text);
        row.appendChild(th);
    }
}

function generateData() {
    var sut = []
    var data = []
    var temperaturSensor = document.querySelector("#tempSensor");
    var lichtSensor = document.querySelector("#lichtSensor");
    var abstandSensor = document.querySelector("#abstandSensor");
    sut.push(temperaturSensor)
    sut.push(lichtSensor)
    sut.push(abstandSensor)
    data.push("Zeit")

   // console.log(sut.innerHTML)
    for (let i = 0; i < sut.length; i++) {
        if (sut[i].innerHTML == "Temperatur" && sut[i].className == "aktiveButtons") {
            data.push("Temperatur")
            data.push("Luft")
        }
        else if (sut[i].innerHTML == "LichtSensor" && sut[i].className == "aktiveButtons") {
            data.push("UV");
            data.push("VIS");
            data.push("IR");
        }
        else if (sut[i].innerHTML == "Ultrarange" && sut[i].className == "aktiveButtons") {
            data.push("Abstand")
        }
    }
    return data
}

generateData();

function helperTable(data) {

    var help = [[]]
    for (let i = 0; i < data.length; i++) {

        if (data[i] == "Zeit") {
            help.push(zeit)
        }
        else if (data[i] == "Temperatur") {
            help.push(temperatur)
        }
        else if (data[i] == "Luft") {
            help.push(luft)
        }
        else if (data[i] == "Abstand") {
            help.push(abstand)
        }
        else if (data[i] == "UV") {
            help.push(uv)
        }
        else if (data[i] == "VIS") {
            help.push(vis)
        }
        else if (data[i] == "IR") {
            help.push(ir)
        }
    }
    return help
}
// console.log(helperTable(data2));
var zumT = helperTable(data2);

//  console.log(zumT[1][0])


function generateTable(table, data) {
    for (let i = 0; i < data[0].length; i++) {
        let row = table.insertRow();
        for (let b = 0; b < data.length; b++) {
            let cell = row.insertCell();
            let text = document.createTextNode(data[b][i]);
            cell.appendChild(text);
        }
    }
}


//etwas.remove();


function changeColor(id) {
    var element = document.querySelector("#" + id)
    var classes = element.className;
    if (classes == "aktiveButtons") {
        element.className = "deaktiveButtons";
    }
    else {
        element.className = "aktiveButtons";
    }

    //document.querySelector('#tempSensor').className = "deaktiveButtons";
    //document.querySelector('#tempSensor').style.color = 'blue';

}

function toggleGraph(id) {
    let clickedButton = document.getElementById(id);
    if (clickedButton.getAttribute("class") === "inactiveGraphButtons") {
        document.getElementById("myChart").style.maxHeight = "100%";
        let buttons = document.getElementsByClassName("activeGraphButtons");
        if (buttons.length > 0) buttons[0].setAttribute("class", "inactiveGraphButtons");
        clickedButton.setAttribute("class", "activeGraphButtons");
    } else {
        document.getElementById("myChart").style.maxHeight = "0";
        clickedButton.setAttribute("class", "inactiveGraphButtons");
    }

}

function tableErase() {

    var etwas = document.querySelector('#sensorDaten');
    //   var etwas = document.getElementsByClassName('table table-dark table-striped')
    etwas.remove();
    tabelleG = true;


    //let table = document.querySelector("table");
    var tag = document.createElement("table");
    tag.className = "etwas";
    tag.id = "sensorDaten";
    var element = document.querySelector(".hilfeTabelle")
    element.appendChild(tag);
    var table = document.querySelector(".etwas")

    var data = generateData();
    //console.log("Die neue Daten sind:" + data)
    var data2 = helperTable(data);
    data2.shift();
    //   console.log(data2)
    generateTable(table, data2);
    generateTableHead(table, data);
    table.className = "table table-dark table-striped";
}



function tableCreate() {
    const body = document.body,
        tbl = document.createElement('table');
    tbl.style.width = '100px';
    tbl.style.border = '1px solid black';

    for (let i = 0; i < 3; i++) {
        const tr = tbl.insertRow();
        for (let j = 0; j < 2; j++) {
            if (i === 2 && j === 1) {
                break;
            } else {
                const td = tr.insertCell();
                td.appendChild(document.createTextNode(`Cell I${i}/J${j}`));
                td.style.border = '1px solid black';
                if (i === 1 && j === 1) {
                    td.setAttribute('rowSpan', '2');
                }
            }
        }
    }
    body.appendChild(tbl);
}

var tempChart = null;
function createGraphs(Sensor) {
    var etwas = document.querySelector('#etwas');
    etwas.id = "etwas";
    etwas.style = "background:rgb(255, 255, 255);"

    var xValues = zeit;

    if (tempChart != null) tempChart.destroy();
    tempChart = new Chart("myChart", {
        type: "line",
        data: {
            labels: xValues,
            datasets: [{
                data: Sensor,
                borderColor: "red",
                fill: false
            },]
        },
        options: {
            maintainAspectRatio: false,
            legend: { display: false },
            scales: {
                        xAxes: [{
                        		ticks: {
                            	reverse: true
                            }
                        }]
                    }
        }
    });
}
