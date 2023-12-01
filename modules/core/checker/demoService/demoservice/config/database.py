"""Import Modules"""
from pymongo.mongo_client import MongoClient

client = MongoClient("mongodb://localhost:27017")

try:
    client.admin.command("ping")
    print("Connected successfully!!!")
except Exception as e: # pylint: disable=broad-except
    print("Could not connect to MongoDB:", e)

db = client.assessment_db
collection = db["assessment_collection"]
