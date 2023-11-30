FROM python:3.11-slim-bullseye AS builder

WORKDIR /app

COPY modules/core/checker/demoService/pyproject.toml ./

RUN pip3 install poetry
RUN poetry config virtualenvs.create false

RUN poetry install --no-dev

COPY modules/core/checker/demoService/ ./

CMD ["uvicorn", "demoservice.main:app", "--host", "0.0.0.0", "--port", "80"]
