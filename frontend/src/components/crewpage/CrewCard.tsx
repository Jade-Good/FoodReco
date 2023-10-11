import React from "react";
import { IoIosAddCircleOutline } from "react-icons/io";
import classes from "./CrewCard.module.css";
import { useNavigate } from "react-router-dom";

import { CrewProps } from "../../pages/crew/CrewList";

interface CrewCardProps {
  key: number;
  crew?: CrewProps;
  onClick?: () => void;
}

const CrewCard = ({ crew, key, onClick }: CrewCardProps) => {
  const navigate = useNavigate();

  const crewDetailNav = () => {
    if (!crew) return;

    navigate(`/crew/detail/${crew.crewSeq}`);
  };
  const makeCrew = () => {
    navigate("/crew/make");
  };
  return (
    <div className={classes.crewCardBox} key={key} onClick={crewDetailNav}>
      {crew ? (
        <>
          <div className={classes.crewImgCircle}>
            <img
              src={crew.img ? crew.img : "/favicon.ico"}
              alt="crewImg"
              key={key}
              className={classes.crewImg}
            />
          </div>
          <p className={classes.crewName}>{crew.name}</p>
          <div className={classes.crewCardBtns}>
            <div className={classes.memberCnt}>{crew.crewCnt}명</div>
            <div className={classes.crewLastTime}>
              {crew.recentRecommend === -1
                ? `신규그룹`
                : `${crew.recentRecommend}일전`}
            </div>
          </div>
        </>
      ) : (
        <IoIosAddCircleOutline
          onClick={makeCrew}
          className={classes.crewAddBtn}
        />
      )}
    </div>
  );
};

export default CrewCard;
