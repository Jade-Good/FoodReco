import React, { useState } from "react";
import { FooterRecommendation } from "../../components/footer/FooterRecommendation";
import HeaderQuestion from "../../components/header/HeaderQuestion";
import { FoodCard } from "../../components/recommend/foodCard";
import { FoodButton } from "../../components/recommend/foodButton";
import FoodDetail from "../../components/recommend/foodDetail";

export const MemberRecommendation = () => {
  return (
    <div style={{ overflow: "hidden", paddingTop: "24vmin" }}>
      <HeaderQuestion />

      <FoodCard />

      <FoodDetail />

      <FoodButton />

      <FooterRecommendation />
    </div>
  );
};
