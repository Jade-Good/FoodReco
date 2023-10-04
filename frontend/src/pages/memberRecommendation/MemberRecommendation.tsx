import React, { useState, useEffect } from "react";
import { FooterRecommendation } from "../../components/footer/FooterRecommendation";
import HeaderQuestion from "../../components/header/HeaderQuestion";
import { FoodCard } from "../../components/recommend/FoodCard";
import { FoodButton } from "../../components/recommend/FoodButton";
import FoodDetail from "../../components/recommend/FoodDetail";
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

export interface FoodFeedBackProps {
  foodFeedBack: (point: number) => void;
}

export const MemberRecommendation = () => {
  const [foodList, setFoodList] = useState<FoodList[]>([
    {
      recommendedFoodSeq: 3899,
      recommendedFoodName: "잠시 기다려 주세요",
      cookingMethod: "굽기",
      type: "메인반찬",
      category: "중화요리",
      img: "/favicon.ico",
      ingredientSimilarity: 0.776180681744197,
      foodNameSimilarity: 0.15384615384615385,
      originalFoodName: "짬뽕",
    },
  ]);

  const [foodIdx, setFoodIdx] = useState(0);

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
    setFoodIdx(0);
  };

  const foodfeedback = (point: number) => {
    let nowSeq = foodList[foodIdx].recommendedFoodSeq;
    let nextIdx = foodIdx + (1 % foodList.length);

    let nextSeq = foodList[nextIdx].recommendedFoodSeq;
    if (point === 3 || nextIdx === 0) nextSeq = 0;

    console.log(nowSeq, nextIdx, nextSeq, point);
    api
      .patch(
        `${process.env.REACT_APP_BASE_URL}/recommend/feedback/${nextSeq}`,
        { foodSeq: nowSeq, feedback: point }
      )
      .then((res) => {
        console.log("음식 피드백 성공:", res);
        if (point !== 3) {
          setFoodIdx(nextIdx);
        }
      })
      .catch((err) => {
        console.log("음식 피드백 실패:", err);
      });
  };

  return (
    <div style={{ overflow: "hidden", paddingTop: "24vmin" }}>
      <HeaderQuestion />

      <FoodCard
        foodImg={foodList[foodIdx].img}
        foodName={foodList[foodIdx].recommendedFoodName}
        foodFeedBack={foodfeedback}
      />

      <FoodDetail foodName={foodList[foodIdx].recommendedFoodName} />

      <FoodButton foodFeedBack={foodfeedback} />

      <FooterRecommendation />
    </div>
  );
};
