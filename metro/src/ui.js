import { MSTS } from "./mosmetro";
import axios from "axios";
const dateEl = document.getElementById("date");
const timeEl = document.getElementById("time");
const lineEl = document.getElementById("line");
const nameEl = document.getElementById("name");
const squareMetersEl = document.getElementById("squareMeters");
const buildingTypeEl = document.getElementById("buildingType");
const submitBtn = document.getElementById("submit");
const formEl = document.getElementById("form");
import { draw, getFromWhiteToRed, loading } from "./index.js";

class LoaderButton {
    constructor(button) {
        this.button = button;
        this.buttonText = this.button.querySelector(".button-text");
        this.loader = this.button.querySelector(".loader");
    }

    startLoader() {
        this.button.disabled = true;
        this.buttonText.style.display = "none";
        this.loader.style.display = "inline-block";
    }

    endLoader() {
        this.button.disabled = false;
        this.buttonText.style.display = "inline";
        this.loader.style.display = "none";
    }
}

const popup = document.getElementById("popup");

export function updatePopup({ fill, station, e }) {
    if (!station) {
        popup.style.opacity = '0';
        return;
    } else {
        popup.style.opacity = '1';
    }
    popup.style.left = `${e.clientX}px`;
    popup.style.top = `${e.clientY}px`;
    console.log(e);
    popup.innerHTML = `
        <h3 style="margin:0 10px 10px 10px; text-align: center">${station}</h3>
        <div class="filler-container">
            <div class="filler" style="width: ${Math.round(fill * 100)}%; background-color: ${getFromWhiteToRed(fill)}"></div>
            <p style="position: absolute; margin: 0px; text-align: center; width: 100%">Нагруженность ${Math.round(fill * 100)}%</p>
        </div>
    `;
}

const loader = new LoaderButton(submitBtn);

export const request = async (e) => {
    try {
        if (e) e.preventDefault();
        
        // Получение данных из всех полей
        const date = dateEl.value;
        const time = timeEl.value;
        const datetime = `${date} ${time}`;
        const line = lineEl.value;
        const name = nameEl.value;
        const squareMeters = parseFloat(squareMetersEl.value);
        const buildingType = buildingTypeEl.value;

        const data = {
            line,
            name,
            squareMeters,
            buildingType,
            datetime,
        };

        console.log(JSON.stringify(data));

        loader.startLoader();

        const response = await fetch("http://127.0.0.1:8003/api/stations/predict/frontend", {
        //const response = await fetch("http://127.0.0.1:5000", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        loader.endLoader();
        if (response.ok) {
            document.querySelectorAll('tr td div > svg > g > *').forEach(el => el.remove());
            var aaaa = await response.json();

            console.log(aaaa);
            loading.stations = aaaa;

            loading.stations = loading.stations.stations;
            draw();
        } else {
            console.error("Server returned an error:", await response.text());
        }
    } catch (error) {
        console.error("An error occurred:", error);
    }
};

// Привязка кнопки к обработчику
submitBtn.addEventListener("click", request);
