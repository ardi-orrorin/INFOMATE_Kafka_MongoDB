import http from 'k6/http';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


export default function(data) {
    http.get('http://localhost:9091/chat/10/100/2023-08-15')
    // http.get('http://localhost:9091/chat1/10/100/2023-08-15')

}

export function handleSummary(data) {
    return {
        "summary.html": htmlReport(data),
    };
}
