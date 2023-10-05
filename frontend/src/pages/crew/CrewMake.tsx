import React, { useState, useEffect, ChangeEvent } from "react";
import { FooterCrew } from "../../components/footer/FooterCrew";
import HeaderCrewInviteBasic from "../../components/header/HeaderCrewInviteBasic";
import HeaderCrewInviteProfile from "../../components/header/HeaderCrewInviteProfile";
import api from "../../utils/axios";
import { Friend } from "../../components/friend/Friend";
import classes from "./CrewMake.modules.css";
import { CrewAdd } from "../../components/crewpage/CrewAdd";
import axios from "axios";
import StyledButton from "../../styles/StyledButton";
import { useNavigate } from "react-router-dom";
interface CrewData {
  crewName: string;
  crewMembers: number[];
  crewImg?: string | File | null;
}
export const CrewMake = () => {
  const navigate = useNavigate();
  const [friendCount, setFriendCount] = useState(0);
  const [friendList, setFriendList] = useState<
    { memberNickname: string; memberImg: string; memberSeq: number }[]
  >([]);
  const [search, setSearch] = useState("");
  const [isInputFocused, setIsInputFocused] = useState(false);
  const [crew, setCrew] = useState<number[]>([]);
  const [crewName, setCrewName] = useState("");
  const [crewImg, setCrewImg] = useState<File | null>(null);

  useEffect(() => {
    setFriendCount(crew.length);
  }, [crew]);

  useEffect(() => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/member/friend/list`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      })
      .then((res) => {
        // console.log(res);
        setFriendList(res.data.friendList);
      })
      .catch((err) => console.log(err));
  }, []);

  const toggleFriend = (friendSeq: number) => {
    if (crew.includes(friendSeq)) {
      // 음식이 이미 좋아요 목록에 있는 경우, 제거합니다.
      setCrew(crew.filter((item) => item !== friendSeq));
    } else {
      // 음식이 좋아요 목록에 없는 경우, 추가합니다.
      setCrew([...crew, friendSeq]);
    }
  };

  const handleFileChange = (
    event: ChangeEvent<HTMLInputElement>,
    setter: React.Dispatch<React.SetStateAction<File | null>>
  ) => {
    const file = event.target.files?.[0];
    if (file) {
      setter(file);
    }
  };

  const handleMakeCrew = (e: React.FormEvent) => {
    e.preventDefault();
    let crewData: CrewData | undefined;

    const formData = new FormData();

    crewData = {
      crewName: crewName,
      // crewImg: crewImg,
      crewMembers: crew,
    };

    // console.log(crewData);
    formData.append("crewName", crewName); // crewName 추가
    if (crewImg) {
      formData.append("crewImg", crewImg); // crewImg 추가
    }

    const crewMembersJSON = JSON.stringify(
      crew.map((memberSeq) => ({ memberSeq }))
    );
    formData.append("crewMembers", crewMembersJSON);

    // formData.append(
    //   "request",
    //   new Blob([JSON.stringify(crewDa)], { type: "application/json" })
    // );

    console.log("crewMembers", formData.get("crewMembers"));

    api
      .post(`${process.env.REACT_APP_BASE_URL}/crew/regist`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${localStorage.getItem("accesstoken")}`,
        },
      })
      .then((res) => {
        console.log(res);
        navigate("/crew");
      })
      .catch((err) => console.log(err));
  };

  const handleCrewName = (e: ChangeEvent<HTMLInputElement>) => {
    setCrewName(e.target.value);
  };

  return (
    <>
      <HeaderCrewInviteBasic count={friendCount} />
      <div style={{ margin: "5.5rem 0", overflow: "hidden" }}>
        <form onSubmit={handleMakeCrew}>
          <input
            style={inputStyle}
            value={crewName}
            placeholder="그룹명을 적어주세요"
            onChange={handleCrewName}
          />
          <input
            accept="image/*"
            type="file"
            id="certificateUploadInput"
            onChange={(e) => handleFileChange(e, setCrewImg)}
          />
          {crewImg && (
            <img
              src={URL.createObjectURL(crewImg)}
              alt="Crew"
              width="100"
              height="100"
              style={{ marginTop: "20px" }}
            />
          )}
          <StyledButton type="submit">생성하기</StyledButton>
        </form>

        {friendList.map((friend, i) => {
          return (
            <CrewAdd
              name={friend.memberNickname}
              imgUrl={friend.memberImg}
              key={friend.memberSeq}
              onClick={() => toggleFriend(friend.memberSeq)}
              selected={crew.includes(friend.memberSeq)}
            />
          );
        })}
      </div>
      <FooterCrew />
    </>
  );
};

const inputStyle = {
  borderRadius: "20px",
  border: "1px solid #FE9D3A",
  display: "flex",
  justifyContent: "center",
  width: "70vw",
  height: "3vh",

  alignItems: "center", // 세로 중앙 정렬
  transition: "border-color 0.2s", // 테두리 색상 변화를 부드럽게 만들기 위한 트랜지션

  margin: "0 auto",
};
