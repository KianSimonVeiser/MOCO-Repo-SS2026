from fastapi import FastAPI, Query
from typing import List
import duckdb
import os
import uvicorn

app = FastAPI(title="DB Punctuality Server")

DATA_PATH = "data/monthly_processed_data/*.parquet"

@app.get("/statistics/connection")
async def get_connection_stats(trains: List[str] = Query(...)):
    print(f"\n--- Request: {trains} ---")

    total_score = 0.0
    max_risk = 0.0
    found_count = 0
    details = []

    parquet_files = []
    if os.path.exists("data"):
        for root, _, files in os.walk("data"):
            for file in files:
                if file.endswith(".parquet"):
                    parquet_files.append(os.path.join(root, file))

    if not parquet_files:
         return {"score": 0.0, "bindingLossProbability": 1.0, "error": "No data files"}

    db = duckdb.connect(':memory:')

    for train_id in trains:
        try:
            parts = train_id.strip().split(" ")
            if len(parts) < 2: continue

            t_type, t_num = parts[0].upper(), parts[1]

            query = f"""
                SELECT
                    avg(CASE WHEN is_canceled = false THEN delay_in_min ELSE NULL END) as avg_delay,
                    count(CASE WHEN delay_in_min > 20 OR is_canceled = true THEN 1 END) * 1.0 / count(*) as risk,
                    count(CASE WHEN is_canceled = true THEN 1 END) * 1.0 / count(*) as cancel_rate,
                    count(*) as total_samples
                FROM read_parquet({parquet_files})
                WHERE UPPER(train_type) = '{t_type}' AND train_number = '{t_num}'
            """
            res = db.execute(query).fetchone()

            if res and res[3] > 0:
                avg_delay = res[0] if res[0] is not None else 0.0
                risk = res[1] if res[1] is not None else 0.0
                cancel_rate = res[2] if res[2] is not None else 0.0

                # Verbesserte Score-Formel: 10 - (Verspätung / 5) - (Ausfallrate * 10)
                # Beispiel: 10 Min Delay -> Score 8.0. 20% Ausfall -> -2.0 Punkte.
                score = 10.0 - (avg_delay / 5.0) - (cancel_rate * 10.0)
                score = max(0.0, min(10.0, score))

                print(f"[{t_type} {t_num}] Delay: {avg_delay:.1f}m, Canceled: {cancel_rate:.1%}, Samples: {res[3]}")

                total_score += score
                max_risk = max(max_risk, risk)
                found_count += 1
                details.append({"train": train_id, "delay": round(avg_delay, 1), "score": round(score, 1)})
            else:
                print(f"[{t_type} {t_num}] Keine Daten gefunden.")
        except Exception as e:
            print(f"Fehler bei {train_id}: {e}")

    if found_count > 0:
        final_score = total_score / found_count
        final_risk = max_risk
    else:
        final_score, final_risk = 7.5, 0.05

    print(f"FINALES ERGEBNIS -> Score: {final_score:.1f}, Risiko: {final_risk:.1%}")

    return {
        "score": round(final_score, 1),
        "bindingLossProbability": round(final_risk, 3),
        "found_trains": found_count,
        "details": details
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
