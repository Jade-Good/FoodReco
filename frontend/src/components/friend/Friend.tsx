import React from "react";
import classes from "./Friend.module.css";
import axios from "axios";

interface FriendProps {
  name: string;
  key: number;
  imgUrl: string | null;
}

export const Friend = ({ name, key, imgUrl }: FriendProps) => {
  return (
    <div className={classes.friendBox}>
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
    </div>
  );
};
