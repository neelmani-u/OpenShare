import axios from "axios";

let sessionPromise;
let sessionData;
let sessionError;


export function getSessionResource() {
    if (sessionData) return sessionData;

    if (!sessionPromise) {
        sessionPromise = axios
            .get("http://localhost:8084/api/auth/check-session", {
                withCredentials: true,
            })
            .then((res) => {
                console.log(res.data);
                sessionData = res.data || null;
            })
            .catch((err) => {
                sessionError = err;
            });
    }

    if (sessionError) throw sessionError;
    throw sessionPromise;
}
