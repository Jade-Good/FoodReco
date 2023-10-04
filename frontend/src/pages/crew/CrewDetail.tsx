import React, { useState, useEffect } from "react";
import styled, { css } from "styled-components";

import { FooterCrew } from "../../components/footer/FooterCrew";
import HeaderCrewDetail from "../../components/header/HeaderCrewDetail";

import { CrewProps } from "../../pages/crew/CrewList";
import { useRecoilState } from "recoil";
import { crewDetail } from "../../recoil/atoms/crewState";

import api from "../../utils/axios";

interface CrewDetailProps {
  crewSeq: number;
  crewName: string;
  crewImg: string;
  crewStatus: "투표중" | "분석중" | "투표중";
  memberStatus: "미응답" | "수락" | "거절"; // 본인의 그룹 가입 여부
  crewMembers: {
    memberSeq: number;
    memberName: string;
    memberImg: string;
    memberStatus: "미응답" | "수락" | "거절"; // 해당 사용자의 가입 여부
  };

  // 투표 중인경우만 존재(아닌경우 null)
  voteRecommend: {
    crewRecommendSeq: number;
    crewRecommendTime: Date;
    foodList: {
      foodSeq: number;
      foodName: string;
      foodImg: string;
      foodVoteCount: number;
    };
  } | null;

  //분석 중인 경우 []
  histories: history[];
}

interface history {
  crewRecommendSeq: number;
  crewRecommendTime: Date;
  foodList: {
    foodSeq: number;
    foodName: string;
    foodImg: string;
    foodVoteCount: number;
  };
}

export const CrewDetail = () => {
  const [crewDetailInfo, setCrewDetailInfo] = useState<CrewDetailProps>();
  const [crewDetails, setCrewDetail] = useRecoilState(crewDetail);

  useEffect(() => {
    getCrewDetail();
  }, []);

  const getCrewDetail = () => {
    api
      .get(
        `${process.env.REACT_APP_BASE_URL}/crew/detail/${crewDetails.crewSeq}`
      )
      .then((res) => {
        console.log(res);
        setCrewDetailInfo(res.data);
        console.log(res.data);
        console.log(crewDetailInfo);
      })
      .catch((err) => {
        console.log("그룹 상세 정보 못가져옴:", err);
      });
  };

  return (
    <>
      <HeaderCrewDetail />
      <CrewFrame>
        <CrewImg
          src={`${crewDetails.img}` ? `${crewDetails.img}` : "/favicon.ico"}
        />
        <h1 style={{ margin: "0", fontSize: "4vmax" }}>{crewDetails.name}</h1>
        <div style={{ width: "90vw", margin: "10vw" }}>
          <h1 style={{ margin: "0 0 5vmin 0", fontSize: "3vmax" }}>그룹원</h1>
          <div>사진 및 이름 프로필~~</div>
        </div>
        <div style={{ width: "90vw" }}>
          <h1 style={{ margin: "0 0 5vmin 0", fontSize: "3vmax" }}>메뉴투표</h1>
          <div>메뉴 버튼이나 투표 리스트~</div>
        </div>
      </CrewFrame>
      <FooterCrew />
    </>
  );
};

const CrewFrame = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 24vmin;
  height: 80vh;
`;

const CrewImg = styled.img<{ src: string }>`
  width: 80vmin;
  height: 80vmin;

  max-width: 30vh;
  max-height: 30vh;

  border-radius: 100%;
  margin-bottom: 2vmax;

  /* border: solid orange 1px; */
`;
