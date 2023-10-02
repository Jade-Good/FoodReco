import React, { useState } from "react";
import { FooterRecommendation } from "../../components/footer/FooterRecommendation";
import HeaderQuestion from "../../components/header/HeaderQuestion";
import { FoodCard } from "../../components/recommend/FoodCard";
import { FoodButton } from "../../components/recommend/FoodButton";
import FoodDetail from "../../components/recommend/FoodDetail";

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
