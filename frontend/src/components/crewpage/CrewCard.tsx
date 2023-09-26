import React from "react";
import { IoIosAddCircleOutline } from "react-icons/io";
import classes from "./CrewCard.module.css";

interface CrewCardProps {
  name?: string;
  key: number;
}

const CrewCard = ({ name, key }: CrewCardProps) => {
  return (
    <div className={classes.crewCardBox} key={key}>
      {name ? (
        <>
          <div className={classes.crewImgCircle}>
            <img
              src={`/images/${name}.jpg`}
              alt="crewImg"
              key={key}
              className={classes.crewImg}
            />
          </div>
          <p className={classes.crewName}>{name}</p>
          <div className={classes.crewCardBtns}>
            <div className={classes.memberCnt}>4명</div>
            <div className={classes.crewLastTime}>3일전</div>
          </div>
        </>
      ) : (
        <IoIosAddCircleOutline className={classes.crewAddBtn} />
      )}
    </div>
  );
};

export default CrewCard;
