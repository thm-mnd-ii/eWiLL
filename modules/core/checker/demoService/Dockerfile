FROM tiangolo/uvicorn-gunicorn-fastapi:python3.9-slim AS builder

WORKDIR /app

COPY ./pyproject.toml ./

RUN pip3 install poetry
RUN poetry config virtualenvs.create false

RUN poetry install --no-dev

COPY . .

CMD ["uvicorn", "demoservice.main:app", "--host", "0.0.0.0", "--port", "80"]
