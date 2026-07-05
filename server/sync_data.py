import os
import shutil
from huggingface_hub import hf_hub_download, HfApi

def download_latest_db_data():
    """
    Finds and downloads the latest processed DB punctuality data from Hugging Face.
    Clears old data before downloading.
    """
    repo_id = "piebro/deutsche-bahn-data"
    local_dir = "data"

    # 1. Clear old data
    if os.path.exists(local_dir):
        print(f"Cleaning up old data in {local_dir}...")
        shutil.rmtree(local_dir)
    os.makedirs(local_dir)

    print(f"Searching for the latest data in {repo_id}...")

    try:
        api = HfApi()
        files = api.list_repo_files(repo_id=repo_id, repo_type="dataset")

        # Filter for monthly processed data and sort them to get the newest
        monthly_files = [f for f in files if "monthly_processed_data/data-" in f and f.endswith(".parquet")]

        if not monthly_files:
            print("No processed data files found.")
            return

        latest_file = sorted(monthly_files)[-1]

        print(f"Latest file found: {latest_file}")
        print("Starting download...")

        path = hf_hub_download(
            repo_id=repo_id,
            filename=latest_file,
            repo_type="dataset",
            local_dir=local_dir,
            local_dir_use_symlinks=False
        )
        print(f"Successfully downloaded to: {path}")
        print("\nYour Punctuality Server is now using the most recent historical data.")

    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    download_latest_db_data()
