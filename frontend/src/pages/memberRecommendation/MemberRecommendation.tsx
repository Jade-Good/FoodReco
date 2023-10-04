import React, { useState, useEffect } from "react";
import { FooterRecommendation } from "../../components/footer/FooterRecommendation";
import HeaderQuestion from "../../components/header/HeaderQuestion";
import { FoodCard } from "../../components/recommend/foodCard2";
import { FoodButton } from "../../components/recommend/foodButton2";
import FoodDetail from "../../components/recommend/foodDetail2";
import api from "../../utils/axios";

export interface FoodList {
  recommendedFoodSeq: number;
  recommendedFoodName: string;
  cookingMethod: string;
  type: string;
  category: string;
  img: string;
  ingredientSimilarity: number;
  foodNameSimilarity: number;
  originalFoodName: string;
}

export const MemberRecommendation = () => {
  const [foodList, setFoodList] = useState<FoodList[]>([
    {
      recommendedFoodSeq: 3899,
      recommendedFoodName: "짜장면",
      cookingMethod: "굽기",
      type: "메인반찬",
      category: "중화요리",
      img: "../images/짜장면.png",
      ingredientSimilarity: 0.776180681744197,
      foodNameSimilarity: 0.15384615384615385,
      originalFoodName: "짬뽕",
    },
  ]);

  let foodIdx = 0;

  // 최초 1회 검색 및 지도 생성
  useEffect(() => {
    foodRecommend();
  }, []);

  const foodRecommend = () => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/recommend/personal`)
      .then((res) => {
        console.log(res);
        setFoodList(res.data);
      })
      .catch((err) => {
        console.log("추천 메뉴 못가져옴:", err);
      });
  };

  return (
    <div style={{ overflow: "hidden", paddingTop: "24vmin" }}>
      <HeaderQuestion />

      <FoodCard
        foodImg={foodList[foodIdx].img}
        foodName={foodList[foodIdx].recommendedFoodName}
      />

      <FoodDetail foodName={foodList[foodIdx].recommendedFoodName} />

      <FoodButton />

      <FooterRecommendation />
    </div>
  );
};
