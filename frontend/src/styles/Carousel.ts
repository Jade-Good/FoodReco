import styled from "styled-components";

export const CarouselContainer = styled.div`
  display: flex;
  overflow: hidden;
  width: 300px; /* Set the desired width of the carousel */
`;

export const SlideContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-width: 100%;
  transition: transform 0.3s ease-in-out;
`;

export const SlideImage = styled.img`
  max-width: 100%;
  height: auto;
`;

export const SlideText = styled.p`
  /* Add any text styles here */
`;
