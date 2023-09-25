import React from "react";
import classes from "./Friend.module.css";

interface FriendProps {
  name: string;
  key: number;
}

export const Friend = ({ name, key }: FriendProps) => {
  return (
    <div className={classes.friendBox}>
      <div className={classes.profileBox}>
        <img
          src={`/images/${name}.jpg`}
          alt="sds"
          key={key}
          className={classes.profileImg}
        />
      </div>
      <h3>{name}</h3>
    </div>
  );
};
