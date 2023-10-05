import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import styled, { css } from "styled-components";
import api from "../../utils/axios";

import { FooterCrew } from "../../components/footer/FooterCrew";
import HeaderCrewDetail from "../../components/header/HeaderCrewDetail";

import { CrewProps } from "../../pages/crew/CrewList";
import CrewMemberProfile from "../../components/crewpage/CrewMemberProfile";

interface CrewDetailProps {
  memberSeq: number;
  crewSeq: number;
  crewName: string;
  crewImg: string;
  crewStatus: "투표전" | "분석중" | "투표중";
  memberStatus: "미응답" | "수락"; // 본인의 그룹 가입 여부
  crewMembers: crewMembers[];

  // 투표 중인경우만 존재(아닌경우 null)
  voteRecommend: {
    crewRecommendSeq: number;
    crewRecommendTime: Date;
    foodList: foodList[];
  } | null;

  //분석 중인 경우 []
  histories: history[];
}

interface foodList {
  foodSeq: number;
  foodName: string;
  foodImg: string;
  foodVoteCount: number;
}

interface crewMembers {
  memberSeq: number;
  memberName: string;
  memberImg: string;
  memberStatus: "미응답" | "수락"; // 해당 사용자의 가입 여부
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
  const [crewDetailInfo, setCrewDetailInfo] = useState<CrewDetailProps | null>(
    null
  );
  const { crewSeq } = useParams();

  useEffect(() => {
    console.log("크루 상세 정보 조회");

    let eventSource: EventSource;

    async function getCrewInfo() {
      await getCrewDetail();

      console.log("투표 SSE 연결");
      eventSource = new EventSource(
        `${process.env.REACT_APP_BASE_URL}/crew/sse/${crewSeq}/${crewDetailInfo?.memberSeq}`
      );

      eventSource.addEventListener("connect", (e) => {
        console.log("connect");
      });
      eventSource.addEventListener("start", (e) => {
        console.log("start", e);
      });
      eventSource.addEventListener("end", (e) => {
        console.log("end", e);
      });

      eventSource.addEventListener("vote", (e) => {
        console.log(JSON.parse(e.data));
      });
      eventSource.onerror = (error) => {
        console.error("SSE Error:", error);
        eventSource.close();
      };
    }

    getCrewInfo();

    // if (!crewDetailInfo?.memberSeq) {
    //   console.log("회원 번호 없음 - SSE 연결 취소");
    //   return;
    // }

    return () => {
      eventSource.close();
    };
  }, []);

  const getCrewDetail = () => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/crew/detail/${crewSeq}`)
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
          src={
            `${crewDetailInfo?.crewImg}`
              ? `${crewDetailInfo?.crewImg}`
              : "/favicon.ico"
          }
        />
        <h1 style={{ margin: "0", fontSize: "4vmax" }}>
          {crewDetailInfo?.crewName}
        </h1>
        <div style={{ width: "90vw" }}>
          <h1 style={{ margin: "0 0 2vmin 0", fontSize: "1.3rem" }}>그룹원</h1>
          <div
            style={{
              display: "flex",
              gap: "5vmin",
              overflowX: "scroll",
              padding: " 1rem 1rem 0 1rem",
            }}
          >
            {crewDetailInfo?.crewMembers.map((member, key) => {
              return (
                <CrewMemberProfile
                  name={member.memberName}
                  profileImg={member.memberImg}
                  memberStatus={member.memberStatus}
                  key={key}
                />
              );
            })}
          </div>
        </div>
        <div style={{ width: "90vw" }}>
          <h1 style={{ margin: "0 0 5vmin 0", fontSize: "1.3rem" }}>
            메뉴투표
          </h1>
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
  /* height: 80vh; */
  gap: 2vh;

  overflow-y: scroll;
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
