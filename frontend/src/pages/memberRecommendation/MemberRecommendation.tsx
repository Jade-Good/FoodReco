import React, { useState } from "react";
import { FooterRecommendation } from "../../components/footer/FooterRecommendation";
import HeaderQuestion from "../../components/header/HeaderQuestion";
import { FoodCard } from "../../components/recommend/foodCard";
import classes from "./MemberRecommendation.module.css";
import { FoodButton } from "../../components/recommend/foodButton";

interface Position {
  x: number;
  y: number;
}

export const MemberRecommendation = () => {
  const [isDragging, setIsDragging] = useState<boolean>(false);
  const [position, setPosition] = useState<Position>({ x: 0, y: 0 });

  const handleTouchStart = (e: React.TouchEvent<HTMLDivElement>) => {
    setIsDragging(true);

    const clientX = e.touches[0].clientX;
    const clientY = e.touches[0].clientY;

    const startX = clientX - position.x;
    const startY = clientY - position.y;

    const handleTouchMove = (e: TouchEvent) => {
      if (!isDragging) return;

      const clientX = e.touches[0].clientX;
      const clientY = e.touches[0].clientY;

      const newX = clientX - startX;
      const newY = clientY - startY;

      setPosition({ x: newX, y: newY });
    };

    const handleTouchEnd = () => {
      setIsDragging(false);
      document.removeEventListener("touchmove", handleTouchMove);
      document.removeEventListener("touchend", handleTouchEnd);
    };

    document.addEventListener("touchmove", handleTouchMove);
    document.addEventListener("touchend", handleTouchEnd);
  };

  return (
    <>
      <HeaderQuestion />
      <div
        className={`${classes.foodCard} ${isDragging ? "dragging" : ""}`}
        style={{ transform: `translate(${position.x}px, ${position.y}px)` }}
        onTouchStart={handleTouchStart}
      >
        <FoodCard />
      </div>
      <FoodButton />
      <FooterRecommendation />
    </>
  );
};
