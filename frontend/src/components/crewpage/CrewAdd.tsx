import React from "react";
import classes from "./CrewAdd.module.css";
import axios from "axios";
import ToggleButton from "@mui/material/ToggleButton";

interface FriendProps {
  name: string;
  key: number;
  imgUrl: string | null;
  onClick: () => void;
  selected: boolean;
}

export const CrewAdd: React.FC<FriendProps> = ({
  name,
  key,
  imgUrl,
  onClick,
  selected,
}) => {
  return (
    <>
      <div className={classes.friendBox} onClick={onClick}>
        <div className={classes.profileBox}>
          {imgUrl ? (
            <img
              src={imgUrl}
              alt="sds"
              key={key}
              className={classes.profileImg}
            />
          ) : (
            <img
              src="/images/profileImg.png"
              alt="sds"
              key={key}
              className={classes.profileImg}
            />
          )}
        </div>

        <h3>{name}</h3>

        <div
          style={{
            backgroundColor: selected ? "orange" : "white",
            borderRadius: "50%",
            border: "1px solid black",
            width: "5vw",
            height: "5vw",
            marginLeft: "30vw",
          }}
        ></div>
      </div>
    </>
  );
};
