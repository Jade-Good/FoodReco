import React from "react";
import styled, { css } from "styled-components";

import { IoIosAddCircleOutline } from "react-icons/io";
import classes from "./CrewCard.module.css";
import { useNavigate } from "react-router-dom";

import { CrewProps } from "../../pages/crew/CrewList";
import { useRecoilState } from "recoil";
import { crewDetail } from "../../recoil/atoms/crewState";

interface CrewMemberProps {
  name: string;
  profileImg: string;
  memberStatus: string;
  key: number;
}

const CrewMemberProfile = ({
  name,
  profileImg,
  memberStatus,
  key,
}: CrewMemberProps) => {
  const navigate = useNavigate();
  const [crewDetails, setCrewDetail] = useRecoilState(crewDetail);

  return (
    <ProfileFrame key={key} memberStatus={memberStatus}>
      <ProfileImg src={profileImg} />
      <CrewName>{name}</CrewName>
    </ProfileFrame>
  );
};

export default CrewMemberProfile;

const ProfileFrame = styled.div<{ memberStatus: string }>`
  display: flex;
  flex-direction: column;
  align-items: center;
  /* width: 10vmin; */
  height: 24vmin;

  ${(props) =>
    props.memberStatus === "미응답" &&
    css`
      opacity: 50%;
    `}
`;

const CrewName = styled.div`
  display: inline-block;
  font-size: 1rem;
  align-content: center;
  white-space: nowrap;
`;

const ProfileImg = styled.img<{ src: string }>`
  width: 15vmin;
  height: 15vmin;

  border-radius: 100%;

  border: solid orange 1px;
  overflow: hidden;
`;
