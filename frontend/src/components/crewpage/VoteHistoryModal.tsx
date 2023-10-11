import React, { useState, useEffect, useRef } from "react";
import styled, { css } from "styled-components";
import api from "../../utils/axios";

import Modal from "react-modal";
import { useRecoilState } from "recoil";
import { crewVoteHistorylModal } from "../../recoil/atoms/modalState";

import { voteRecommend, foodList } from "../../pages/crew/CrewDetail";
import ProgressBar from "./ProgressBar";

interface voteModalProps extends voteRecommend {
  memberCnt?: number;
  crewSeq?: number;
}

const VoteHistoryModal: React.FC<voteModalProps> = ({
  crewRecommendSeq,
  crewRecommendTime,
  foodList,
  memberCnt,
  crewSeq,
}) => {
  // -------------------------- 프로그래스 바 --------------------------
  const [progress, setProgress] = useState<number[]>([]);

  useEffect(() => {
    if (!foodList) return;
    if (!memberCnt) return;

    let cnt: number[] = [];

    for (let i = 0; i < foodList?.length; i++) {
      cnt.push((foodList[i].foodVoteCount / (memberCnt + 1)) * 100);
    }

    setProgress(cnt);
  }, [foodList]);

  // -------------------------- 모달 --------------------------
  const [modal, setModalOpen] = useRecoilState(crewVoteHistorylModal);

  console.log(foodList);

  const check = (foodSeq: number) => {
    if (!crewSeq) return;
    api
      .post(`${process.env.REACT_APP_BASE_URL}/crew/vote`, {
        crewSeq: crewSeq,
        crewRecommendSeq: crewRecommendSeq,
        foodSeq: foodSeq,
      })
      .then((res) => {
        console.log(res.data); // 그룹 투표 시작
      })
      .catch((err) => {
        console.error("투표 시작 실패", err);
      });
  };

  return (
    <Modal
      isOpen={modal.modalOpen}
      onRequestClose={() => setModalOpen({ modalOpen: false })}
      style={customModalStyles}
      ariaHideApp={false}
      contentLabel="Pop up Message"
      shouldCloseOnOverlayClick={true}
    >
      {/* 메뉴 투표 창 */}
      <div style={{ padding: "1rem" }}>
        <h1 style={{ margin: "0 0 5vmin 0", fontSize: "1.5rem" }}>메뉴 투표</h1>
        <h6 style={{ textAlign: "right" }}>
          미투표 : {foodList ? foodList[0].foodVoteCount : 0}명
        </h6>
        <div style={{ textAlign: "center" }}>
          {foodList?.map((food, key) => {
            if (key === 0) return;
            return (
              <VoteItem
                check={food.vote}
                onClick={() => {
                  check(food.foodSeq);
                }}
              >
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "flex-end",
                    userSelect: "none",
                  }}
                >
                  <div style={{ display: "flex", alignItems: "flex-end" }}>
                    <FoodImg src={food.foodImg} />
                    <div
                      style={{
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "flex-start",
                        marginLeft: "3vmin",
                      }}
                    >
                      <h5
                        style={{
                          margin: "0 0 1vmin 0",
                          fontSize: "1.2rem",
                          textAlign: "left",
                        }}
                      >
                        {food.foodName}
                      </h5>
                      <div
                        style={{
                          display: "inline",
                          textAlign: "center",
                          width: "6rem",
                          padding: "0.2rem 0.2rem",
                          border: "1px solid #fe9d3a",
                          color: "#fe9d3a",
                          borderRadius: "1rem",
                          fontSize: "0.8rem",
                        }}
                        onClick={() => {
                          console.log("클릭");
                        }}
                      >
                        상세보기 버튼
                      </div>
                    </div>
                  </div>
                  <p style={{ fontSize: "1rem" }}>
                    {foodList[key].foodVoteCount}명
                  </p>
                </div>
                <ProgressBar percentage={progress[key]} />
              </VoteItem>
            );
          })}
        </div>
      </div>
    </Modal>
  );
};

export default VoteHistoryModal;

const VoteItem = styled.div<{ check: boolean }>`
  margin: 1rem 0;
  overflow: scroll;
  padding: 1rem;
  /* border: 1px solid orange; */
  &:active {
    background-color: #dcdcdc;
  }

  ${(props) =>
    props.check &&
    css`
      background-color: #fff0e7;
      /* border: solid #fe9d3a 1px; */
      /* color: #fe9d3a; */
    `}
`;

const FoodImg = styled.img<{ src: string }>`
  width: 15vmin;
  height: 15vmin;

  border-radius: 100%;
  /* margin-bottom: 2vmax; */

  border: solid orange 1px;
`;

// isOpen
// 모달 창이 표시되어야 하는지 여부를 설명하는 boolean 값이다.
// 즉, 해당 값이 true여야 모달 창이 열리는 것이다.

// onRequestClose
// 모달이 닫힐 때 실행될 함수를 의미한다.
// 즉,사용자가 모달을 닫으려고 할 때 실행되는 함수이다.

// style
// 모달 창과 모달 창 바깥에 대한 style을 지정해준다.

// ariaHideApp
// appElement를 숨길지 여부를 나타내는 boolean 값입니다.
// 이 값이 true이면 appElement가 숨겨준다.

// contentLabel
// 스크린리더 사용자에게 콘텐츠를 전달할 때
// 사용되는 방법을 나타내는 문자열이다.

// shouldCloseOnOverlayClick
//팝업창이 아닌 바깥 부분에서 클릭하였을 때, 닫히도록 할 것인지에 대한 처리이다.
// 기본값으로는 true를 가지고 있다.

// CSS

/*overlay는 모달 창 바깥 부분을 처리하는 부분이고,
content는 모달 창부분이라고 생각하면 쉬울 것이다*/
const customModalStyles: ReactModal.Styles = {
  overlay: {
    backgroundColor: " rgba(0, 0, 0, 0.4)",
    width: "100%",
    height: "100vh",
    zIndex: "10",
    position: "fixed",
    top: "0",
    left: "0",
  },
  content: {
    width: "90vw",
    height: "60vh",
    // zIndex: "150",
    // position: "absolute",
    top: "50%",
    left: "50%",

    padding: "0",

    transform: "translate(-50%, -50%)",
    borderRadius: "1rem",
    boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
    backgroundColor: "white",
    justifyContent: "center",
    overflow: "auto",
  },
};
