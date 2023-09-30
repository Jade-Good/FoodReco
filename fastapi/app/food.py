from pydantic import BaseModel

class Food(BaseModel):
    foodSeq: int
    name: str