import React, { useState, useEffect } from "react";
import styled from "styled-components";
import ToggleButton from "@mui/material/ToggleButton";

const TagDiv = styled.div`
  display: flex;
  background-color: white;
  border: 2px solid #fe9d3a;
  width: 5rem;
  height: 2rem;
  justify-content: center;
  align-items: center;
  border-radius: 1rem;
  margin: 0.2rem; /* Add margin to create spacing between ToggleButtons */
`;

const ToggleButtonContainer = styled.div`
  display: flex;
  flex-wrap: wrap; /* Allow ToggleButtons to wrap to the next line */
`;

const CustomToggleButton = styled(ToggleButton)`
  && {
    border-radius: 1rem; /* Increase border-radius for a more rounded appearance */
    margin: 0.2rem; /* Add margin to create spacing between ToggleButtons */
    // border: 1px solid orange;
    // color: orange;
  }
`;

interface ChooseFoodProps {
  foodList: string[];
}

export const ChooseUnlikeFood: React.FC<ChooseFoodProps> = ({ foodList }) => {
  const [unlikeFood, setUnlikeFood] = useState<string[]>([]);

  const toggleUnlikeFood = (food: string) => {
    if (unlikeFood.includes(food)) {
      setUnlikeFood(unlikeFood.filter((item) => item !== food));
    } else {
      setUnlikeFood([...unlikeFood, food]);
    }
  };

  useEffect(() => {
    console.log("likefood 변경됨:", unlikeFood);
  }, [unlikeFood]);

  return (
    <ToggleButtonContainer>
      {foodList.map((food, index) => (
        <CustomToggleButton
          value="check"
          selected={unlikeFood.includes(food)}
          onClick={() => toggleUnlikeFood(food)}
          key={index}
        >
          {food}
        </CustomToggleButton>
      ))}
    </ToggleButtonContainer>
  );
};
