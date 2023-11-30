"""Module for REST API"""
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
async def read_root():
    """Demo service for checker"""
    return {"Hello": "World"}
