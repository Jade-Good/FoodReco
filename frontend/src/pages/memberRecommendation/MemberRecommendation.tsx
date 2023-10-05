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
  reason: string;
}

export interface FoodFeedBackProps {
  foodFeedBack: (point: number) => void;
}

export interface Coords {
  latitude: number;
  longitude: number;
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
      reason: "대기하세요",
    },
  ]);
  // -------------------------- 현위치 -------------------------
  const [coords, setCoords] = useState<Coords | null>(null);
  const [error, setError] = useState<string | null>(null);

  // -------------------------- 추천메뉴 -------------------------
  const [foodIdx, setFoodIdx] = useState(0);

  const getPos = async (): Promise<Coords | undefined> => {
    try {
      if ("geolocation" in navigator) {
        const position = await new Promise<any>((resolve, reject) => {
          navigator.geolocation.getCurrentPosition(
            (position) => resolve(position),
            (err) => reject(err)
          );
        });

        const { latitude, longitude } = position.coords;
        return { latitude, longitude };
      } else {
        setError("브라우저에서 Geolocation을 지원하지 않습니다.");
      }
    } catch (error) {
      console.error("GPS 정보를 가져오지 못했습니다:", error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      const pos = await getPos();

      if (!pos || pos.latitude === 0 || pos.longitude === 0) {
        setTimeout(fetchData, 1000); // 1초 후 재시도
        return;
      }

      try {
        const response = await api.get<FoodList[]>(
          `${process.env.REACT_APP_BASE_URL}/recommend/personal/${pos.longitude}/${pos.latitude}`
          // `${process.env.REACT_APP_BASE_URL}/recommend/personal/${pos.latitude}/${pos.longitude}`
        );
        console.log(response.data);
        setFoodList(response.data);
      } catch (err) {
        console.error("추천 메뉴 못가져옴:", err);
        // 오류 처리를 추가하거나 재시도 등의 조치를 취할 수 있습니다.
      }

      setCoords(pos);
      setFoodIdx(0);
    };

    fetchData();
  }, []);

  const foodfeedback = (point: number) => {
    let nowSeq = foodList[foodIdx].recommendedFoodSeq;
    let nextIdx = foodIdx + (1 % foodList.length);

    let nextSeq = foodList[nextIdx].recommendedFoodSeq;
    if (point === 3 || nextIdx === 0) nextSeq = 0;

    console.log(nowSeq, nextIdx, nextSeq, point);
    api
      .patch(
        `${process.env.REACT_APP_BASE_URL}/recommend/feedback/${nextSeq}/${coords?.longitude}/${coords?.latitude}`,
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

      <FoodDetail
        foodName={foodList[foodIdx].recommendedFoodName}
        longitude={coords ? coords.longitude : 0}
        latitude={coords ? coords.latitude : 0}
      />

      <FoodButton foodFeedBack={foodfeedback} />

      <FooterRecommendation />
    </div>
  );
};
